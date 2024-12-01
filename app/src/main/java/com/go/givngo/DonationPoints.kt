package com.go.givngo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@Composable
fun donationPoints() {
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    // State to hold points for each category
    var userCategoryPoints by remember { mutableStateOf(0) }
    var mealsPoints by remember { mutableStateOf(0) }
    var clothingsPoints by remember { mutableStateOf(0) }
    var furnituresPoints by remember { mutableStateOf(0) }
    var booksPoints by remember { mutableStateOf(0) }
    var stationeryPoints by remember { mutableStateOf(0) }
    var householdsPoints by remember { mutableStateOf(0) }

    val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
    val statusType = "Donor"
    val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

    // Fetch category points from Firestore every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            firestore.collection("GivnGoAccounts")
                .document(userAccountType)
                .collection(statusType)
                .document(userCurrentSignedInEmail)
                .collection("MyPoints")
                .document("Post")
                .collection("Unclaimed_Points")
                .get()
                .addOnSuccessListener { documents ->
                    // Reset points for all categories
                    mealsPoints = 0
                    clothingsPoints = 0
                    furnituresPoints = 0
                    booksPoints = 0
                    stationeryPoints = 0
                    householdsPoints = 0

                    for (document in documents) {
                        val category = document.getString("donation_category") ?: ""
                        val points = (document.getLong("donation_points_to_claim") ?: 0L).toInt()

                        // Update points based on category
                        when (category) {
                            "Meals" -> mealsPoints = points
                            "Clothings" -> clothingsPoints = points
                            "Furnitures" -> furnituresPoints = points
                            "Books/Stationery" -> booksPoints = points
                            "Toys" -> stationeryPoints = points
                            "Daily Necessities" -> householdsPoints = points
                        }
                    }

                    // Calculate total points
                    userCategoryPoints = listOf(
                        mealsPoints,
                        clothingsPoints,
                        furnituresPoints,
                        booksPoints,
                        stationeryPoints,
                        householdsPoints
                    ).sum()
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreError", "Failed to retrieve points: ${e.message}")
                }

            // Wait for 5 seconds before the next update
            delay(5000L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 3.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp)
            )
    ) {
        // Container for the title and total points
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, top = 30.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Donation Points Category",
                fontSize = 18.sp,
                color = Color(0xFFF9B733),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Text(
                text = "Your Total Points: $userCategoryPoints",
                fontSize = 13.sp,
                color = Color(0xFF312C3F),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
            ) {
                pointsCategoryMeal(
                    pointsCardsTitles = "Meals",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQB1lygTI9wDhY4yJYk-v4xvUgzhUMqf_OHt1CX7tuM7JOzW_Dwmh4NDlV1&s=10",
                    eachPointsCard = 10,
                    initialNewPointsEachCategory = mealsPoints
                )

                Spacer(modifier = Modifier.height(12.dp))

                pointsCategoryClothings(
                    pointsCardsTitles = "Clothings",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcToALnvCoH1UuiCETyJNZeWxbeG0KxHkqq69-OWWHb5U_w7SuXFB19eVCE&s=10",
                    eachPointsCard = 6,
                    initialNewPointsEachCategory = clothingsPoints
                )

                Spacer(modifier = Modifier.height(12.dp))

                pointsCategoryFurnitures(
                    pointsCardsTitles = "Furnitures",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAdsL7lB8NFQbA5S6mNqI8axzV9_d5SdNBLVxZ9lL7VWZ4eifJcmO-zhI&s=10",
                    eachPointsCard = 4,
                    initialNewPointsEachCategory = furnituresPoints
                )

                Spacer(modifier = Modifier.height(12.dp))

                pointsCategoryBooks(
                    pointsCardsTitles = "Books/Stationery",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_eG3HPE7h_L-XIK8AbYVi7ovWSadwLpYIvg&usqp=CAU",
                    eachPointsCard = 2,
                    initialNewPointsEachCategory = booksPoints
                )

                Spacer(modifier = Modifier.height(12.dp))

                pointsCategoryStationery(
                    pointsCardsTitles = "Toys",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9pxdThd5_YsFcq6owq46FYHlKwtJ-7zIYRVoZnYTq3KKNGvSwdWX_O0-x&s=10",
                    eachPointsCard = 5,
                    initialNewPointsEachCategory = stationeryPoints
                )

                Spacer(modifier = Modifier.height(12.dp))

                pointsCategoryHouseholds(
                    pointsCardsTitles = "Daily Necessities",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQAwumaBlvyQbaJYsk8E0W4N0tW5KzfTNeI2Q&usqp=CAU",
                    eachPointsCard = 8,
                    initialNewPointsEachCategory = householdsPoints
                )
            }
        }
    }
}
