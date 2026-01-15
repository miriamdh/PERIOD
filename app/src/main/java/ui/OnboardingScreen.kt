package com.g12.periodee.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.g12.periodee.data.UserPreferences
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import java.util.Locale
import com.g12.periodee.data.FirestoreRepository

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {

    val context = LocalContext.current
    //val userPrefs = UserPreferences(context)
    val firestore = FirestoreRepository()
    val scope = rememberCoroutineScope()

    var firstName by remember { mutableStateOf("") }
    var lastPeriodDate by remember { mutableStateOf("") }
    var cycleLength by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFE1D8))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Bienvenue",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Veuillez entrer vos informations")

        Spacer(modifier = Modifier.height(16.dp))

        //Prénom
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Prénom") },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        //Date des dernières règles (calendrier)
        OutlinedTextField(
            value = lastPeriodDate,
            onValueChange = {},
            label = { Text("Date des dernières règles") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            trailingIcon = {
                IconButton(onClick = {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            lastPeriodDate = String.format(
                                Locale.FRANCE,
                                "%02d/%02d/%04d",
                                day,
                                month + 1,
                                year
                            )
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Choisir une date"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Durée du cycle
        OutlinedTextField(
            value = cycleLength,
            onValueChange = { cycleLength = it },
            label = { Text("Durée du cycle (ex : 28)") },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            scope.launch {
                firestore.saveUser(
                    firstName = firstName,
                    cycleLength = cycleLength.toIntOrNull() ?: 28,
                    lastPeriodDate = lastPeriodDate
                )
                onContinue()
            }
        })
        {
            Text("Continuer")
        }
    }
}
