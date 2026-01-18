@file:OptIn(ExperimentalMaterial3Api::class)

package com.g12.periodee.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.g12.periodee.R
import com.g12.periodee.data.CycleHistoryEntry
import com.g12.periodee.data.FirestoreRepository
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(onBack: () -> Unit) {

    val firestore = FirestoreRepository()
    val scope = rememberCoroutineScope()

    var history by remember { mutableStateOf<List<CycleHistoryEntry>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        history = firestore.getCycleHistory()
        loading = false
    }

    val averageCycle = history
        .takeIf { it.isNotEmpty() }
        ?.map { it.cycleLength }
        ?.average()
        ?.toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFE1D8))
    ) {

        // 🔹 Top bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.history_title),
                    color = Color(0xFF5F4B4B)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back_button),
                        tint = Color(0xFFE39AB5)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFEFE1D8)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            // 🔹 Average cycle
            averageCycle?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3F7)
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.average_cycle),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFFE39AB5)
                        )
                        Text(
                            text = "$it ${stringResource(R.string.days)}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // 🔹 Loading
            if (loading) {
                CircularProgressIndicator(
                    color = Color(0xFFE39AB5),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                return@Column
            }

            // 🔹 Empty state
            if (history.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_history),
                    color = Color(0xFF5F4B4B)
                )
                return@Column
            }

            // 🔹 Evolution
            Text(
                text = stringResource(R.string.cycle_evolution),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFE39AB5)
            )

            Spacer(Modifier.height(12.dp))

            Column {
                history.takeLast(6).forEach { entry ->
                    val barWidth = (entry.cycleLength * 4).dp

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = entry.periodStart,
                            modifier = Modifier.width(90.dp),
                            fontSize = 12.sp
                        )
                        Box(
                            modifier = Modifier
                                .height(16.dp)
                                .width(barWidth)
                                .background(
                                    Color(0xFFE39AB5),
                                    RoundedCornerShape(8.dp)
                                )
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "${entry.cycleLength} ${stringResource(R.string.days)}",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // 🔹 Details list
            Text(
                text = stringResource(R.string.details),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFE39AB5)
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn {
                items(history.reversed(), key = { it.id }) { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF8D6E3)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = "${stringResource(R.string.cycle_start)} ${entry.periodStart}"
                                )
                                Text(
                                    text = "${entry.cycleLength} ${stringResource(R.string.days)}",
                                    fontSize = 13.sp,
                                    color = Color(0xFF5F4B4B)
                                )
                            }
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        firestore.deleteCycle(entry.id)
                                        history = firestore.getCycleHistory()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = Color(0xFFE57373)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
