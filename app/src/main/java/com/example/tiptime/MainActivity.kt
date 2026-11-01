package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TipTimeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          TipTimeLayout()
        }
      }
    }
  }
}

@Composable
fun TipTimeTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = lightColorScheme(
      primary = Color(0xFF7D5260),
      secondary = Color(0xFF7D5260),
      surface = Color(0xFFFFFBFE),
      surfaceVariant = Color(0xFFF1DEE3),
      surfaceContainerHighest = Color(0xFFF1DEE3)
    ),
    content = content
  )
}

@Composable
fun TipTimeLayout() {
  var amountInput by rememberSaveable { mutableStateOf("") }
  var tipInput by rememberSaveable { mutableStateOf("") }
  var roundUp by rememberSaveable { mutableStateOf(false) }
  val amount = amountInput.replace(',', '.').toDoubleOrNull() ?: 0.0
  val tipPercent = tipInput.replace(',', '.').toDoubleOrNull() ?: 0.0
  val tip = calculateTip(amount, tipPercent, roundUp)
  val focusManager = LocalFocusManager.current

  Column(
    modifier = Modifier
      .fillMaxSize()
      .safeDrawingPadding()
      .imePadding()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = stringResource(R.string.calculate_tip),
      modifier = Modifier.align(Alignment.Start).padding(top = 24.dp, bottom = 16.dp)
    )
    EditNumberField(
      value = amountInput,
      onValueChange = { amountInput = it },
      label = R.string.bill_amount,
      icon = R.drawable.ic_bill,
      imeAction = ImeAction.Next,
      keyboardActions = KeyboardActions(onNext = {
        focusManager.moveFocus(FocusDirection.Down)
      }),
      modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
    )
    EditNumberField(
      value = tipInput,
      onValueChange = { tipInput = it },
      label = R.string.tip_percentage,
      icon = R.drawable.ic_percent,
      imeAction = ImeAction.Done,
      keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
      modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    )
    RoundTheTipRow(roundUp = roundUp, onRoundUpChanged = { roundUp = it })
    Text(
      text = stringResource(R.string.tip_amount, tip),
      fontSize = 30.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(top = 28.dp, bottom = 24.dp)
    )
    Spacer(modifier = Modifier.height(120.dp))
  }
}

@Composable
fun EditNumberField(
  value: String,
  onValueChange: (String) -> Unit,
  label: Int,
  icon: Int,
  imeAction: ImeAction,
  keyboardActions: KeyboardActions,
  modifier: Modifier = Modifier
) {
  TextField(
    value = value,
    onValueChange = onValueChange,
    label = { Text(stringResource(label)) },
    leadingIcon = { Icon(painterResource(icon), contentDescription = null) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction),
    keyboardActions = keyboardActions,
    modifier = modifier
  )
}

@Composable
fun RoundTheTipRow(roundUp: Boolean, onRoundUpChanged: (Boolean) -> Unit) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(stringResource(R.string.round_up_tip))
    Switch(checked = roundUp, onCheckedChange = onRoundUpChanged)
  }
}

@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
  var tip = tipPercent / 100 * amount
  if (roundUp) {
    tip = ceil(tip)
  }
  return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimePreview() {
  TipTimeTheme {
    TipTimeLayout()
  }
}
