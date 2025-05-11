package com.ingencode.reciclaia.ui.navigation.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ingencode.reciclaia.ui.screens.app.history.HistoryScreen
import com.ingencode.reciclaia.ui.screens.app.home.HomeScreen
import com.ingencode.reciclaia.ui.screens.app.learn.LearnScreen
import com.ingencode.reciclaia.ui.screens.app.settings.SettingsScreen

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@Composable
fun NavigationForBottomBar(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = EnumScreensForBottomBar.HomeScreen.name) {
        composable(EnumScreensForBottomBar.HomeScreen.name) {
            HomeScreen()
        }
        composable(EnumScreensForBottomBar.HistoryScreen.name) {
            HistoryScreen()
        }
        composable(EnumScreensForBottomBar.LearnScreen.name) {
            LearnScreen()
        }
        composable(EnumScreensForBottomBar.SettingsScreen.name) {
            SettingsScreen()
        }
    }
}