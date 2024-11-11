package com.go.givngo

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.go.givngo.Database.fetchImageUri
import com.go.givngo.Extras.VoucherMarket
import com.go.givngo.Model.DonorDetails
import com.go.givngo.recipientSide.ClaimedDonations
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun AppDrawer(finishBasic: String,
              onItemClicked: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val context = LocalContext.current
    val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

    // State to hold donor details
    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    var donorDetails by remember { mutableStateOf(DonorDetails()) }
    var isLoading by remember { mutableStateOf(true) }
    val statusUser = SharedPreferences.getStatusType(context) ?: "Developer"

    // Fetch donor details from Firestore
    LaunchedEffect(userCurrentSignedInEmail) {
        if (userCurrentSignedInEmail.isNotEmpty()) {

            val collectionPath = when (statusUser) {
                "Donor" -> "Donor"
                "Recipient" -> "Recipient"
                "Volunteer" -> "Volunteer"
                else -> return@LaunchedEffect // Exit if finishBasic is not recognized
            }


            firestore.collection("GivnGoAccounts")
                .document("BasicAccounts")
                .collection(collectionPath)
                .document(userCurrentSignedInEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        donorDetails = when (statusUser) {
                            "Donor" -> DonorDetails(
                                organizationName = document.getString("OrganizationName") ?: "",
                                statusAccount = document.getString("Status_account") ?: "",
                                uriProfileImage = document.getString("profileImage") ?: ""
                            )
                            "Recipient" -> DonorDetails(
                                firstName = document.getString("FirstName") ?: "",
                                lastName = document.getString("LastName") ?: "",
                                statusAccount = document.getString("Status_account") ?: "",
                                uriProfileImage = document.getString("profileImage") ?: ""
                            )
                            "Volunteer" -> DonorDetails(
                                firstName = document.getString("FirstName") ?: "",
                                lastName = document.getString("LastName") ?: "",
                                statusAccount = document.getString("Status_account") ?: "",
                                uriProfileImage = document.getString("profileImage") ?: ""
                            )
                            else -> DonorDetails() // Default case
                        }
                    }
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    // Handle the error (optional)
                    isLoading = false

                    Toast.makeText(context, "Error 303: " + exception , Toast.LENGTH_LONG).show()

                }
        } else {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(30.dp))
                .padding(top = 18.dp, bottom = 20.dp)
        ) {

            // Header box with rounded corners
            Box(
                modifier = Modifier
                    .height(28.dp)
                    .background(
                        color = Color(0xFFE9E7FF),
                        shape = RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp)
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize().padding(top = 3.dp)
                ) {
                    Text(
                        text = "$statusUser Account!",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF917BFF),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // User profile
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 18.dp)
            ) {

                when (statusUser) {
                    "Donor", "Recipient", "Volunteer" -> {
                        val profileImageUri = SharedPreferences.getProfileImageUri(context) ?: "Developer"
                        val profileImageUriParsed = Uri.parse(profileImageUri)

                        if(profileImageUriParsed != null){

                            AsyncImage(
                                model = profileImageUriParsed,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                        } else {
                            var newUriProfileImage by remember { mutableStateOf<Uri?>(null) }
                            // Fetch profile image from Firestore if profileImageUriParsed is null
                            LaunchedEffect(Unit) {
                                val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                                val firestorePath = FirebaseFirestore.getInstance()
                                val storage = FirebaseStorage.getInstance()
                                val formattedEmail = userCurrentSignedInEmail.replace(".", "_")
                                val storagePath = "GivnGoAccounts/$formattedEmail/profileImages/image_1"

                                firestorePath.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            fetchImageUri(storage, storagePath, context) { uri ->
                                                uri?.let {
                                                    SharedPreferences.saveProfileImageUri(context, uri.toString())
                                                    newUriProfileImage = uri
                                                }
                                            }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("Firestore", "Error retrieving document", exception)
                                    }
                            }

                            // Display the new profile image if available, otherwise show placeholder
                            AsyncImage(
                                model = newUriProfileImage,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                    }
                    "Developer" ->{

                        Image(
                            painter = painterResource(id = R.drawable.profile_test), // Replace with actual image resource
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    if (isLoading) {
                        Text(text = "Loading...", fontWeight = FontWeight.Bold, color = Color.Gray)
                    } else {
                        when (statusUser) {
                            "Donor" -> {
                                val orgName = SharedPreferences.getOrgName(context) ?: "Developer"
                                val basicType = SharedPreferences.getBasicType(context) ?: "Developer"


                                Text(
                                    text = orgName,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF917BFF),
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = basicType,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            "Recipient", "Volunteer" -> {
                                val recipientOrRiders = SharedPreferences.getFirstName(context) ?: "Developer"
                                val basicType = SharedPreferences.getBasicType(context) ?: "Developer"

                                Text(
                                    text = "$recipientOrRiders",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF917BFF),
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = basicType,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Drawer items
            DrawerItem(text = "Dashboard", isSelected = true) {
                onItemClicked("Dashboard")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (statusUser) {
                "Donor" -> {
                    DrawerItem(
                        text = "Market Points",
                        onClick = {
                            val intent = Intent(context, VoucherMarket::class.java)
                            context.startActivity(intent)
                        }
                    )

                }
                "Recipient" -> {
                    DrawerItem(
                        text = "Claim Donation",
                        onClick = {
                            val intent = Intent(context, ClaimedDonations::class.java)
                            context.startActivity(intent)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DrawerItem(text = "My Notifications",
                onClick = {
                    val intent = Intent(context, MyNotifications::class.java)
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DrawerItem(text = "Settings",
                onClick = {
                    val intent = Intent(context, mySettings::class.java)
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Logout section
            TextButton(
                onClick = {

                    when (statusUser) {
                        "Donor" -> {
                            SharedPreferences.clearBasicType(context)
                            SharedPreferences.clearEmail(context)
                            SharedPreferences.clearStatusType(context)
                            SharedPreferences.clearOrgName(context)
                            SharedPreferences.clearAdd(context)
                            SharedPreferences.clearHashPass(context)
                            SharedPreferences.clearProfileImageUri(context)
                        }
                        "Recipient" -> {
                            SharedPreferences.clearEmail(context)
                            SharedPreferences.clearAdd(context)
                            SharedPreferences.clearHashPass(context)
                            SharedPreferences.clearBasicType(context)
                            SharedPreferences.clearStatusType(context)
                            SharedPreferences.clearFirstName(context)
                            SharedPreferences.clearProfileImageUri(context)
                        }
                        "Volunteer" -> {
                            SharedPreferences.clearEmail(context)
                            SharedPreferences.clearBasicType(context)
                            SharedPreferences.clearHashPass(context)
                            SharedPreferences.clearStatusType(context)
                            SharedPreferences.clearAdd(context)
                            SharedPreferences.clearFirstName(context)
                            SharedPreferences.clearProfileImageUri(context)
                        }

                    }

                    val intent = Intent(context, greetings::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.align(Alignment.Start).height(45.dp).padding(start = 30.dp).clip(
                    RoundedCornerShape(30.dp)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "•",
                            fontSize = 18.sp,
                            color = Color(0xFFD6171E),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Log Out",
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 16.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}




