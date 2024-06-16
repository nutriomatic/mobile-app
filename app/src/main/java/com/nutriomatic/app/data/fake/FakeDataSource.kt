package com.nutriomatic.app.data.fake

import com.nutriomatic.app.data.fake.model.NutritionScan
import com.nutriomatic.app.data.fake.model.Product
import com.nutriomatic.app.data.fake.model.Transaction
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
        val grades = listOf("A", "B", "C", "D")
        NutritionScan(
            UUID.nameUUIDFromBytes(random.nextBytes(16)),
            "Scan $it",
            "Type $it",
            "https://picsum.photos/seed/$it/2000/4000",
            grades[random.nextInt(grades.size)]
        )
    }

    private val fakeTransactions = List(20) {
        val status = listOf("Pending", "Success", "Failed")
        Transaction(
            id = UUID.randomUUID().toString(),
            price = random.nextDouble(from = 50.0, until = 200.0),
            va = "VA $it",
            startDate = "2021-01-01",
            endDate = "2021-01-31",
            status = status[random.nextInt(status.size)],
            paymentProof = "https://picsum.photos/seed/$it/2000/4000",
            createdAt = "2021-01-01",
            updatedAt = "2021-01-01",
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

    fun generateFakeTransactions(): List<Transaction> {
        return fakeTransactions
    }

    fun getTransactionById(id: String): Transaction? {
        return fakeTransactions.find { it.id == id }
    }
}