package com.nutriomatic.app.data.fake.model

import java.util.UUID

data class Product(
    val id: UUID,
    val name: String,
    val price: Float,
    val description: String,
    val isShown: Boolean,
    val fatGrams: Float,
    val proteinGrams: Float,
    val sodiumMilliGrams: Float,
    val carbohydratesGrams: Float,
    val photoUrl: String,
)