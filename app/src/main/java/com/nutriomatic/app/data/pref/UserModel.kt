package com.nutriomatic.app.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
)