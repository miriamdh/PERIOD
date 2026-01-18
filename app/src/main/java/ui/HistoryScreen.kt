@file:OptIn(ExperimentalMaterial3Api::class)

package com.g12.periodee.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.g12.periodee.data.CycleHistoryEntry
import com.g12.periodee.data.FirestoreRepository
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


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

    val averageCycle =
        history.takeIf { it.isNotEmpty() }
            ?.map { it.cycleLength }
            ?.average()
            ?.toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFE1D8))
    ) {

        //Top bar avec retour
        TopAppBar(
            title = {
                Text(
                    "Mon historique",
                    color = Color(0xFF5F4B4B)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Retour",
                        tint = Color(0xFFE39AB5)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFEFE1D8)
            )
        )

        // Contenu
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {


        //Moyenne
        averageCycle?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3F7))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Moyenne de cycle",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFE39AB5)
                    )
                    Text(
                        "$it jours",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
        }

        if (loading) {
            CircularProgressIndicator(
                color = Color(0xFFE39AB5),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            return@Column
        }

        if (history.isEmpty()) {
            Text(
                "Aucun cycle enregistré pour le moment 🌱",
                color = Color(0xFF5F4B4B)
            )
            return@Column
        }

        //Mini graph
        Text(
            "Évolution de mes cycles",
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
                        entry.periodStart,
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
                    Text("${entry.cycleLength} j", fontSize = 12.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        //Liste
        Text(
            "Détails",
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8D6E3))
                ) {
                    Row(
                        Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Début : ${entry.periodStart}")
                            Text(
                                "${entry.cycleLength} jours",
                                fontSize = 13.sp,
                                color = Color(0xFF5F4B4B)
                            )
                        }

                        IconButton(onClick = {
                            scope.launch {
                                firestore.deleteCycle(entry.id)
                                history = firestore.getCycleHistory()
                            }
                        }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Supprimer",
                                tint = Color(0xFFE57373)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

//        Button(
//            onClick = onBack,
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(24.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE39AB5))
//        ) {
//            Text("Retour")
//        }
    }
}
}
