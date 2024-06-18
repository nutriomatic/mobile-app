package com.nutriomatic.app.data.local.model

import androidx.annotation.DrawableRes

data class Grade(
    val name: String,
    @DrawableRes val label: Int,
)