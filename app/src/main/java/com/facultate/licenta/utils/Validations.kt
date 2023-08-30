package com.facultate.licenta.utils

fun validateEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password: String):Boolean{
    val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
    return password.matches(Regex(passwordPattern))
}
