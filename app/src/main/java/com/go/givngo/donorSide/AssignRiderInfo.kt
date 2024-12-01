package com.go.givngo.donorSide
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
import android.content.Context

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
import android.net.Uri
import coil.compose.rememberImagePainter

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import android.util.Log

import com.go.givngo.SharedPreferences

import androidx.activity.compose.BackHandler

import com.google.firebase.storage.FirebaseStorage

import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.widget.Toast

import java.util.UUID

import com.google.firebase.messaging.FirebaseMessaging
import kotlin.collections.hashMapOf

import java.text.SimpleDateFormat
import java.util.*

class AssignRiderInfo : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get data from the Intent
        val volunteerEmail = intent.getStringExtra("volunteerEmail") ?: "No Email Provided"
        val volunteerName = intent.getStringExtra("volunteerName") ?: "No Name Provided"
        val courriertype = intent.getStringExtra("courriertype") ?: "No Vehicle Type"
        val imageProfile = intent.getStringExtra("imageProfile") ?: ""

val donationTitle = intent.getStringExtra("donTitle") ?: ""
val recipientEmail = intent.getStringExtra("recEmail") ?: ""
val donDesc = intent.getStringExtra("donDesc") ?: ""
        val donThumb = intent.getStringExtra("donThumb") ?: ""
        
        setContent {
            MyComposeApplicationTheme {
                RiderInfoAssign(volunteerName,volunteerEmail,courriertype,imageProfile,donationTitle,donDesc,donThumb,recipientEmail)
            }
        }
    }
}


