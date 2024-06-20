package com.nutriomatic.app.data.local.model

import androidx.annotation.StringRes

data class TransactionStatus(
    val id: Int,
    val codeName: String,
    @StringRes val nameRes: Int,
)
