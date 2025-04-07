package com.ingencode.reciclaia.ui.navigation.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.ingencode.reciclaia.ui.navigation.compose.BottomNavItems.Item1
import com.ingencode.reciclaia.ui.navigation.compose.BottomNavItems.Item2
import com.ingencode.reciclaia.ui.navigation.compose.BottomNavItems.Item3
import com.ingencode.reciclaia.ui.navigation.compose.BottomNavItems.Item4

/**
Created with ❤ by jesusmarvaz on 2025-02-20.
 */

@Composable
fun MyBottomNavigationBar(navHostController: NavHostController) {
    val c = LocalContext.current
    val menuItems = listOf(Item1(c), Item2(c), Item3(c), Item4(c))

    NavigationBar {
        menuItems.forEach { item ->
            val selected = currentRoute(navHostController) == item.route
            NavigationBarItem(selected = selected, onClick = { navHostController.navigate(item.route)}, icon = { Icon(item.icon, item.title) }, label = { Text(item.title) }, alwaysShowLabel = true)
        }
    }
}