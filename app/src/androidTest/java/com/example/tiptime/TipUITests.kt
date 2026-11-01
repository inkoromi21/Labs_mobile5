package com.example.tiptime

import androidx.compose.material3.Surface
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat

class TipUITests {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun calculate_20_percent_tip() {
    composeTestRule.setContent {
      TipTimeTheme {
        Surface { TipTimeLayout() }
      }
    }
    composeTestRule.onNodeWithText("Bill Amount").performTextInput("10")
    composeTestRule.onNodeWithText("Bill Amount").performImeAction()
    composeTestRule.onNodeWithText("Tip Percentage").assertIsFocused()
    composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")
    composeTestRule.onNodeWithText("Tip Percentage").performImeAction()
    composeTestRule.onNodeWithText("Bill Amount").assertIsNotFocused()
    composeTestRule.onNodeWithText("Tip Percentage").assertIsNotFocused()
    val expectedTip = NumberFormat.getCurrencyInstance().format(2)
    composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists()
  }

  @Test
  fun roundUpSwitch_changesResult() {
    composeTestRule.setContent {
      TipTimeTheme {
        Surface { TipTimeLayout() }
      }
    }
    composeTestRule.onNodeWithText("Bill Amount").performTextInput("10.5")
    composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")
    val exactTip = NumberFormat.getCurrencyInstance().format(2.1)
    composeTestRule.onNodeWithText("Tip Amount: $exactTip").assertExists()
    composeTestRule.onNode(isToggleable()).performClick()
    val roundedTip = NumberFormat.getCurrencyInstance().format(3)
    composeTestRule.onNodeWithText("Tip Amount: $roundedTip").assertExists()
    composeTestRule.onNode(isToggleable()).performClick()
    composeTestRule.onNodeWithText("Tip Amount: $exactTip").assertExists()
  }
}
