package com.nutriomatic.app.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("hg_type")
    val hgType: Int = 1,

    @SerializedName("al_type")
    val alType: Int = 1,
)

data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,
)