package com.ingencode.reciclaia.ui.screens.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingencode.reciclaia.ui.compose.ReciclaIaTheme

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-12.
 */
@Composable
fun Tutorial() {
    ReciclaIaTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .background(color = MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text("Recicla-IA", style = MaterialTheme.typography.displayLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Tutorial()
}