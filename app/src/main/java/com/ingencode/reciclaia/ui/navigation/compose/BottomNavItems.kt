package com.ingencode.reciclaia.ui.navigation.compose

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DynamicFeed
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.ingencode.reciclaia.R

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

sealed class BottomNavItems(val title: String, val icon: ImageVector, val route: String) {
    class Item1(val context: Context): BottomNavItems(context.getString(R.string.home), Icons.Rounded.Home,
        EnumScreensForBottomBar.HomeScreen.name)
    class Item2(val context: Context): BottomNavItems(context.getString(R.string.history), Icons.Rounded.History,
        EnumScreensForBottomBar.HistoryScreen.name)
    class Item3(val context: Context): BottomNavItems(context.getString(R.string.learn), Icons.Rounded.DynamicFeed,
        EnumScreensForBottomBar.LearnScreen.name)
    class Item4(val context: Context): BottomNavItems(context.getString(R.string.settings), Icons.Rounded.Settings,
        EnumScreensForBottomBar.SettingsScreen.name)
}