package com.go.givngo
import com.go.givngo.ui.theme.MyComposeApplicationTheme
import com.go.givngo.ui.modifer.drawColoredShadow
import com.go.givngo.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*

import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable

import kotlinx.coroutines.launch

import androidx.compose.foundation.*

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.navigation.compose.currentBackStackEntryAsState

import com.go.givngo.bottomBar.model.*
import com.go.givngo.bottomBar.*
import com.go.givngo.bottomBar.components.*
import com.go.givngo.OverscrollEffect.*
import com.go.givngo.progressIndicator.DotProgressIndicator
import com.go.givngo.MainActivity

import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.res.vectorResource

import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.compose.rememberAsyncImagePainter

import androidx.compose.foundation.Image
import androidx.core.view.WindowCompat
import androidx.compose.ui.platform.LocalContext

import androidx.core.view.WindowInsetsControllerCompat

import androidx.compose.ui.ExperimentalComposeUiApi

import androidx.core.view.ViewCompat

import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.overscroll

import androidx.compose.ui.graphics.Color


import java.util.Calendar

import androidx.compose.foundation.interaction.MutableInteractionSource


import android.content.Intent


import android.app.Activity


import androidx.compose.material.OutlinedTextField
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.gestures.Orientation

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import coil.compose.rememberImagePainter

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import android.util.Log

import com.go.givngo.SharedPreferences

import androidx.activity.compose.BackHandler

import com.go.givngo.Model.DonationPackageRequest

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import android.widget.Toast
import org.json.JSONObject

import kotlin.random.Random
import androidx.activity.compose.BackHandler
import com.go.givngo.Model.DonationSchedule

import java.util.UUID

import com.go.givngo.Model.DonationPackageMyRoutes

import android.net.Uri

import androidx.compose.ui.window.Dialog

import com.go.givngo.Model.DonationPost

@Composable
fun CustomDonationDialog(
    donationPost: DonationPost,
    onDismissRequest: () -> Unit,
    onClaimDonation: () -> Unit
) {
var donationDeliveryMethod = donationPost.delivery_method ?: ""

val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                var donorprofileImage by remember { mutableStateOf("") }
                
                
fun listenToAccountChanges(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Donor")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    donorprofileImage = documentSnapshot.getString("profileImage") ?: ""
                    accountFound= true
                    }
            }
    }
    
    LaunchedEffect(Unit) {
        listenToAccountChanges("BasicAccounts", donationPost.donor_email)
        
        
        if (!accountFound) {
            listenToAccountChanges("VerifiedAccounts", donationPost.donor_email)
            
        }
    }
    
    val profileImageUriParsed = Uri.parse(donorprofileImage)


    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDismissRequest()
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Transparent)
            ) {
            Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp,bottom = 8.dp, top = 8.dp)
    ) {
    
    AsyncImage(
                            model = profileImageUriParsed,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
    
    Text(
                        text = donationPost.donor,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 8.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

    
    }
            
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    AsyncImage(
                        model = donationPost.image_thumbnail,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .padding(start = 10.dp, end = 40.dp, top = 60.dp)
                            .clip(RoundedCornerShape(23.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = donationPost.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 10.dp, bottom = 12.dp)
                )
                Text(
                    text = donationPost.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    modifier = Modifier.padding(start = 10.dp, bottom = 18.dp)
                )
                Text(
                    text = "Donation Delivery Method: $donationDeliveryMethod",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    modifier = Modifier.padding(start = 10.dp, bottom = 40.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .height(60.dp)
                            .padding(8.dp)
                            .wrapContentWidth()
                            .clip(RoundedCornerShape(25.dp))
                            .background(color = Color(0xFF8070F6)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            onClick = onClaimDonation,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF6F5FF)
                            ),
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(4.dp)
                                .wrapContentWidth(),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_donationpackage),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(2.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = "Claim donation...",
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .padding(start = 2.dp),
                                    color = Color(0xFF8070F6),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}