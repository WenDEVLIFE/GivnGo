package com.go.givngo

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.go.givngo.donorSide.MyDonation
import com.go.givngo.donorSide.fetchImageUri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Locale

data class Donation(
    val title: String,
    val description: String,
    val category: String,
    val points: String,
    val quantity: String,
    val thumbnailUrl: String,
    val imageUrls: List<String>,
    val postedDate: String
)

@Composable
fun TopBar(onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onMenuClick() },
            modifier = Modifier.padding(end = 1.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_menubar),
                contentDescription = "Menu",
                modifier = Modifier.size(34.dp)
            )
        }

        Text(
            text = "GivnGo",
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun displayMyDonations(
    userClaimedDonation: String,
    donationDescription: String,
    yearPosted: String,
    donationCategory: String,
    donationPoints: String,
    donationQuantity: String,
    image_thumbnail: Uri?,
    donation_stored_images: List<Uri>
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 15.dp, end = 8.dp)
            .clickable { /* onClick() */ }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFAF9FF))
        ) {

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(start = 12.dp, top = 8.dp)
            ) {
                Text(
                    text = yearPosted,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 12.dp),color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start
                )

                Row(verticalAlignment = Alignment.CenterVertically)
                {
                    AsyncImage(
                        model = image_thumbnail,
                        contentDescription = null,
                        modifier = Modifier
                            .width(180.dp)
                            .padding(start = 12.dp,top = 8.dp)
                            .height(180.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(color = Color.Gray),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(start = 12.dp)
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

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 40.dp)
                ) {
                    Text(
                        text = "Points to Earn: $donationPoints",
                        fontSize = 13.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "Quantity: $donationQuantity",
                        fontSize = 13.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                }

            }



        }
    }
}

@Composable
fun myDonations() {
    val context = LocalContext.current
    val userFollowers by remember { mutableStateOf(0) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            // Handle the result from the launched activity
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 3.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp)
            )
    ) {

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .padding(start = 18.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {


            val profileImageUri = SharedPreferences.getProfileImageUri(context) ?: "Developer"
            val profileImageUriParsed = Uri.parse(profileImageUri)
            val statusUser = SharedPreferences.getStatusType(context) ?: "Developer"

            when (statusUser) {
                "Donor", "Recipient", "Rider"-> {
                    AsyncImage(
                        model = profileImageUriParsed,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

            }
            Spacer(modifier = Modifier.width(8.dp))

            Column {

                when (statusUser) {
                    "Donor" -> {
                        val orgName = SharedPreferences.getOrgName(context) ?: "Developer"
                        val basicType = SharedPreferences.getBasicType(context) ?: "Developer"


                        Text(
                            text = "$orgName",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF312C3F),
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Your followers: $userFollowers",
                            fontSize = 14.sp,
                            color = Color(0xFF312C3F)
                        )
                    }
                    "Recipient", "Volunteer" -> {

                        val recipientOrRiders = SharedPreferences.getFirstName(context) ?: "Developer"
                        val basicType = SharedPreferences.getBasicType(context) ?: "Developer"

                        Text(
                            text = "$recipientOrRiders",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF312C3F),
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Your followers: $userFollowers",
                            fontSize = 14.sp,
                            color = Color(0xFF312C3F)
                        )
                    }

                }


            }

            Spacer(modifier = Modifier.width(100.dp))


            Box(modifier = Modifier.size(30.dp).padding(end = 16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrowright),
                    contentDescription = "View profile",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = Color.Gray, bounded = false) // Circular ripple
                        ) {
                            val intent = Intent(context, MyProfile::class.java)
                            launcher.launch(intent)
                        }
                        .align(Alignment.CenterEnd)  // Align the image to the center of the Box
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth().height(6.dp).background( color = Color(0xFFF6F1FF)))

        Spacer(modifier = Modifier.height(16.dp))

        myDonationsHeadline()

        Spacer(modifier = Modifier.height(12.dp))

        searchBarMyDonationList()

    }


}

@Composable
fun myDonationsHeadline() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "My Donations",
            fontSize = 20.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
                .padding(start = 25.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun searchBarMyDonationList() {
    val firestore = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    val context = LocalContext.current
    val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"
    val myDonations = remember { mutableStateListOf<MyDonation>() }
    var searchQuery by remember { mutableStateOf("") }

    // Display all donations if searchQuery is empty; otherwise, display filtered results
    val filteredDonations = if (searchQuery.isEmpty()) {
        myDonations
    } else {
        myDonations.filter {
            it.title.contains(searchQuery, ignoreCase = true)
        }
    }

    // Step 1: Retrieve donation data from Firestore
    LaunchedEffect(Unit) {
        firestore.collection("GivnGoPublicPost")
            .document("DonationPosts")
            .collection("Posts")
            .whereEqualTo("donor_email", userCurrentSignedInEmail)
            .get()
            .addOnSuccessListener { documents ->
                val donations = documents.map { doc ->
                    val data = doc.data
                    MyDonation(
                        title = data["donation_post_title"] as? String ?: "Unknown Title",
                        description = data["donation_post_description"] as? String ?: "No Description",
                        category = data["donation_category"] as? String ?: "Unknown Category",
                        points = data["donation_points"] as? String ?: "Unknown Points",
                        quantity = data["donation_quantity"] as? String ?: "Unknown Quantity",
                        timestamp = data["donation_timestamp"] as? Timestamp ?: Timestamp.now(),
                        thumbnailUri = null,
                        storedImages = emptyList()
                    )
                }
                myDonations.addAll(donations)

                // Step 2: Retrieve image URIs from Firebase Storage based on formatted paths
                donations.forEachIndexed { index, donation ->
                    val formattedEmail = userCurrentSignedInEmail.replace(".", "_")
                    val formattedTitle = donation.title.replace(" ", "_")
                    val storagePath = "DonationPost/$formattedEmail/$formattedTitle"

                    fetchImageUri(storage, "$storagePath/thumbnail_image", context) { uri ->
                        uri?.let {
                            myDonations[index] = myDonations[index].copy(thumbnailUri = uri)
                        }
                    }

                    val storedImages = mutableListOf<Uri>()
                    for (i in 0..4) {
                        fetchImageUri(storage, "$storagePath/image_$i", context) { uri ->
                            uri?.let {
                                storedImages.add(uri)
                                myDonations[index] = myDonations[index].copy(storedImages = storedImages)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error retrieving donation posts", exception)
            }
    }

    Column {
        // Search Bar
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
                        .padding(start = 12.dp,top = 8.dp)
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
                                text = "Search your donations...",
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
        Spacer(modifier = Modifier.height(8.dp))

        // Donation List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(filteredDonations) { index, donation ->
                displayMyDonations(
                    userClaimedDonation = donation.title,
                    donationDescription = donation.description,
                    donationCategory = donation.category,
                    donationPoints = donation.points,
                    donationQuantity = donation.quantity,
                    yearPosted = SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(donation.timestamp.toDate()),
                    image_thumbnail = donation.thumbnailUri,
                    donation_stored_images = donation.storedImages
                )
            }
        }
    }
}
