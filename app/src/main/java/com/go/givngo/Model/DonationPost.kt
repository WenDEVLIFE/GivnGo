package com.go.givngo.Model

import android.net.Uri

// Data model with a single thumbnail URI
data class DonationPost(
    val documentId: String,
    val pointsId: String,
    val pointsToEarn: Long,
    val title: String,
    val description: String,
    val donor: String,
    val donor_email: String,
    val delivery_method: String,
    val image_thumbnail: Uri?,
    val images_donation: List<Uri>
)