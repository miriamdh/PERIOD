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
import com.g12.periodee.ui.ProfileScreen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.g12.periodee.data.FirestoreRepository
import com.g12.periodee.data.User
import com.g12.periodee.notifications.NotificationHelper


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
        }

        setContent {

            PERIODEETheme {

                LaunchedEffect(Unit) {
                    NotificationHelper.show(
                        context = this@MainActivity,
                        title = "🌸 Period",
                        message = "Les notifications fonctionnent !",
                        id = 1
                    )
                }
                var currentScreen by remember { mutableStateOf("welcome") }
                val scope = rememberCoroutineScope()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFEFE1D8))
                ) {
                    when (currentScreen) {
                        "welcome" -> WelcomeScreen {
                            scope.launch {
                                val firestore = FirestoreRepository()
                                val user = firestore.getUser()

                                currentScreen = if (user != null) "home" else "onboarding"
                            }
                        }

                        "onboarding" -> OnboardingScreen {
                            currentScreen = "home"
                        }
                        "home" -> HomeScreen {
                            currentScreen = "profile"
                        }
                        "profile" -> ProfileScreen(
                            onBack = { currentScreen = "home" },
                            onBackToOnboarding = { currentScreen = "onboarding" }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun HomeScreen(onProfileClick: () -> Unit) {
    val context = LocalContext.current
    val firestore = FirestoreRepository()


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
        val user = firestore.getUser()

        if (user != null) {
            firstName = user.firstName
            lastPeriodDate = user.lastPeriodDate
            cycleLength = user.cycleLength

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE)
            val displayFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.FRANCE)

            val lastDate = LocalDate.parse(lastPeriodDate, formatter)
            val nextPeriodDate = lastDate.plusDays(cycleLength.toLong())

            nextPeriodText = "Prochaine période estimée : ${nextPeriodDate.format(displayFormatter)}"

            val today = LocalDate.now()
            val daysSince = kotlin.math.max(
                0,
                java.time.temporal.ChronoUnit.DAYS.between(lastDate, today).toInt()
            )

            dayOfCycle = (daysSince % cycleLength) + 1
            progress = (dayOfCycle.toFloat() / cycleLength.toFloat()).coerceIn(0f, 1f)

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
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            //Titre
                Text(
                    text = if (firstName.isNotEmpty()) "Bonjour $firstName ♡" else "Bonjour ♡",
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

                Text("Jour $dayOfCycle sur $cycleLength")

                Spacer(modifier = Modifier.height(17.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp),
                    color = Color(0xFFE39AB5)
                )

                Spacer(modifier = Modifier.height(24.dp))
                Text("Phase : $phase")

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8D6E3))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Sport", color = Color(0xFFE39AB5))
                            Spacer(modifier = Modifier.height(8.dp))
                            sportTips.forEach {
                                Text(
                                    "• $it",
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
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8D6E3))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Nutrition", color = Color(0xFFE39AB5))
                            Spacer(modifier = Modifier.height(8.dp))
                            nutritionTips.forEach {
                                Text(
                                    "• $it",
                                    color = Color(0xFF5F4B4B),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onProfileClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE39AB5))
                ) {
                    Text("Modifier mes infos")
                }
            }
        }
    }
}
