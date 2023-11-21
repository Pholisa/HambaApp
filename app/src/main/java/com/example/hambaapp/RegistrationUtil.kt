package com.example.hambaapp

object RegistrationUtil
{
    /**
     * the input is not valid if the email/name/password/confirmpassword/numer is not filled in.
     * confirmed password is not the same as first password
     * password contains less than 2 digits
     */
    fun validateRegistrationInput(
        email:String,
        name:String,
        password: String,
        confirmPassword: String,
        number: String
    ):Boolean {
        return true
    }
}