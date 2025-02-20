package com.ingencode.reciclaia.ui.navigation.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
Created with ❤ by jesusmarvaz on 2025-02-20.
 */

@Composable
fun currentRoute(navController: NavController): String? {
    return navController.currentBackStackEntryAsState().value?.destination?.route
}