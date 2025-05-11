package com.ingencode.reciclaia.ui.screens.app.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.ui.components.Title
import com.ingencode.reciclaia.ui.theme.MyComposeWrapper

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@Composable
fun SettingsScreen(){
    val c = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Title(title = c.getString(R.string.settings))
        Box(modifier = Modifier.fillMaxSize().background(Color.Transparent))
    }
}