package com.nutriomatic.app.data.local

import android.content.Context
import com.nutriomatic.app.R
import com.nutriomatic.app.data.local.model.ProductType

object LocalData {
    private val PRODUCT_TYPES = listOf(
        ProductType(201, R.string.product_type_snack),
        ProductType(202, R.string.product_type_chocolate_bar),
        ProductType(203, R.string.product_type_ice_cream),
        ProductType(204, R.string.product_type_bread),
        ProductType(205, R.string.product_type_cheese),
        ProductType(206, R.string.product_type_frozen_food),
        ProductType(301, R.string.product_type_water),
        ProductType(302, R.string.product_type_juice),
        ProductType(303, R.string.product_type_milk),
        ProductType(304, R.string.product_type_coffee),
        ProductType(305, R.string.product_type_tea),
    )

    fun getProductTypeNames(context: Context): List<String> {
        return PRODUCT_TYPES.map { context.getString(it.name) }
    }

    fun getProductTypeCodeByName(context: Context, name: String): Int {
        return PRODUCT_TYPES.find { context.getString(it.name) == name }?.type ?: 0
    }

    fun getProductTypeNameByCode(context: Context, code: Int): String {
        return PRODUCT_TYPES.find { it.type == code }?.let { context.getString(it.name) } ?: ""
    }
}