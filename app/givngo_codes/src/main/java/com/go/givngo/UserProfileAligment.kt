package com.go.givngo

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.go.givngo.donorSide.fetchImageUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun userProfileAlignment() {
    val context = LocalContext.current
    val profileImageUri = SharedPreferences.getProfileImageUri(context) ?: "Developer"
    val profileImageUriParsed = Uri.parse(profileImageUri)
    val statusUser = SharedPreferences.getStatusType(context) ?: "Developer"

    val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp,bottom = 8.dp, top = 8.dp)
    ) {

        val imagePainter = runCatching { painterResource(id = R.drawable.profile_test) }.getOrNull()

        if (imagePainter != null) {

            when (statusUser) {
                "Donor", "Recipient", "Rider"-> {


                    if(profileImageUriParsed != null){
                        AsyncImage(
                            model = profileImageUriParsed,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }else {
                        var newUriProfileImage by remember { mutableStateOf<Uri?>(null) }
                        // Fetch profile image from Firestore if profileImageUriParsed is null
                        LaunchedEffect(Unit) {
                            val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                            val firestore = FirebaseFirestore.getInstance()
                            val storage = FirebaseStorage.getInstance()
                            val formattedEmail = userCurrentSignedInEmail.replace(".", "_")
                            val storagePath = "GivnGoAccounts/$formattedEmail/profileImages/image_1"

                            firestore.collection("GivnGoAccounts")
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
            }


        } else {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF6F5FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "JK",
                    color = Color(0xFF8070F6),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {

            when (statusUser) {
                "Donor" -> {
                    val orgName = SharedPreferences.getOrgName(context) ?: "Developer"

                    Text(
                        text = "$orgName",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                } "Recipient" -> {

                val fName = SharedPreferences.getFirstName(context) ?: "Developer"
                val lName = SharedPreferences.getLastName(context) ?: "Developer"

                Text(
                    text = fName + " " + lName,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

            }
                "Rider" -> {

                    val fName = SharedPreferences.getFirstName(context) ?: "Developer"
                    val lName = SharedPreferences.getLastName(context) ?: "Developer"



                    Text(
                        text = fName + " " + lName,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                }
            }

            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }


    }
}

