package com.go.givngo

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import java.util.UUID
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.go.givngo.Database.fetchImageUri
import com.go.givngo.Model.DonationPost
import com.go.givngo.Model.LottieAnimationFromUrl
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONObject

@Composable
fun BrowseDonations() {
    var selectedPost by remember { mutableStateOf<DonationPost?>(null) }
    val firestore = FirebaseFirestore.getInstance()
    val isError = remember { mutableStateOf(false) }
    val storage = FirebaseStorage.getInstance()
    val context = LocalContext.current

    val myDonations = remember { mutableStateListOf<DonationPost>() }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        firestore.collection("GivnGoPublicPost")
            .document("DonationPosts")
            .collection("Posts")
            .orderBy("donation_timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val donations = documents.mapNotNull { doc ->
                    val data = doc.data
                    val documentId = doc.id
                    val claimStatus = data["donation_claim_status"] as? String ?: ""

                    if (claimStatus == "Unclaimed") {
                        DonationPost(
                            documentId = documentId,
                            title = data["donation_post_title"] as? String ?: "No Title",
                            description = data["donation_post_description"] as? String ?: "No Description",
                            donor = data["donor_name"] as? String ?: "Unknown Donor",
                            donor_email = data["donor_email"] as? String ?: "Unknown Email",
                            image_thumbnail = null,
                            images_donation = emptyList()
                        )
                    } else null
                }

                myDonations.addAll(donations)

                donations.forEachIndexed { index, donation ->
                    val formattedEmail = donation.donor_email.replace(".", "_")
                    val formattedTitle = donation.title.replace(" ", "_")
                    val storagePath = "DonationPost/$formattedEmail/$formattedTitle"

                    // Retrieve thumbnail image
                    fetchImageUri(storage, "$storagePath/thumbnail_image", context) { uri ->
                        uri?.let {
                            myDonations[index] = myDonations[index].copy(image_thumbnail = uri)
                        }
                    }

                    // Retrieve additional images (up to 5)
                    val storedImages = mutableListOf<Uri>()
                    for (i in 0..4) {
                        fetchImageUri(storage, "$storagePath/image_$i", context) { uri ->
                            uri?.let {
                                storedImages.add(uri)
                                myDonations[index] = myDonations[index].copy(images_donation = storedImages)
                            }
                        }
                    }
                }

                isError.value = donations.isEmpty()
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
        browseDonationHeadline()

        // Search bar
        BasicTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 30.dp)
                .background(color = Color(0xFFF2F2FF), shape = RoundedCornerShape(28.dp)),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.Top, // Aligns icon and text at the top
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_searchicon),
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp).padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search available donations...",
                                fontSize = 13.sp,
                                color = Color(0xFFB6ACFF),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 2.dp) // Adjust top padding if needed
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        if (isError.value) {
            // Display error message if no donations are available
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                LottieAnimationFromUrl("https://lottie.host/eb406c2a-ca40-4dcf-9539-b14f28cc6bed/DR1tL8FJr8.json")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No donations available",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8070F6)
                )
            }
        } else {
            // Filter donations based on the search query
            val filteredDonations = if (searchQuery.isEmpty()) {
                myDonations
            } else {
                myDonations.filter { it.title.contains(searchQuery, ignoreCase = true) }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredDonations) { donation ->
                    PostDisplayModel(
                        userDonationPost = donation.title,
                        imageUrl = donation.image_thumbnail,
                        onClick = { selectedPost = donation }
                    )
                }
            }
        }
    }
        // Send HTTP POST request
    fun sendPostRequest(url: String, json: JSONObject) {
        val request = JsonObjectRequest(
            Request.Method.POST, url, json,
            Response.Listener { response ->
                Log.d("PushNotification", "Response: $response")
                Toast.makeText(context, "Response: $response", Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Log.e("PushNotification", "Error: $error")
                Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
    
            })

        // Add the request to the request queue
        Volley.newRequestQueue(context).add(request)
    }

        // Push Notification Function
    fun sendPushNotification(fcmToken: String, title: String, body: String, email: String) {
        val url = "https://lt-ruletagalog.vercel.app/api/givngo/trigger"

        val json = JSONObject()
        json.put("fcmToken", fcmToken)
        json.put("title", title)
        json.put("body", body)
        json.put("email", email)

        // Send the POST request to the server
        sendPostRequest(url, json)
    }


    // Display CustomDonationDialog when a donation is selected
    selectedPost?.let { donationPost ->
    CustomDonationDialog(
        donationPost = donationPost,
        onDismissRequest = { selectedPost = null },
        onClaimDonation = {
            Log.d("DonationClaim", "Claimed donation with ID: ${donationPost.documentId}")

            val firestore = FirebaseFirestore.getInstance()
            val recipientEmail = SharedPreferences.getEmail(context) ?: "Developer"
            val recipientFirstName = SharedPreferences.getFirstName(context) ?: "Developer"
            val claimedData = mutableMapOf<String, Any>(
                "documentId" to donationPost.documentId,
                "title" to donationPost.title,
                "description" to donationPost.description,
                "donor" to donationPost.donor,
                "donor_email" to donationPost.donor_email,
                "image_thumbnail" to (donationPost.image_thumbnail?.toString() ?: ""),
                "images_donation" to donationPost.images_donation.map { it.toString() }
            )

            try {
                // Update `donation_claim_status` to "Claimed"
                firestore.collection("GivnGoPublicPost")
                    .document("DonationPosts")
                    .collection("Posts")
                    .document(donationPost.documentId ?: "defaultDocId")
                    .update("donation_claim_status", "Claimed")
                    .addOnSuccessListener {
                        // Check for donor's FCM token in BasicAccounts first, then VerifiedAccounts
                        val donorEmail = donationPost.donor_email

                        // First check in BasicAccounts collection
                        firestore.collection("GivnGoAccounts")
                            .document("BasicAccounts")
                            .collection("Donor")
                            .document(donorEmail)
                            .get()
                            .addOnSuccessListener { donorDoc ->
                                if (donorDoc.exists()) {
                                    // If the donor exists in BasicAccounts, use the FCM token
                                    val fcmToken = donorDoc.getString("fcmToken")
                                    
                                    val accountOne = "BasicAccounts"
                                    
                                    if (fcmToken != null) {
                                        sendPushNotification(fcmToken, "Your donation was claimed!", "Your Donation ${donationPost.title} was claimed by $recipientFirstName", donationPost.donor_email)
                                    }

                                    // Save notification in MyNotifications for BasicAccounts donor
                                    saveNotificationToFirestore(accountOne, firestore, donationPost.donor_email, recipientFirstName, donationPost.title,donationPost.image_thumbnail?.toString() ?: "", donationPost.documentId)
                                } else {
                                    // If not found in BasicAccounts, check in VerifiedAccounts
                                    firestore.collection("GivnGoAccounts")
                                        .document("VerifiedAccounts")
                                        .collection("Donor")
                                        .document(donorEmail)
                                        .get()
                                        .addOnSuccessListener { verifiedDonorDoc ->
                                            if (verifiedDonorDoc.exists()) {
                                            
                                            val accountTwo = "VerifiedAccounts"
                                                // If the donor exists in VerifiedAccounts, use the FCM token
                                                val fcmToken = verifiedDonorDoc.getString("fcmToken")
                                                if (fcmToken != null) {
                                                    sendPushNotification(fcmToken, "Your donation was claimed!", "Your Donation ${donationPost.title} was claimed by $recipientFirstName", donationPost.donor_email)
                                                }

                                                // Save notification in MyNotifications for VerifiedAccounts donor
                                                saveNotificationToFirestore(accountTwo,firestore, donationPost.donor_email, recipientFirstName, donationPost.title,donationPost.image_thumbnail?.toString() ?: "", donationPost.documentId)
                                            } else {
                                                // If donor is not found in either collection
                                                Log.e("DonationClaim", "Donor not found in BasicAccounts or VerifiedAccounts")
                                            }
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("FirestoreError", "Error fetching donor from VerifiedAccounts: ", exception)
                                        }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("FirestoreError", "Error fetching donor from BasicAccounts: ", exception)
                            }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FirestoreError", "Failed to update donation status: ", exception)
                        Toast.makeText(context, "Failed to update donation status: ${exception.message}", Toast.LENGTH_LONG).show()
                    }
            } catch (e: Exception) {
                Log.e("FirestoreError", "Unexpected error during status update: ", e)
                Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
            }

            try {
                // Store the claimed donation in the user's claimed list
                firestore.collection("GivnGoAccounts")
                    .document("BasicAccounts")
                    .collection("Recipient")
                    .document(recipientEmail)
                    .collection("MyClaimedDonations")
                    .document(donationPost.documentId ?: "defaultDocId")
                    .set(claimedData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Donation claimed successfully.")
                        selectedPost = null
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FirestoreError", "Failed to claim donation: ", exception)
                        Toast.makeText(context, "Failed to claim donation: ${exception.message}", Toast.LENGTH_LONG).show()
                    }
            } catch (e: Exception) {
                Log.e("FirestoreError", "Unexpected error during claiming: ", e)
                Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    )
}

}

private fun saveNotificationToFirestore(path: String, firestore: FirebaseFirestore, donorEmail: String, recipientFirstName: String, donationTitle: String, imageThumbnail: String?, documentid: String) {
    val notificationData = hashMapOf(
    "document_id" to documentid,
        "title" to "Your donation was claimed!",
        "body" to "Your Donation $donationTitle was claimed by $recipientFirstName",
        "thumbnail" to imageThumbnail,
        "timestamp" to System.currentTimeMillis()
    )

    val randomUuid = UUID.randomUUID().toString()

    firestore
    .collection("GivnGoAccounts")
        .document(path) // Check BasicAccounts or VerifiedAccounts
        .collection("Donor")
        .document(donorEmail)
        .collection("MyNotifications")
        .document("ClaimedDonations")
        .collection("Notification")
        .add(notificationData)
        .addOnSuccessListener {
            Log.d("Firestore", "Notification added successfully")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error adding notification", e)
        }
}

