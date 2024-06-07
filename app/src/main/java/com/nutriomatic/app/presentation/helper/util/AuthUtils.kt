package com.nutriomatic.app.presentation.helper.util

fun isValidEmail(email: CharSequence): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 8
}

fun isValidName(name: String): Boolean {
    return name.length >= 3
}