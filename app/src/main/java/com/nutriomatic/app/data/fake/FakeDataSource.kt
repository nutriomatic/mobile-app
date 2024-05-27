package com.nutriomatic.app.data.fake

import com.nutriomatic.app.data.fake.model.Product
import java.util.UUID
import kotlin.random.Random

private const val SEED = 14159L

object FakeDataSource {
    private val random = Random(SEED)

    fun generateFakeProduct(): List<Product> {
        return List(5) {
            Product(
                UUID.nameUUIDFromBytes(random.nextBytes(16)),
                "Product $it",
                random.nextFloat(),
                "Description $it",
                random.nextBoolean(),
                random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                random.nextFloat(),
                "https://picsum.photos/seed/$it/200/300"
            )
        }
    }
}