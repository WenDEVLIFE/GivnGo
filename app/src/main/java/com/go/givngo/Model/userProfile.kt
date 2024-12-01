package com.go.givngo.Model

data class UserRider(
    val documentId: String = "",
    val user_email: String = "",
    val user_firstName: String = "",
    val user_lastName: String = "",
    val profileImage: String = "",
    val fcmToken: String = ""
)

data class UserDonor(
    val documentId: String = "",
    val user_email: String = "",
    val donor_org: String = "",
    val profileImage: String = "",
    val fcmToken: String = ""
)

