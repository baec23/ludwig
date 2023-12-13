package com.baec23.ludwig.morpher.util

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class AnimatorUtilKtTest {
    @Test
    fun `getClampedIndex clamps correctly`() {
        getClampedIndex(0f, 500) shouldBe 0
        getClampedIndex(1f, 500) shouldBe 500
        getClampedIndex(0.5f, 500) shouldBe 250
        getClampedIndex(1.1f, 500) shouldBe 450
        getClampedIndex(2.1f, 500) shouldBe 450
        getClampedIndex(-0.1f, 500) shouldBe 50
        getClampedIndex(-2.1f, 500) shouldBe 50
    }
}