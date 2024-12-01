package com.go.givngo

import com.go.givngo.ui.theme.MyComposeApplicationTheme
import com.go.givngo.ui.modifer.drawColoredShadow

import com.go.givngo.donorSide.*
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue

import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.AnimatedVisibility

import androidx.navigation.NavController


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



import com.go.givngo.donorSide.assignRider

class MyNotifications : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
                MyNoti()
            }
        }
    }
}

data class ClaimNotification(
    val documentId: String,
    val title: String,
    val body: String,
    val deliverymethod: String,
    val thumbnail: String?,
    val recipientEmail: String,
    val timestamp: Long
)

data class deliveryReuqest(
    val title: String,
    val body: String,
    val thumbnail: String?,
    val timestamp: Long
)



@Composable
fun MyNoti() {

    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = context as MyNotifications
    val notifications = remember { mutableStateListOf<ClaimNotification>() }
    val notificationsRider = remember { mutableStateListOf<deliveryReuqest>() }
    val firestore = FirebaseFirestore.getInstance()
    val statusUser = SharedPreferences.getStatusType(context) ?: "Developer"
    
    // Fetch notifications from Firestore
    LaunchedEffect(Unit) {
        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
        val recipientEmail = SharedPreferences.getEmail(context) ?: "Developer"

        when (statusUser) {
            "Donor" -> {
                firestore.collection("GivnGoAccounts")
                    .document(userAccountType) // or "VerifiedAccounts"
                    .collection("Donor")
                    .document(recipientEmail)
                    .collection("MyNotifications")
                    .document("ClaimedDonations")
                    .collection("Notification")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { result ->
                        val fetchedNotifications = result.mapNotNull { doc ->
                            val data = doc.data
                            val documentId = data["document_id"] as? String ?: "No Document"
                            val thumbnail = data["thumbnail"] as? String
                            val title = data["title"] as? String ?: "No Title"
                            val body = data["body"] as? String ?: "No Body"
                            val deliveryMethod = data["Delivery_method"] as? String ?: "No delivery method"
                            val timestamp = data["timestamp"] as? Long ?: System.currentTimeMillis()
                            val recEmail = data["recipient_email"] as? String ?: "No email"
                            ClaimNotification(
                                documentId = documentId,
                                title = title,
                                body = body,
                                deliverymethod = deliveryMethod,
                                thumbnail = thumbnail,
                                recipientEmail = recEmail,
                                timestamp = timestamp
                            )
                        }
                        notifications.addAll(fetchedNotifications)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Error fetching notifications: ", exception)
                    }
            }

            "Rider" -> {
                firestore.collection("GivnGoAccounts")
                    .document(userAccountType) // or "VerifiedAccounts"
                    .collection("Rider")
                    .document(recipientEmail)
                    .collection("MyNotifications")
                    .document("DeliveryRequest")
                    .collection("Notification")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { result ->
                        val fetchedNotifications = result.mapNotNull { doc ->
                            val data = doc.data
                            val thumbnail = data["profileImage"] as? String
                            val title = data["notification_title"] as? String ?: "No Title"
                            val body = data["donationDescription"] as? String ?: "No Body"
                            val timestamp = data["timestamp"] as? Long ?: System.currentTimeMillis()

                            deliveryReuqest(
                                title = title,
                                body = body,
                                thumbnail = thumbnail,
                                timestamp = timestamp
                            )
                        }
                        notificationsRider.addAll(fetchedNotifications)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Error fetching notifications: ", exception)
                    }
            }
        }
    }

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
                TopBarMyNotifications(onMenuClick = { activity.finish() }, isScrolled = scrollState.value > 0)
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    when (statusUser) {
                        "Donor" -> {
                            if (notifications.isNotEmpty()) {
                                Text(
                                    text = "New Donation Claims!",
                                    fontSize = 18.sp,
                                    color = Color(0xFF8070F6),
                                    modifier = Modifier
                                        .padding(start = 25.dp)
                                        .fillMaxWidth(),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Start
                                )

                                LazyVerticalGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    columns = GridCells.Fixed(1)
                                ) {
                                    items(notifications) { notification ->
                                        NotificationItem(notification = notification)
                                    }
                                }
                            } else {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(text = "No notifications available")
                                }
                            }
                        }

                        "Rider" -> {
                            if (notificationsRider.isNotEmpty()) {
                                LazyVerticalGrid(
                                    modifier = Modifier.fillMaxSize(),
                                    columns = GridCells.Fixed(1)
                                ) {
                                    items(notificationsRider) { notification ->
                                        NotificationItemTwo(notification = notification)
                                    }
                                }
                            } else {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(text = "No notifications available")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LottieAnimationFromUrlNotification(url: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url(url))
    LottieAnimation(
        composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .size(130.dp) // Adjust the size as needed
    )
}

@Composable
fun NotificationItemTwo(notification: deliveryReuqest) {
val context = LocalContext.current
val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
    onResult = { result -> 
        // Handle the result from the launched activity
    }
)
    // Use AsyncImage for loading images asynchronously
    val imageUrl = notification.thumbnail?.let { Uri.parse(it).toString() }


    Row( 
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp,bottom = 8.dp, top = 8.dp)
    ) {
    
            // AsyncImage will handle loading the image
        imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Thumbnail",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(40.dp)),
                    contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_boxwithheart), // Placeholder image
                error = painterResource(id = R.drawable.ic_donationpackage) // Error image in case of failure
            )
        }
        
        
        Spacer(modifier = Modifier.width(10.dp))
     
        Text(
            text = notification.title,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8070F6),
            fontSize = 18.sp
        )
        
        }
        
  }
        

