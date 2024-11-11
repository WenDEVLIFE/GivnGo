package com.go.givngo.Model

import android.net.Uri

data class DonationSchedule(
    val userClaimedDonation: String,
    val donationDescription: String,
    val imageUri: Uri?,
    val packageStatus: String
)