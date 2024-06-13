package com.nutriomatic.app.data.local.model

import androidx.annotation.StringRes
import com.nutriomatic.app.R

data class ProductType(
    val type: Int,
    @StringRes val name: Int,
)