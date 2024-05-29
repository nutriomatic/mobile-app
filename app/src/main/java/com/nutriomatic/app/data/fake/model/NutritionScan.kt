package com.nutriomatic.app.data.fake.model

import java.util.UUID

data class NutritionScan(
    val id: UUID,
    val name: String,
    val type: String,
    val photoUrl: String,
)