@Composable
fun NotificationItem(notification: ClaimNotification) {
val context = LocalContext.current
val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
    onResult = { result -> 
        // Handle the result from the launched activity
    }
)
    // Use AsyncImage for loading images asynchronously
    val imageUrl = notification.thumbnail?.let { Uri.parse(it).toString() }


    Row( 
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp,bottom = 8.dp, top = 8.dp)
    ) {
    
            // AsyncImage will handle loading the image
        imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Thumbnail",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(40.dp)),
                    contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_boxwithheart), // Placeholder image
                error = painterResource(id = R.drawable.ic_donationpackage) // Error image in case of failure
            )
        }
        
        
        Spacer(modifier = Modifier.width(10.dp))
       
        Column {
    
     
        Text(
            text = notification.title,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8070F6),
            fontSize = 16.sp
        )
        
        
        
    Row( 
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp,top = 6.dp)
    ) {
    
    if(notification.deliverymethod == "Pick-Up"){
    
        Text(
            text = "Recipient Coming to pickup the package at your address",
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            fontSize = 11.sp
        )
        
        }else if (notification.deliverymethod == "Self Delivery"){
        Box(
    modifier = Modifier
        .height(30.dp)
        .background(color = Color(0xFF8070F6), shape = RoundedCornerShape(25.dp))
        .clickable { 
        
        }
        .wrapContentWidth(),  
    contentAlignment = Alignment.Center // Centers the content inside the Box
) {
    Text(
        text = "Self Delivery",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 6.dp, end = 10.dp, start = 10.dp, bottom = 6.dp),
        color = Color.White,
        fontSize = 11.sp
    )
}

        }else if (notification.deliverymethod == "GivnGo Rider"){
        Spacer(modifier = Modifier.width(8.dp))

Box(
    modifier = Modifier
        .height(30.dp)
        .padding(start = 6.dp)
        .background(color = Color(0xFF8070F6), shape = RoundedCornerShape(25.dp))
        .clickable {   
        val intent = Intent(context, assignRider::class.java).apply {
                            putExtra("donationTitle", notification.documentId)
                            putExtra("donationDescription", notification.body)
                            putExtra("donThumb", notification.thumbnail)
                            putExtra("recEmail", notification.recipientEmail)
                        }
                        context.startActivity(intent)
            }
        .wrapContentWidth(),
    contentAlignment = Alignment.Center // Centers the content inside the Box
) {
    Text(
        text = "Assign Rider",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 6.dp, end = 12.dp, start = 12.dp, bottom = 6.dp),
        color = Color.White,
        fontSize = 11.sp
    )
}
        }
        
    


            
    }
    
    
    }
    
    

    }
}


@Composable
fun TopBarMyNotifications(onMenuClick: () -> Unit, isScrolled: Boolean) {
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

        Spacer(modifier = Modifier.width(65.dp))

        Text(
            text = "My Notifications",
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(bottom = 2.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