@Composable
fun RiderInfoAssign(
    volunteerName: String,
    volunteerEmail: String,
    courriertype: String,
    imageProfile: String,
    donTitles: String,
    donDescs: String,
    donThumb: String,
    recEmail: String
) {
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as AssignRiderInfo)

    var message by remember { mutableStateOf("") }

    // Reactive state for Firestore data
    var vehicleWeight by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var riderBio by remember { mutableStateOf("") }
    
    var riderFirstName by remember { mutableStateOf("") }
    var riderLastName by remember { mutableStateOf("") }
    
    
    var donationTitle by remember { mutableStateOf("") }
    var donationDesc by remember { mutableStateOf("") }
    var donorProfileImage = SharedPreferences.getProfileImageUri(context) ?: "No Name"
    var donationCategory by remember { mutableStateOf("") }
    var donorName = SharedPreferences.getFirstName(context) ?: "No Name"
    var donorEmail = SharedPreferences.getEmail(context) ?: "No Name"
    var recAddress by remember { mutableStateOf("") }
    var donationQuantity by remember { mutableStateOf("") }
    var fcmToken by remember { mutableStateOf("") }
    var fcmTokenDonor by remember { mutableStateOf("") }
    var donorAddress = SharedPreferences.getAdd(context) ?: "No Address"

    val db = FirebaseFirestore.getInstance()
    val accountsRef = db.collection("GivnGoAccounts")
    val postRef = db.collection("GivnGoPublicPost")
    var accountFound = false
    var requestData = hashMapOf<String, Any?>()
    var riderChatConvo = hashMapOf<String, Any?>()
    var donorChatConvo = hashMapOf<String, Any?>()

    // Function to handle Firestore updates
    fun listenToAccountChanges(collectionPath: String, email: String) {
        accountsRef.document(collectionPath).collection("Rider")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    contactNumber = documentSnapshot.getString("fullContact") ?: ""
                    riderBio = documentSnapshot.getString("bio") ?: ""
                    vehicleWeight = documentSnapshot.getString("vehicle_weight") ?: ""
                    fcmToken = documentSnapshot.getString("fcmToken") ?: ""
                    riderFirstName = documentSnapshot.getString("Rider First Name") ?: ""
                    riderLastName = documentSnapshot.getString("Rider Last Name") ?: ""
                    
                    postRef.document("DonationPosts").collection("Posts")
                    .document(donTitles) .addSnapshotListener { documentSnapshot, error ->
                    if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                
                donationTitle =documentSnapshot.getString("donation_post_title") ?: ""
                donationDesc =documentSnapshot.getString("donation_post_description") ?: ""
                donationCategory =documentSnapshot.getString("donation_category") ?: ""
                donationQuantity = documentSnapshot.getString("donation_quantity") ?: ""
                    recAddress= documentSnapshot.getString("claimed by - ") ?: ""
                accountFound = true 
                
                }
                    
                    }
                    
                    // Mark account as found
                }
            }
    }

 
    fun listenToAccountChangesGetRider(collectionPath: String, email: String) {
    
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                                return@addOnCompleteListener
                            }
                             fcmTokenDonor = task.result
                            
                            accountsRef.document(collectionPath).collection("Rider")
            .document(email)
            .update("fcmToken" ,fcmTokenDonor )
            .addOnSuccessListener { 
                
                }
                
                            }
                            
        
    }        
    
    // Listen to Firestore updates
    LaunchedEffect(volunteerEmail) {
        listenToAccountChanges("BasicAccounts", volunteerEmail)
        listenToAccountChangesGetRider("BasicAccounts", volunteerEmail)
        
        if (!accountFound) {
            listenToAccountChanges("VerifiedAccounts", volunteerEmail)
            listenToAccountChangesGetRider("VerifiedAccounts", volunteerEmail)
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
                TopBarSettings(onMenuClick = { activity.finish() }, isScrolled = scrollState.value > 0)
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
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    RiderInfo(
                        riderEmail = volunteerEmail,
                        riderName = volunteerName,
                        riderVehicle = courriertype,
                        riderBio = riderBio,
                        imageUri = imageProfile
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    riderInformation(
                        information = "Contact Number: $contactNumber",
                        resId = R.drawable.ic_phone
                    )

                    riderInformation(
                        information = "Vehicle Weight: $vehicleWeight",
                        resId = R.drawable.ic_weight
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier
                        .height(12.dp)
                        .fillMaxWidth()
                        .background(Color(0xFFEAE8FF)))

                    Spacer(modifier = Modifier.height(12.dp))

                    displaySchedulesDonationModel(
                        userClaimedDonation = donationTitle,
                        description = donationCategory,
                        imageUri = donThumb
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    editTextMultipleLineRider(
                        "Add Message",
                        message,
                        { message = it }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(modifier = Modifier
                        .height(3.dp)
                        .fillMaxWidth()
                        .background(Color(0xFFEAE8FF)))

                    DeliveryInformations(
                        information = "Address to pickup: $donorAddress",
                        resId = R.drawable.ic_deliveries
                    )

                    Box(modifier = Modifier
                        .height(3.dp)
                        .fillMaxWidth()
                        .background(Color(0xFFEAE8FF)))

                    DeliveryInformations(
                        information = "Quantity: $donationQuantity ",
                        resId = R.drawable.ic_donationpackage
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { 
                            
    val uuid = UUID.randomUUID().toString() // Generate a unique document name if donorName is not available
                            var status = "Request Status: Pending"
                             
                            fun formatTimestampDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy, hh:mm a z", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date(timestamp))
    }

 val timestamps = System.currentTimeMillis()
                            
                            requestData = hashMapOf(
                            "userClaimedDonation" to donationTitle,
                            "donationDescription" to donationDesc,
                            "donationQuantity" to donationQuantity,
                            "donorAddress" to donorAddress,
                            "donorEmail" to donorEmail,
                                                        "recipientEmail" to recEmail,
                                                        "donationThumbnail" to donThumb,
                                                          "packageStatus" to status,
                                                              "packaged_time_packed" to formatTimestampDate(timestamps),
    "donation_schedule_status" to "Donation Packed and ready by the donor"
                            )
                           
                            
                           val requestDataRider: HashMap<String, Any?> = hashMapOf(
    "userClaimedDonation" to donationTitle,
    "profileImage" to donorProfileImage,
    "notification_title" to "New Delivery Request from $donorName",
    "donationDescription" to donationDesc,
    "donationQuantity" to donationQuantity,
    "donorAddress" to donorAddress,
    "donorEmail" to donorEmail,
    "recipientEmail" to recEmail,
    "donationThumbnail" to donThumb,
    "packageStatus" to status,
    "timestamp" to System.currentTimeMillis()
)

                            
                            riderChatConvo = hashMapOf(
                            "rider_email" to volunteerEmail,
                            "firstName" to riderFirstName,
                            "lastName" to riderLastName,
                            "profileImage" to imageProfile,
                            "fcmToken" to fcmToken
                            )
                            
                            donorChatConvo  = hashMapOf(
                            "donor_email" to donorEmail,
                            "Bussiness/Organization_Name" to donorName,
                            "profileImage" to donorProfileImage,
                            "fcmToken" to fcmTokenDonor
                            )
                            
                            fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy, hh:mm a z", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date(timestamp))
    }

 val timestamp = System.currentTimeMillis()
 
                            val chatMessage = ChatMessage(
            sender_email = donorEmail,
            message = "",
            timestamp = formatTimestamp(timestamp),
            chat_type = "Request",
            request_image = donThumb,
            donation_name = donationTitle
        )

val chatCollection = db.collection("GivnGoChat")
        .document(donorEmail)
        .collection("Chat_To_Rider")
        .document(uuid)
        .collection("Chats")
        
// Adding delivery request
db.collection("GivnGoRiders")
    .document("Rider")
    .collection("AvailableDeliveries")
    .document(volunteerEmail)
    .collection("RequestStatus")
    .add(requestData)
    .addOnSuccessListener {
    
    saveNotificationToFirestore("BasicAccounts",db,volunteerEmail,requestDataRider)
    
    sendPushNotification(
                                                fcmToken,
                                                "New delivery request from GivnGo",
                                                "You have a new delivery request from $donorName",
                                                donorEmail
                                            )
    
        // Check if donor chat to rider already exists
        db.collection("GivnGoChat")
    .document(donorEmail)
    .collection("Chat_To_Rider")
    .whereEqualTo("rider_email", volunteerEmail)
    .get()
    .addOnSuccessListener { querySnapshot ->
        if (querySnapshot.isEmpty) {
            // If no matching document exists, add the riderChatConvo
            db.collection("GivnGoChat")
                .document(donorEmail)
                .collection("Chat_To_Rider")
                .document(uuid)
                .set(riderChatConvo)
                .addOnSuccessListener {
                
                
        chatCollection.add(chatMessage)
            .addOnSuccessListener {
                Log.d("Firestore", "Message sent successfully")
            }
            .addOnFailureListener { error ->
                Log.e("Firestore", "Error sending message: ${error.localizedMessage}")
            }
            
                    Log.d("Firestore", "Rider chat conversation successfully added.")
                    try {
                        // Proceed to check for Chat_To_Donor
                        db.collection("GivnGoChat")
                            .document(volunteerEmail)
                            .collection("Chat_To_Donor")
                            .whereEqualTo("donor_email", donorEmail)
                            .get()
                            .addOnSuccessListener { donorQuerySnapshot ->
                                if (donorQuerySnapshot.isEmpty) {
                                    // Add donorChatConvo since no matching donor_email exists
                                    db.collection("GivnGoChat")
                                        .document(volunteerEmail)
                                        .collection("Chat_To_Donor")
                                        .document(uuid)
                                        .set(donorChatConvo)
                                        .addOnSuccessListener {
                                            
                                            Log.d("Firestore", "Donor chat conversation successfully added.")
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("Firestore", "Error adding donor chat conversation: ", exception)
                                        }
                                } else {
                                    Log.d("Firestore", "Donor chat conversation with this rider already exists.")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("Firestore", "Error checking existing donor chats: ", exception)
                            }
                    } catch (exception: Exception) {
                        Log.e("Firestore", "Unexpected error: ", exception)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error adding rider chat conversation: ", exception)
                }
        } else {
            Log.d("Firestore", "Rider chat conversation with this donor already exists.")
            
            chatCollection.add(chatMessage)
            .addOnSuccessListener {
                Log.d("Firestore", "Message sent successfully")
            }
            .addOnFailureListener { error ->
                Log.e("Firestore", "Error sending message: ${error.localizedMessage}")
            }
            
            
        }
    }
    .addOnFailureListener { exception ->
        Log.e("Firestore", "Error checking existing chats in Chat_To_Rider: ", exception)
    }

    }
    .addOnFailureListener { exception ->
        Log.e("Firestore", "Error adding delivery request: ", exception)
    }


                            
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF8070F6)
                            ),
                            modifier = Modifier
                                .height(60.dp)
                                .padding(8.dp)
                                .wrapContentWidth(),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text(
                                text = "Send Request",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(36.dp))
                }
            }
        }
    }
}

