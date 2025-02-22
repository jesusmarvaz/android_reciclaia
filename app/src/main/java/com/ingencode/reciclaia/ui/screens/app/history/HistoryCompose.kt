package com.ingencode.reciclaia.ui.screens.app.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.ui.components.Title

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@Composable
fun HistoryScreen() {
    /*Column(modifier = Modifier.fillMaxSize().padding(16.dp).background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Pantalla Historial (Compose)", style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.tertiary)
    }*/
    val c = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Title(title = c.getString(R.string.history))
        Box(modifier = Modifier.fillMaxSize().background(Color.Transparent))
    }
}