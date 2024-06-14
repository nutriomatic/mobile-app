package com.nutriomatic.app.data.local.model

import androidx.annotation.StringRes

data class HealthGoal(
    val type: Int,
    @StringRes val desc: Int,
)
