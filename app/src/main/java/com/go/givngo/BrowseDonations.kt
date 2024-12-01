package com.go.givngo

import android.annotation.SuppressLint
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
import android.content.Context
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
import com.google.firebase.firestore.DocumentSnapshot
import com.go.givngo.subscriptionDialog
import android.content.Intent

@Composable
fun BrowseDonations() {
    var selectedPost by remember { mutableStateOf<DonationPost?>(null) }
    val firestore = FirebaseFirestore.getInstance()
    val isError = remember { mutableStateOf(false) }
    val storage = FirebaseStorage.getInstance()
    val context = LocalContext.current

    val myDonations = remember { mutableStateListOf<DonationPost>() }
    var searchQuery by remember { mutableStateOf("") }
    
    
    val statusType = "Recipient"
    val emailRecipient = SharedPreferences.getEmail(context) ?: "developer@gmail.com"
    val accountBasicType = SharedPreferences.getBasicType(context) ?: "developer@gmail.com"
    var claimLimits by remember { mutableStateOf(0L) }
    var addClaim by remember { mutableStateOf(0L) }

    var postIdentification by remember { mutableStateOf("") }
    var donorPointsForPost by remember { mutableStateOf(0L) }

    var donorPoints by remember { mutableStateOf(0L) }
    var accountFound = false
    var accountFoundTwo = false
    var accountFoundFive =false
    
    val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")

    fun listenToAccountChangesDonorGet(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Recipient")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                     claimLimits = documentSnapshot.getLong("recipient_claim_limits") ?: 0L
                     addClaim = documentSnapshot.getLong("recipient_claim_status") ?: 0L
                    accountFoundTwo = true

                    }
            }
    }
    
    LaunchedEffect(Unit) {
        listenToAccountChangesDonorGet("BasicAccounts", emailRecipient)

        if (!accountFoundTwo) {
            listenToAccountChangesDonorGet("VerifiedAccounts", emailRecipient)
        }
    }

    fun listenToAccountChangesDonorUpdatePoints(collectionPath: String, email: String, postIdentification:String, donorPointsForPost:Long) {

        accountsRef.document(collectionPath).collection("Donor")
            .document(email)
            .collection("MyPoints")
            .document("Post")
            .collection("Unclaimed_Points")
            .document(postIdentification)
            .update(
                "donation_points_to_claim", donorPointsForPost
            )
            .addOnSuccessListener {
accountFoundFive = true
            }
    }

    fun listenToAccountChangesRecipientUpdate(collectionPath: String, email: String, newClaim: Long) {

                accountsRef.document(collectionPath).collection("Recipient")
            .document(email)
            .update(
            "recipient_claim_status", newClaim
            )
            .addOnSuccessListener {

            }
    }



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
                            pointsId = data["donatioon_points_id"] as? String ?: "No Title",
                            pointsToEarn =  data["donation_points_to_earn"] as? Long ?: 0L,
                            title = data["donation_post_title"] as? String ?: "No Title",
                            description = data["donation_post_description"] as? String ?: "No Description",
                            donor = data["donor_name"] as? String ?: "Unknown Donor",
                            donor_email = data["donor_email"] as? String ?: "Unknown Email",
                            delivery_method = data["delivery_method"] as? String ?: "Unknown Delivery Method",
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
                        onClick = { 
            selectedPost = donation
            
                        
                         }
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


    fun saveNotificationToFirestorePickUp(path: String, firestore: FirebaseFirestore, donorEmail: String, recipientFirstName: String, donationTitle: String, imageThumbnail: String?, documentid: String, deliveryPickup: String) {
    val emailRecipient = SharedPreferences.getEmail(context) ?: "developer@gmail.com"
    
    val notificationData = hashMapOf(
    "document_id" to documentid,
        "title" to "Your donation was claimed!",
        "body" to "Your Donation $donationTitle was claimed by $recipientFirstName ",
        "thumbnail" to imageThumbnail,
        "Delivery_method" to deliveryPickup,
        "recipient_email" to emailRecipient,
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

    fun handleDonorAccountLogic(
        firestore: FirebaseFirestore,
        donationPost: DonationPost,
        donorDoc: DocumentSnapshot,
        accountType: String,
        recipientFirstName: String,
        recipientEmail: String
    ) {
        val fcmToken = donorDoc.getString("fcmToken") ?: ""

        if (fcmToken != null) {
        // Update the donation post to include the recipient's email
        firestore.collection("GivnGoPublicPost")
            .document("DonationPosts")
            .collection("Posts")
            .document(donationPost.documentId)
            .update("claimed by", recipientEmail)
            .addOnSuccessListener {
                Log.d("FirestoreUpdate", "Successfully updated 'claimed by' field")

                listenToAccountChangesDonorUpdatePoints("BasicAccounts", donationPost.donor_email, donationPost.pointsId, donationPost.pointsToEarn)

                if (!accountFoundFive) {
                    listenToAccountChangesDonorUpdatePoints("VerifiedAccounts", donationPost.donor_email, donationPost.pointsId, donationPost.pointsToEarn)
                }

                }
            .addOnFailureListener { exception ->
                Log.e("FirestoreUpdateError", "Failed to update 'claimed by' field", exception)
            }

        // Send a push notification to the donor
        sendPushNotification(
            fcmToken,
            "Your donation was claimed!",
            "Your Donation ${donationPost.title} was claimed by $recipientFirstName",
            donationPost.donor_email
        )

        // Save notification details
        saveNotificationToFirestore(
            accountType,
            context,
            firestore,
            donationPost.donor_email,
            recipientFirstName,
            donationPost.title,
            donationPost.image_thumbnail?.toString() ?: "",
            donationPost.documentId,
            donationPost.delivery_method
        )
    } else {
        Log.e("DonorAccountLogic", "FCM token not found for donor in $accountType")
    }
}

    

selectedPost?.let { donationPost ->
    CustomDonationDialog(
        donationPost = donationPost,
        onDismissRequest = { selectedPost = null },
        onClaimDonation = {
            Log.d("DonationClaim", "Claimed donation with ID: ${donationPost.documentId}")

            if (addClaim != claimLimits) { // Only proceed if addClaim is not equal to claimLimits
                addClaim += 1L // Increment addClaim by 1
                
                // Call the function to update recipient claim status
                listenToAccountChangesRecipientUpdate("BasicAccounts", emailRecipient, addClaim)

                // Check if account is not found and update in VerifiedAccounts
                if (!accountFound) {
                    listenToAccountChangesRecipientUpdate("VerifiedAccounts", emailRecipient, addClaim)
                }

                val firestore = FirebaseFirestore.getInstance()
                val recipientEmail = SharedPreferences.getEmail(context) ?: "Developer"
                val recipientFirstName = SharedPreferences.getFirstName(context) ?: "Developer"
                val claimedData = mutableMapOf<String, Any>(
                    "documentId" to donationPost.documentId,
                    "title" to donationPost.title,
                    "description" to donationPost.description,
                    "donor" to donationPost.donor,
                    "donor_email" to donationPost.donor_email,
                    "delivery_method" to donationPost.delivery_method,
                    "image_thumbnail" to (donationPost.image_thumbnail?.toString() ?: ""),
                    "images_donation" to donationPost.images_donation.map { it.toString() }
                )

                val pickUpClaimedData = mutableMapOf<String, Any>(
                    "documentId" to donationPost.documentId,
                    "donation_post_title" to donationPost.title,
                    "donation_post_description" to donationPost.description,
                    "donor" to donationPost.donor,
                    "donor_email" to donationPost.donor_email,
                    "donation_schedule_status" to donationPost.delivery_method,
                    "donation_status" to "Pickup Status: Donor waiting for pickup",
                    "delivery_method" to donationPost.delivery_method,
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
                                        handleDonorAccountLogic(
                                            firestore,
                                            donationPost,
                                            donorDoc,
                                            "BasicAccounts",
                                            recipientFirstName,
                                            recipientEmail
                                        )
                                    } else {
                                        // If not found in BasicAccounts, check in VerifiedAccounts
                                        firestore.collection("GivnGoAccounts")
                                            .document("VerifiedAccounts")
                                            .collection("Donor")
                                            .document(donorEmail)
                                            .get()
                                            .addOnSuccessListener { verifiedDonorDoc ->
                                                if (verifiedDonorDoc.exists()) {
                                                    handleDonorAccountLogic(
                                                        firestore,
                                                        donationPost,
                                                        verifiedDonorDoc,
                                                        "VerifiedAccounts",
                                                        recipientFirstName,
                                                        recipientEmail
                                                    )
                                                } else {
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
                            if (donationPost.delivery_method == "Pick-Up") {
                                firestore.collection("GivnGoAccounts")
                                    .document("BasicAccounts")
                                    .collection("Recipient")
                                    .document(emailRecipient)
                                    .collection("MySchedules")
                                    .document("Donations")
                                    .collection("Schedules")
                                    .document(donationPost.documentId ?: "defaultDocId")
                                    .set(pickUpClaimedData)
                                    .addOnSuccessListener {
                                        Log.d("Firestore", "Pickup donation")
                                        selectedPost = null
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d("FirestoreError", "Pickup donation error", exception)
                                    }
                            }
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
            }else{
            val intent = Intent(context, subscriptionDialog::class.java)
            context.startActivity(intent)
            }
        }
    )
}


}

private fun saveNotificationToFirestore(path: String,context: Context, firestore: FirebaseFirestore, donorEmail: String, recipientFirstName: String, donationTitle: String, imageThumbnail: String?, documentid: String, deliveryPickup: String) {
  
   val emailRecipient = SharedPreferences.getEmail(context) ?: "developer@gmail.com"
    val notificationData = hashMapOf(
    "document_id" to documentid,
        "title" to "Your donation was claimed!",
        "body" to "Your Donation $donationTitle was claimed by $recipientFirstName",
        "Delivery_method" to deliveryPickup,
        "thumbnail" to imageThumbnail,
        "recipient_email" to emailRecipient,
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
