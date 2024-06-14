package com.nutriomatic.app.data.local.model

import androidx.annotation.StringRes

data class Gender(
    val code: Int,
    @StringRes val name: Int,
)