package com.zionhuang.music.ui.utils

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

fun NavController.canNavigateUp(backStack: NavBackStackEntry): Boolean {
    val currentDestination = backStack.destination
    val parent = graph.findNode(currentDestination.id)?.parent
    return parent != null && parent != currentDestination
}