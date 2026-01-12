package com.example.gymknight.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.gymknight.navigation.ExercisesNavigationScreen
import com.example.gymknight.navigation.MainNavigationScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RootScreen() {
    TabNavigator(MainNavigationScreen()) {
        Scaffold(bottomBar = {
            NavigationBar {
                TabNavigationItem(MainNavigationScreen())
                TabNavigationItem(ExercisesNavigationScreen())
            }
        })
        {
            CurrentTab()
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        },
        alwaysShowLabel = true,
        label = { Text(tab.options.title) }
    )
}