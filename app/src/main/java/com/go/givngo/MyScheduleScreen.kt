package com.go.givngo

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.go.givngo.Model.DonationSchedule
import com.go.givngo.Model.LottieAnimationFromUrl
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun mySchedules() {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("Deliveries") }
    val pickupSchedules = remember { mutableStateListOf<DonationSchedule>() }
    val deliverySchedules = remember { mutableStateListOf<DonationSchedule>() }
    val firestore = FirebaseFirestore.getInstance()

    val isError = remember { mutableStateOf(false) }

    val statusType = "Donor"
    val emailDonor = SharedPreferences.getOrgName(context) ?: "developer@gmail.com"

    LaunchedEffect(Unit) {
        firestore.collection("GivnGoAccounts")
            .document(statusType)
            .collection("MySchedules")
            .document(emailDonor)
            .collection("schedules") // Assuming schedules are stored in this sub-collection
            .get()
            .addOnSuccessListener { querySnapshot ->
                pickupSchedules.clear()
                deliverySchedules.clear()
                for (document in querySnapshot.documents) {
                    val claimStatus = document.getString("donation_schedule_status")
                    val donationPost = DonationSchedule(
                        userClaimedDonation = document.getString("donation_post_title") ?: "",
                        donationDescription = document.getString("donation_post_description") ?: "",
                        packageStatus = document.getString("donation_status") ?: "",
                        imageUri = null // Update this if there’s an image field
                    )

                    // Add to respective lists based on claimStatus
                    if (claimStatus == "Pickup") {
                        pickupSchedules.add(donationPost)
                    } else if (claimStatus == "Delivery") {
                        deliverySchedules.add(donationPost)
                    }
                }
                // Set error state to true if both lists are empty
                isError.value = deliverySchedules.isEmpty() && pickupSchedules.isEmpty()
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error retrieving donation posts", exception)
                isError.value = true
            }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 3.dp)
            .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        topCategoryRecipientMySchdules()

        // Category selection row
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 30.dp)
        ) {
            categoryDonationChooser("Deliveries", selectedCategory) { selectedCategory = it }
            categoryDonationChooser("Pickups", selectedCategory) { selectedCategory = it }
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalLine()
        Spacer(modifier = Modifier.height(4.dp))

        // Determine the selected items list based on the category
        val itemsList = if (selectedCategory == "Deliveries") deliverySchedules else pickupSchedules

        if (itemsList.isEmpty()) {
            // Show Lottie animation if the selected category's list is empty
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                LottieAnimationFromUrl("https://lottie.host/b9cc6eec-b9da-4596-b151-e0b2d5a6d9be/0bfHlCFArT.json")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Schedules",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8070F6)
                )
            }
        } else {
            // Display the LazyRow for the selected items list
            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(itemsList.size) { index ->
                    val donation = itemsList[index]
                    displaySchedulesDonationModel(
                        userClaimedDonation = donation.userClaimedDonation,
                        imageUri = donation.imageUri,
                        donationDescription = donation.donationDescription,
                        packageStatus = donation.packageStatus,
                        onClick = {
                            /* val intent = Intent(context, YourTargetActivity::class.java).apply {
                                 putExtra("userClaimedDonation", donation.userClaimedDonation)
                                 putExtra("donationDescription", donation.donationDescription)
                                 putExtra("packageStatus", donation.packageStatus)
                                 donation.imageUri?.let { uri ->
                                     putExtra("imageUri", uri.toString())
                                 }
                             }
                             context.startActivity(intent) */
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun displaySchedulesDonationModel(
    userClaimedDonation: String,
    donationDescription: String,
    imageUri: Uri?,
    packageStatus: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFAF9FF))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(color = Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = userClaimedDonation,
                        fontSize = 15.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = donationDescription,
                        fontSize = 13.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        Text(
            text = packageStatus,
            fontSize = 13.sp,
            color = Color(0xFF8171F8),
            modifier = Modifier.padding(top = 6.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun topCategoryRecipientMySchdules() {

    Column(
        modifier = Modifier.fillMaxWidth(),  // Ensures the column takes the full width
        horizontalAlignment = Alignment.Start // Aligns the content to the start
    ) {
        Text(
            text = "Schedules",
            fontSize = 23.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
                .padding(start = 25.dp) // Padding from the start for centering purpose
                .fillMaxWidth(),  // Ensures the text takes the full width
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start // Align text to the start within the text box
        )
    }
}