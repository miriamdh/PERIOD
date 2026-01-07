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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.style.TextAlign

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

    var dayOfCycle by remember { mutableStateOf(1) }
    var progress by remember { mutableStateOf(0f) }
    var phase by remember { mutableStateOf("") }
    var sportTips by remember { mutableStateOf(listOf<String>()) }
    var nutritionTips by remember { mutableStateOf(listOf<String>()) }

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
            nextPeriodText =
                "Prochaine période estimée : ${nextPeriodDate.format(displayFormatter)}"

            val today = LocalDate.now()
            val daysSince =
                kotlin.math.max(
                    0,
                    java.time.temporal.ChronoUnit.DAYS.between(lastDate, today).toInt()
                )

            dayOfCycle = (daysSince % cycleLength) + 1
            progress =
                (dayOfCycle.toFloat() / cycleLength.toFloat()).coerceIn(0f, 1f)

            when {
                dayOfCycle <= 5 -> {
                    phase = "Règles"
                    sportTips = listOf("Marche douce", "Yoga / étirements")
                    nutritionTips = listOf("Repas chauds", "Fer + vitamine C")
                }

                dayOfCycle <= 13 -> {
                    phase = "Phase folliculaire"
                    sportTips = listOf("Cardio léger", "Renforcement doux")
                    nutritionTips = listOf("Protéines", "Fruits et légumes")
                }

                dayOfCycle <= 16 -> {
                    phase = "Ovulation"
                    sportTips = listOf("Séance plus intense", "Renfo + cardio")
                    nutritionTips = listOf("Bonnes graisses", "Hydratation")
                }

                else -> {
                    phase = "Phase lutéale"
                    sportTips = listOf("Marche", "Yoga / mobilité")
                    nutritionTips = listOf("Magnésium", "Fibres + protéines")
                }
            }
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
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = if (firstName.isNotEmpty())
                        "Bonjour $firstName ♡"
                    else
                        "Bonjour ♡",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 34.sp
                )

                Spacer(modifier = Modifier.height(17.dp))

                if (nextPeriodText.isNotEmpty()) {
                    Text(
                        text = nextPeriodText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFE39AB5)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Jour $dayOfCycle sur $cycleLength",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(17.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                        .height(16.dp),
                    color = Color(0xFFE39AB5)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Phase : $phase",
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8D6E3)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Sport",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFE39AB5)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            sportTips.forEach {
                                Text(
                                    text = "• $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF5F4B4B),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8D6E3)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Nutrition",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFE39AB5)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            nutritionTips.forEach {
                                Text(
                                    text = "• $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF5F4B4B),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}