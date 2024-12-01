package com.go.givngo.Model

import com.go.givngo.R

sealed class RiderNavigationScreen(val route: String, val iconResId: Int, val title: String) {
    object Home : RiderNavigationScreen("Home", R.drawable.ic_homeheart, "Home")
    object Deliv : RiderNavigationScreen("Deliveries", R.drawable.ic_deliveries, "Deliveries")
    object MyRoutes : RiderNavigationScreen("My Routes", R.drawable.ic_deliveries, "My Routes")
}