package com.g12.periodee.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.*
import java.time.LocalDate
import com.g12.periodee.data.CycleHistoryEntry
import androidx.compose.ui.res.stringResource
import com.g12.periodee.R


import com.g12.periodee.data.FirestoreRepository
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onBackToOnboarding: () -> Unit
) {
    val firestore = FirestoreRepository()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastPeriodDate by remember { mutableStateOf("") }
    var cycleLengthText by remember { mutableStateOf("28") }

    // Charger l'utilisateur depuis Firestore
    LaunchedEffect(Unit) {
        val user = firestore.getUser()
        if (user != null) {
            firstName = user.firstName
            lastPeriodDate = user.lastPeriodDate
            cycleLengthText = user.cycleLength.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFE1D8))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(stringResource(R.string.first_name_label)) },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = lastPeriodDate,
            onValueChange = {},
            label = { Text(stringResource(R.string.last_period_label)) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            trailingIcon = {
                IconButton(onClick = {
                    val cal = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, y, m, d ->
                            lastPeriodDate = String.format("%02d/%02d/%04d", d, m + 1, y)
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.pick_date)
                    )
                }
            }
        )


        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = cycleLengthText,
            onValueChange = { cycleLengthText = it },
            label = { Text(stringResource(R.string.cycle_length_label)) },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    val cycleLength = cycleLengthText.toIntOrNull() ?: 28
                    firestore.addCycleToHistory(
                        CycleHistoryEntry(
                            periodStart = lastPeriodDate,
                            cycleLength = cycleLength,
                            recordedAt = LocalDate.now().toString()
                        )
                    )
                    firestore.saveUser(
                        firstName = firstName,
                        cycleLength = cycleLength,
                        lastPeriodDate = lastPeriodDate
                    )
                    onBack()
                }
            },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE39AB5))
        ) {
            Text(stringResource(R.string.save_button))
        }

        TextButton(onClick = onBack) {
            Text(stringResource(R.string.back_button))
        }

        TextButton(onClick = {
            scope.launch {
                firestore.saveUser("", 28, "")
                onBackToOnboarding()
            }
        }) {
            Text(stringResource(R.string.reset_data))
        }
    }
}



