package com.go.givngo.Model

import android.net.Uri
import com.google.firebase.Timestamp

data class MyDonation(
    val title: String,
    val description: String,
    val category: String,
    val points: String,
    val quantity: String,
    val timestamp: Timestamp,
    val thumbnailUri: Uri? = null,
    val storedImages: List<Uri> = emptyList()
)