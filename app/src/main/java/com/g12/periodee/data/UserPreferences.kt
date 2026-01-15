package com.g12.periodee.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val FIRST_NAME = stringPreferencesKey("first_name")
        val CYCLE_LENGTH = intPreferencesKey("cycle_length")
        val LAST_PERIOD_DATE = stringPreferencesKey("last_period_date")
    }

    suspend fun saveUser(
        firstName: String,
        cycleLength: Int,
        lastPeriodDate: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[FIRST_NAME] = firstName
            prefs[CYCLE_LENGTH] = cycleLength
            prefs[LAST_PERIOD_DATE] = lastPeriodDate
        }
    }

    suspend fun getFirstName(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[FIRST_NAME]
    }

    suspend fun getLastPeriodDate(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[LAST_PERIOD_DATE]
    }
    suspend fun getCycleLength(): Int? {
        val prefs = context.dataStore.data.first()
        return prefs[CYCLE_LENGTH]
    }
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

}

