package com.nutriomatic.app.data.local.model

import androidx.annotation.StringRes

data class ActivityLevel(
    val type: Int,
    @StringRes val desc: Int,
)