package com.nutriomatic.app.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveUserModel(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = user.id.toString()
            preferences[NAME_KEY] = user.name.toString()
            preferences[EMAIL_KEY] = user.email.toString()
            user.gender?.let { preferences[GENDER_KEY] = it }
        }
    }

    fun getUserModel(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                id = preferences[ID_KEY],
                name = preferences[NAME_KEY],
                email = preferences[EMAIL_KEY],
                gender = preferences[GENDER_KEY]
            )
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { it[TOKEN_KEY] }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ID_KEY = stringPreferencesKey("id")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val GENDER_KEY = intPreferencesKey("gender")
        private val ROLE_KEY = stringPreferencesKey("role")
        private val TELP_KEY = stringPreferencesKey("telp")
        private val PROFPIC_KEY = stringPreferencesKey("profpic")
        private val BIRTHDATE_KEY = stringPreferencesKey("birthdate")
        private val PLACE_KEY = stringPreferencesKey("place")
        private val HEIGHT_KEY = intPreferencesKey("height")
        private val WEIGHT_KEY = intPreferencesKey("weight")
        private val WEIGHT_GOAL_KEY = intPreferencesKey("weight_goal")
        private val HG_TYPE_KEY = intPreferencesKey("hg_type")
        private val AL_TYPE_KEY = intPreferencesKey("al_type")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}