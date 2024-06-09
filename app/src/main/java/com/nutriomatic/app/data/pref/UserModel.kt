package com.nutriomatic.app.data.pref

data class UserModel(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val gender: Int? = null,
    val role: String? = null,
    val telp: String? = null,
    val profpic: String? = null,
    val birthdate: String? = null,
    val place: String? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val weightGoal: Int? = null,
    val hgId: String? = null,
    val hgType: Int? = null,
    val hgDesc: String? = null,
    val alId: String? = null,
    val alType: Int? = null,
    val alDesc: String? = null,
    val alValue: Double? = null,
)