package com.nutriomatic.app.data.fake

import com.nutriomatic.app.data.fake.model.NutritionScan
import com.nutriomatic.app.data.fake.model.Product
import java.util.UUID
import kotlin.random.Random

private const val SEED = 14159L

object FakeDataSource {
    private val random = Random(SEED)
    private val fakeProducts = List(20) {
        Product(
            id = UUID.nameUUIDFromBytes(random.nextBytes(16)),
            name = "Product $it",
            price = random.nextInt(from = 50, until = 200) * 100,
            type = "Type $it",
            description = "Description $it",
            isShown = random.nextBoolean(),
            servingSizePerContainer = 1,
            fatGrams = random.nextFloat(),
            proteinGrams = random.nextFloat(),
            sodiumMilliGrams = random.nextFloat(),
            carbohydratesGrams = random.nextFloat(),
            photoUrl = "https://picsum.photos/seed/$it/200/300"
        )
    }

    private val fakeScans = List(20) {
        NutritionScan(
            UUID.nameUUIDFromBytes(random.nextBytes(16)),
            "Scan $it",
            "Type $it",
            "https://picsum.photos/seed/$it/2000/4000"
        )
    }

    fun generateFakeProduct(): List<Product> {
        return fakeProducts
    }

    fun getProductById(id: UUID): Product? {
        return fakeProducts.find { it.id == id }
    }

    fun generateFakeScans(): List<NutritionScan> {
        return fakeScans
    }

    fun getScanById(id: UUID): NutritionScan? {
        return fakeScans.find { it.id == id }
    }
}