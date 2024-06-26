package com.nutriomatic.app.data.local

import android.content.Context
import com.nutriomatic.app.R
import com.nutriomatic.app.data.local.model.ActivityLevel
import com.nutriomatic.app.data.local.model.Classification
import com.nutriomatic.app.data.local.model.Gender
import com.nutriomatic.app.data.local.model.Grade
import com.nutriomatic.app.data.local.model.HealthGoal
import com.nutriomatic.app.data.local.model.ProductType
import com.nutriomatic.app.data.local.model.TransactionStatus

object LocalData {
    val GENDERS = listOf(
        Gender(1, R.string.gender_male),
        Gender(2, R.string.gender_female),
    )

    val PRODUCT_TYPES = listOf(
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

    val ACTIVITY_LEVELS = listOf(
        ActivityLevel(1, R.string.activity_level_sedentary),
        ActivityLevel(2, R.string.activity_level_lightly_active),
        ActivityLevel(3, R.string.activity_level_moderately_active),
        ActivityLevel(4, R.string.activity_level_very_active),
        ActivityLevel(5, R.string.activity_level_super_active),
    )

    val HEALTH_GOALS = listOf(
        HealthGoal(1, R.string.health_goal_lose_weight),
        HealthGoal(2, R.string.health_goal_maintain_weight),
        HealthGoal(3, R.string.health_goal_gain_weight),
    )

    //    val GRADES = listOf("A", "B", "C", "D")
    val GRADES = listOf(
        Grade("?", R.drawable.label_default),
        Grade("A", R.drawable.label_a),
        Grade("B", R.drawable.label_b),
        Grade("C", R.drawable.label_c),
        Grade("D", R.drawable.label_d),
    )

    val TRANSACTION_STATUS = listOf(
        TransactionStatus(0, "unadvertise", R.string.tsc_status_unadvertise),
        TransactionStatus(1, "advertise", R.string.tsc_status_advertise),
        TransactionStatus(2, "process", R.string.tsc_status_process),
        TransactionStatus(3, "decline", R.string.tsc_status_decline),
    )

    val CLASSIFICATIONS = listOf(
        Classification(0, R.string.classification_insufficient_weight),
        Classification(1, R.string.classification_normal_weight),
        Classification(2, R.string.classification_obesity_type_1),
        Classification(3, R.string.classification_obesity_type_2),
        Classification(4, R.string.classification_obesity_type_3),
        Classification(5, R.string.classification_overweight_level_1),
        Classification(6, R.string.classification_overweight_level_2),
    )

    fun getGenderNames(context: Context): List<String> {
        return GENDERS.map { context.getString(it.name) }
    }

    fun getGenderCodeByName(context: Context, name: String): Int {
        return GENDERS.find { context.getString(it.name) == name }?.code ?: GENDERS.first().code
    }

    fun getGenderNameByCode(context: Context, code: Int): String {
        return GENDERS.find { it.code == code }?.let { context.getString(it.name) }
            ?: context.getString(GENDERS.first().name)
    }

    fun getProductTypeNames(context: Context): List<String> {
        return PRODUCT_TYPES.map { context.getString(it.name) }
    }

    fun getProductTypeCodeByName(context: Context, name: String): Int {
        return PRODUCT_TYPES.find { context.getString(it.name) == name }?.type
            ?: PRODUCT_TYPES.first().type
    }

    fun getProductTypeNameByCode(context: Context, code: Int): String {
        return PRODUCT_TYPES.find { it.type == code }?.let { context.getString(it.name) }
            ?: context.getString(PRODUCT_TYPES.first().name)
    }

    fun getActivityLevelNames(context: Context): List<String> {
        return ACTIVITY_LEVELS.map { context.getString(it.desc) }
    }

    fun getActivityLevelCodeByName(context: Context, name: String): Int {
        return ACTIVITY_LEVELS.find { context.getString(it.desc) == name }?.type
            ?: ACTIVITY_LEVELS.first().type
    }

    fun getActivityLevelNameByCode(context: Context, code: Int): String {
        return ACTIVITY_LEVELS.find { it.type == code }?.let { context.getString(it.desc) }
            ?: context.getString(ACTIVITY_LEVELS.first().desc)
    }

    fun getHealthGoalNames(context: Context): List<String> {
        return HEALTH_GOALS.map { context.getString(it.desc) }
    }

    fun getHealthGoalCodeByName(context: Context, name: String): Int {
        return HEALTH_GOALS.find { context.getString(it.desc) == name }?.type
            ?: HEALTH_GOALS.first().type
    }

    fun getHealthGoalNameByCode(context: Context, code: Int): String {
        return HEALTH_GOALS.find { it.type == code }?.let { context.getString(it.desc) }
            ?: context.getString(HEALTH_GOALS.first().desc)
    }

    fun getGradeNames(): List<String> {
        return GRADES.map { it.name }.drop(1)
    }

    fun getGradeLabelByName(name: String): Int {
        return GRADES.find { it.name.lowercase() == name.lowercase() }?.label
            ?: GRADES.first().label
    }

    fun getTransactionStatusNameByCode(context: Context, code: Int): String {
        return TRANSACTION_STATUS.find { it.id == code }?.let { context.getString(it.nameRes) }
            ?: context.getString(TRANSACTION_STATUS.first().nameRes)
    }

    fun getTransactionStatusCodeByName(context: Context, name: String): Int {
        return TRANSACTION_STATUS.find { context.getString(it.nameRes) == name }?.id
            ?: TRANSACTION_STATUS.first().id
    }

    fun getTransactionStatusCodeNameByName(context: Context, name: String): String {
        return TRANSACTION_STATUS.find { context.getString(it.nameRes) == name }?.codeName
            ?: TRANSACTION_STATUS.first().codeName
    }

    fun getClassificationNameByCode(context: Context, code: Int): String {
        return CLASSIFICATIONS.find { it.code == code }?.let { context.getString(it.name) }
            ?: context.getString(CLASSIFICATIONS.first().name)
    }
}