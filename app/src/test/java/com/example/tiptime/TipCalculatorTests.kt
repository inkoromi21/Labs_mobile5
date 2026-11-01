package com.example.tiptime

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

class TipCalculatorTests {
  @Test
  fun calculateTip_20PercentNoRoundup() {
    val expectedTip = NumberFormat.getCurrencyInstance().format(2)
    assertEquals(expectedTip, calculateTip(10.0, 20.0, false))
  }

  @Test
  fun calculateTip_roundsUpToNextWholeNumber() {
    val expectedTip = NumberFormat.getCurrencyInstance().format(3)
    assertEquals(expectedTip, calculateTip(10.5, 20.0, true))
  }

  @Test
  fun calculateTip_doesNotRoundWithoutSwitch() {
    val expectedTip = NumberFormat.getCurrencyInstance().format(2.1)
    assertEquals(expectedTip, calculateTip(10.5, 20.0, false))
  }

  @Test
  fun calculateTip_zeroBill() {
    val expectedTip = NumberFormat.getCurrencyInstance().format(0)
    assertEquals(expectedTip, calculateTip(0.0, 20.0, true))
  }
}
