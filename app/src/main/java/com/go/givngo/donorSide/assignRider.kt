package com.go.givngo.donorSide

import com.go.givngo.ui.theme.MyComposeApplicationTheme
import com.go.givngo.ui.modifer.drawColoredShadow

import com.go.givngo.donorSide.*
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue

import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.AnimatedVisibility

import androidx.navigation.NavController
import com.go.givngo.R

import android.content.Context
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.res.vectorResource

import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.compose.rememberAsyncImagePainter
import java.util.UUID
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
import kotlinx.coroutines.delay


import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.activity.result.contract.ActivityResultContracts

import com.go.givngo.SharedPreferences

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import androidx.activity.compose.BackHandler

import android.widget.Toast
import android.net.Uri

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.window.Dialog

import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair

import com.go.givngo.recipientSide.ClaimedDonations
import android.util.Log

import com.google.firebase.firestore.Query
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore

import com.airbnb.lottie.compose.*

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.Timestamp

import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.DisposableEffect
import com.go.givngo.MyNotifications
import com.go.givngo.mySettings
import com.go.givngo.MyProfile
import androidx.compose.foundation.lazy.itemsIndexed
import com.google.firebase.storage.StorageException
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.foundation.text.BasicTextField
import com.go.givngo.Extras.VoucherMarket

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley


class AssignRider : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
                MyAppAsignRider()
            }
        }
    }
}



@Composable
fun MyAppAsignRider() {
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as AssignRider)

    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.Transparent.toArgb()

        WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            ?.isAppearanceLightStatusBars = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color.Transparent,
            topBar = {
                TopBarAssign(onMenuClick = { activity.finish() }, isScrolled = scrollState.value > 0)
            },
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .background(color = Color.White)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
             
                ListVolunteers()
            }
        }
    }
}


@Composable
fun LottieAnimationFromUrl(url: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(url))
    LottieAnimation(
        composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .size(150.dp) // Adjust the size as needed
    )
}


@Composable
fun ListVolunteers() {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }  // Default category "All"
    val allSchedules = remember { mutableStateListOf<AllTypes>() }
    val riderSchedules = remember { mutableStateListOf<RiderSchedule>() }
    val carSchedules = remember { mutableStateListOf<CarSchedule>() }
    val bikeSchedules = remember { mutableStateListOf<BikeSchedule>() }
    val firestore = FirebaseFirestore.getInstance()

    val isError = remember { mutableStateOf(false) }

    val statusType = "Donor"
    val emailDonor = SharedPreferences.getOrgName(context) ?: "developer@gmail.com"

    LaunchedEffect(Unit) {
        firestore.collection("GivnGoRiders")
            .document("Volunteers")
            .collection("AvailableVolunteers")
            .get()
            .addOnSuccessListener { querySnapshot ->
                allSchedules.clear()
                riderSchedules.clear()
                carSchedules.clear()
                bikeSchedules.clear()

                for (document in querySnapshot.documents) {
                    
 
                    val donationPost = AllTypes(
                    volunteerName = document.getString("volunteer_firstname") ?: "",
                        courriertype = document.getString("volunteer_courrier") ?: "",
                        volunteerEmail = document.getString("volunteer_email") ?: "",
                        imageProfile = document.getString("volunteer_profileimage") ?: ""
                    )
                    
                    allSchedules.add(donationPost)

                    // Add to the corresponding list based on the selected category
                    when (document.getString("volunteer_courrier")) {
                        "Rider" -> riderSchedules.add(RiderSchedule(volunteerName = document.getString("volunteer_firstname") ?: "",
                        courriertype = document.getString("volunteer_courrier") ?: "",
                        volunteerEmail = document.getString("volunteer_email") ?: "",
                        imageProfile = document.getString("volunteer_profileimage") ?: ""))
                        "Car" -> carSchedules.add(CarSchedule(volunteerName = document.getString("volunteer_firstname") ?: "",
                        courriertype = document.getString("volunteer_courrier") ?: "",
                        volunteerEmail = document.getString("volunteer_email") ?: "",
                        imageProfile = document.getString("volunteer_profileimage") ?: ""))
                        "Bike" -> bikeSchedules.add(BikeSchedule(volunteerName = document.getString("volunteer_firstname") ?: "",
                        courriertype = document.getString("volunteer_courrier") ?: "",
                        volunteerEmail = document.getString("volunteer_email") ?: "",
                        imageProfile = document.getString("volunteer_profileimage") ?: ""))
                    }
                }
                // Set error state to true if all lists are empty
                isError.value = riderSchedules.isEmpty() && carSchedules.isEmpty() && bikeSchedules.isEmpty() && allSchedules.isEmpty()
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

        

        // Category selection row
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 30.dp)
        ) {
            categoryDonationChooser("All", selectedCategory) { selectedCategory = it }
            categoryDonationChooser("Rider", selectedCategory) { selectedCategory = it }
            categoryDonationChooser("Car", selectedCategory) { selectedCategory = it }
            categoryDonationChooser("Bike", selectedCategory) { selectedCategory = it }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Determine the selected items list based on the category
        val itemsList = when (selectedCategory) {
            "All" -> allSchedules
            "Rider" -> riderSchedules
            "Car" -> carSchedules
            "Bike" -> bikeSchedules
            else -> allSchedules
        }

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
          /*  // Display the LazyRow for the selected items list
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
                            // Handle click here
                        }
                    )
                }
            }*/
            
            
        }
    }
}

// AllTypes data class
data class AllTypes(
    val volunteerName: String,
    val courriertype: String,
    val imageProfile: String,
    val volunteerEmail: String
)

// RiderSchedule data class
data class RiderSchedule(    
    val volunteerName: String,
    val courriertype: String,
    val imageProfile: String,
    val volunteerEmail: String
)

// CarSchedule data class
data class CarSchedule(    
    val volunteerName: String,
    val courriertype: String,
    val imageProfile: String,
    val volunteerEmail: String
)

// BikeSchedule data class
data class BikeSchedule(    
    val volunteerName: String,
    val courriertype: String,
    val imageProfile: String,
    val volunteerEmail: String
)

@Composable
fun TopBarAssign(onMenuClick: () -> Unit, isScrolled: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawColoredShadow(
                color = Color.Black, // Shadow color
                alpha = 0.1f, // Shadow alpha based on scroll state
                borderRadius = 0.dp, // Rounded corners
                blurRadius = 2.dp, // Blur radius
                offsetY = 2.dp // Y offset for the shadow
            )
            .background(color = Color.White) // Background color for the top bar
            .padding(start = 12.dp, top = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onMenuClick() }) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "back",
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(70.dp))

        Text(
            text = "Assign Rider",
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(bottom = 2.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
