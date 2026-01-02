package com.g12.periodee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.g12.periodee.ui.OnboardingScreen
import com.g12.periodee.ui.WelcomeScreen
import com.g12.periodee.ui.theme.PERIODEETheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.g12.periodee.data.UserPreferences
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PERIODEETheme {

                var currentScreen by remember { mutableStateOf("welcome") }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEFE1D8))
                ) {
                    when (currentScreen) {
                        "welcome" -> WelcomeScreen {
                            currentScreen = "onboarding"
                        }
                        "onboarding" -> OnboardingScreen {
                            currentScreen = "home"
                        }
                        "home" -> HomeScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val userPrefs = UserPreferences(context)

    var firstName by remember { mutableStateOf("") }
    var lastPeriodDate by remember { mutableStateOf("") }
    var cycleLength by remember { mutableStateOf(28) }
    var nextPeriodText by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        firstName = userPrefs.getFirstName() ?: ""
        lastPeriodDate = userPrefs.getLastPeriodDate() ?: ""
        cycleLength = userPrefs.getCycleLength() ?: 28

        if (lastPeriodDate.isNotEmpty()) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE)
            val displayFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.FRANCE)

            val lastDate = LocalDate.parse(lastPeriodDate, formatter)
            val nextPeriodDate = lastDate.plusDays(cycleLength.toLong())

            nextPeriodText = "Prochaine période estimée : ${nextPeriodDate.format(displayFormatter)}"
        }

        visible = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = if (firstName.isNotEmpty())
                        "Bonjour $firstName ♡"
                    else
                        "Bonjour ♡",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 34.sp
                )

                if (nextPeriodText.isNotEmpty()) {
                    Text(
                        text = nextPeriodText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
