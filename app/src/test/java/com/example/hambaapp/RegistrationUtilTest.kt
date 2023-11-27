package com.example.hambaapp

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {
    @Test
    fun `empty username returns false` () {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "juliet",
            "123",
            "123",
            "1234"
        )

        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and correctly repeated password returns true` () {
        val result2 = RegistrationUtil.validateRegistrationInput(
            "juliet@gmail.com",
            "juliet",
            "123",
            "123",
            "1234"
        )

        assertThat(result2).isTrue()
    }

    @Test
    fun `confirmpassword is equal password` () {
        val result = RegistrationUtil.validateRegistrationInput(
            "juliet@gmail.com",
            "juliet",
            "12345",
            "123",
            "1234"
        )

        assertThat(result).isFalse()
    }
}