data class ChatMessage(
var message: String,
var timestamp: String,
var sender_email: String,
var chat_type: String,
var request_image: String,
var donation_name: String
) 

private fun saveNotificationToFirestore(path: String, firestore: FirebaseFirestore, riderEmail: String, data: HashMap<String, Any?> 
) {
    
    firestore
    .collection("GivnGoAccounts")
        .document(path) // Check BasicAccounts or VerifiedAccounts
        .collection("Rider")
        .document(riderEmail)
        .collection("MyNotifications")
        .document("DeliveryRequest")
        .collection("Notification")
        .add(data)
        .addOnSuccessListener {
            Log.d("Firestore", "Notification added successfully")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error adding notification", e)
        }
}



@Composable
fun displaySchedulesDonationModel(
    userClaimedDonation: String,
    description: String,
    imageUri: String
) {

val thumbnail =  Uri.parse(imageUri).toString() 

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 8.dp, end = 15.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .padding(start = 12.dp, bottom = 12.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(20.dp))
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
                        fontSize = 14.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "Category Type: $description",
                        fontSize = 12.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        Text(
            text = "Message to the rider:",
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 4.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        
    }
}

@Composable
fun editTextMultipleLineRider(
    placeHolderTitle: String,
    descriptionInfo: String,
    descriptionDonation: (String) -> Unit
) {
    var textState by remember { mutableStateOf(descriptionInfo) }

Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 8.dp)
            ){
            
            Box(
        modifier = Modifier
            .padding(8.dp)
            .heightIn(min = 90.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xFFFBF8FF),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.TopStart
    ) {
        if (textState.isEmpty()) {
            Text(
                text = placeHolderTitle,
                color = Color.Black.copy(alpha = 0.90f),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
        }

        BasicTextField(
            value = textState,
            onValueChange = { newValue ->
                textState = newValue  
                descriptionDonation(newValue) 
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .wrapContentHeight(),
            textStyle = TextStyle(
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            ),
            singleLine = false // Allow multiple lines
        )
    }
    
            }
    
}

@Composable
fun RiderInfo(
    riderEmail: String,
    riderName: String,
    riderVehicle: String,
    riderBio: String,
    imageUri: String
) {
val thumbnail =  Uri.parse(imageUri).toString() 
Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding( bottom = 4.dp)
    ) {
    
    
    Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = 26.dp, top = 16.dp, end = 16.dp, bottom= 16.dp)
            ) {
                AsyncImage(
                    model = thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .clip(RoundedCornerShape(18.dp))
                        .background(color = Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = "Rider name: $riderName",
                        fontSize = 14.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "Vehicle Type: $riderVehicle",
                        fontSize = 11.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    
                }
            }
            
            Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(top = 4.dp, start = 26.dp)
                ) {
                    Text(
                        text = "About Me:",
                        fontSize = 14.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "$riderBio",
                        fontSize = 13.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    
                }
    }
}

@Composable
fun riderInformation(
    information: String, 
    resId: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 26.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
            Image(
                painter = painterResource(id = resId),
                contentDescription = "back",
                modifier = Modifier.size(26.dp)
            )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "$information",
            color = Color.Black,
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal
        )
    }
}



@Composable
fun DeliveryInformations(
    information: String, 
    resId: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 26.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
            Image(
                painter = painterResource(id = resId),
                contentDescription = "back",
                modifier = Modifier.size(28.dp)
            )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "$information",
            color = Color(0xFF8070F6),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun TopBarSettings(onMenuClick: () -> Unit, isScrolled: Boolean) {
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

    }
}
