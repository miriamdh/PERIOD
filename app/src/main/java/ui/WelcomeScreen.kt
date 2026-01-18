package com.g12.periodee.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.g12.periodee.R

@Composable
fun WelcomeScreen(onFinish: () -> Unit) {

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(6000) // temps visible
        visible = false
        delay(1200) // fondu de sortie
        onFinish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFE1D8)),
        contentAlignment = Alignment.Center
    ) {

    AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.welcome_subtitle_1),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(R.string.welcome_subtitle_2),
                style = MaterialTheme.typography.bodyMedium
            )

            }
        }
    }
}
