package com.nutriomatic.app.data.local.model

import androidx.annotation.StringRes

data class ProductType(
    val type: Int,
    @StringRes val name: Int,
)