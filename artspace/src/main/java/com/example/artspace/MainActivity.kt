package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme(colorScheme = lightColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
          ArtSpaceScreen()
        }
      }
    }
  }
}

@Composable
fun ArtSpaceScreen() {
  var currentImage by rememberSaveable { mutableIntStateOf(0) }
  val image = when (currentImage) {
    0 -> R.drawable.artwork_1
    1 -> R.drawable.artwork_2
    else -> R.drawable.artwork_3
  }
  val title = when (currentImage) {
    0 -> R.string.image_one
    1 -> R.string.image_two
    else -> R.string.image_three
  }
  val author = when (currentImage) {
    0 -> R.string.author_one
    1 -> R.string.author_two
    else -> R.string.author_three
  }

  BoxWithConstraints(modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp)) {
    if (maxWidth > maxHeight) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Artwork(image, title, Modifier.weight(1f).fillMaxHeight())
        Spacer(Modifier.width(32.dp))
        Column(
          modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          ArtworkDescription(title, author)
          Spacer(Modifier.height(16.dp))
          ArtworkButtons(
            onPrevious = { currentImage = (currentImage + 2) % 3 },
            onNext = { currentImage = (currentImage + 1) % 3 }
          )
        }
      }
    } else {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Artwork(image, title, Modifier.weight(1f).fillMaxWidth())
        Spacer(Modifier.height(32.dp))
        ArtworkDescription(title, author)
        Spacer(Modifier.height(16.dp))
        ArtworkButtons(
          onPrevious = { currentImage = (currentImage + 2) % 3 },
          onNext = { currentImage = (currentImage + 1) % 3 }
        )
      }
    }
  }
}

@Composable
fun Artwork(image: Int, title: Int, modifier: Modifier = Modifier) {
  Surface(modifier = modifier, border = BorderStroke(1.dp, Color.Gray), color = Color.White) {
    Image(
      painter = painterResource(image),
      contentDescription = stringResource(title),
      contentScale = ContentScale.Fit,
      modifier = Modifier.fillMaxSize().padding(12.dp)
    )
  }
}

@Composable
fun ArtworkDescription(title: Int, author: Int) {
  Surface(border = BorderStroke(1.dp, Color.Gray), color = Color.White) {
    Column(
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(stringResource(title), fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
      Text(stringResource(author), modifier = Modifier.padding(top = 8.dp), textAlign = TextAlign.Center)
    }
  }
}

@Composable
fun ArtworkButtons(onPrevious: () -> Unit, onNext: () -> Unit) {
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
    OutlinedButton(
      onClick = onPrevious,
      modifier = Modifier.weight(1f).heightIn(min = 48.dp),
      shape = RectangleShape,
      border = BorderStroke(1.dp, Color.Gray),
      colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFEEEEEE), contentColor = Color.Black)
    ) {
      Text(stringResource(R.string.previous), fontSize = 20.sp)
    }
    OutlinedButton(
      onClick = onNext,
      modifier = Modifier.weight(1f).heightIn(min = 48.dp),
      shape = RectangleShape,
      border = BorderStroke(1.dp, Color.Gray),
      colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFEEEEEE), contentColor = Color.Black)
    ) {
      Text(stringResource(R.string.next), fontSize = 20.sp)
    }
  }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 892)
@Composable
fun ArtSpacePreview() {
  MaterialTheme { ArtSpaceScreen() }
}
