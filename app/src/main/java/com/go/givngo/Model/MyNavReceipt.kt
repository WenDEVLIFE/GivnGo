package com.go.givngo.Model

import com.go.givngo.R


sealed class MainNavRecipient(val route: String, val iconResId: Int, val title: String) {
    object Home : MainNavRecipient("Home", R.drawable.ic_homeheart, "Home")
    object Donations : MainNavRecipient("Donations", R.drawable.ic_donationpackage, "Donations")
    object Schedules : MainNavRecipient("Schedules", R.drawable.ic_deliveries, "My Schedules")
}

