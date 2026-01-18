package com.g12.periodee

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.g12.periodee.data.UserPreferences
import kotlinx.coroutines.runBlocking
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserPreferencesTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val userPreferences = UserPreferences(context)

    @Test
    fun save_and_read_user_preferences() = runBlocking {
        userPreferences.saveUser(
            firstName = "Lena",
            cycleLength = 28,
            lastPeriodDate = "01/01/2026"
        )

        assertEquals("Lena", userPreferences.getFirstName())
        assertEquals(28, userPreferences.getCycleLength())
        assertEquals("01/01/2026", userPreferences.getLastPeriodDate())
    }
}
