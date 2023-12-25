package com.zionhuang.music.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.currentBackStackEntryAsState
fun NavController.canNavigateUp(backStack: NavBackStackEntry): Boolean {
    val currentDestination = backStack.destination
    val parent = graph.findNode(currentDestination.id)?.parent
    return parent != null && parent != currentDestination
}