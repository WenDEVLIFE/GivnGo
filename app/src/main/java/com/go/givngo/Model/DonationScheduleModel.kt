package com.go.givngo.Model

import android.net.Uri
import java.io.Serializable
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

data class DonationSchedule(
    val documentId: String,
    val userClaimedDonation: String,
    val donationDescription: String,
    val donationThumbnail: String = "",
    val packageStatus: String,
    val donationQuantity: String = "",
    val donorAddress: String = "",
    val donorEmail: String = "",
    val emailRider: String = "",
    val documentIdFromRider: String = "",
    val ticketTrackingId: String = "",
    val timeRecieved: String = ""
) : Serializable



data class DonationPackageRequest(
    var documentId: String = "",
    val userClaimedDonation: String = "",
    val donationDescription: String = "",
    val donationQuantity: String = "",
    val donorAddress: String = "",
    val donorEmail: String = "",
    val recipientEmail: String = "",
    val donationThumbnail: String = "",
    val packageStatus: String = "",
    val recipient_document_id_package: String = "",
    val packaged_time_packed: String = "",
    val donation_schedule_status: String = ""
) : Serializable

@Parcelize
data class DonationPackageMyRoutes(
    val documentId: String = "",
    val userClaimedDonation: String = "",
    val donationDescription: String = "",
    val donationQuantity: String = "",
    val donorAddress: String = "",
    val donorEmail: String = "",
    val recipientEmail: String = "",
    val donationThumbnail: String = "",
    val packageStatus: String = "",
    val ticketId: String = ""
) : Parcelable

