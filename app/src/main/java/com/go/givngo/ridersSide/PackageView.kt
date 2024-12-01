package com.go.givngo.ridersSide

import com.go.givngo.BrowseDonationsPackageView
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
import android.net.Uri
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

import java.text.SimpleDateFormat
import java.util.*
import com.go.givngo.recipientSide.trackingDelivery

import androidx.compose.foundation.lazy.*

class PackageView : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

val documentId = intent.getStringExtra("documentId") ?: "Unknown Ticket"

val packageViewType = intent.getStringExtra("packageViewType") ?: "Request"
        val donationPackageRequest = intent.getSerializableExtra("donationPackageRequest") as? DonationPackageRequest
        ?: DonationPackageRequest(
            documentId = "Ticket Unknown",
            userClaimedDonation = "Unknown",
            donationDescription = "No description available",
            donationQuantity = "0",
            donorAddress = "Unknown address",
            donorEmail = "Unknown email",
            recipientEmail = "Unknown email",
            donationThumbnail = "",
            packageStatus = "",
              recipient_document_id_package = "",
     packaged_time_packed = "",
     donation_schedule_status = ""
        )
        val donationPackageView = intent.getSerializableExtra("donationPackageRequest") as? DonationSchedule
        ?: DonationSchedule(
            documentId = "",
                    userClaimedDonation = "",
                    donationDescription = "",
                    packageStatus = "",
                    donorEmail = "",
                    donationThumbnail = "",
                    donationQuantity = "",
                    donorAddress = "",
                    timeRecieved = ""
        )
        
        val donationIdMyRoute = intent.getStringExtra("documentidmyroutes") ?: ""
        val donTitle = intent.getStringExtra("title") ?: ""
        val donDesc = intent.getStringExtra("description") ?: ""
        val donThumbnail = intent.getStringExtra("thumbnail") ?: ""
        val packageStatus = intent.getStringExtra("packageStatus") ?: ""
        val donQuantity = intent.getStringExtra("quantity") ?: ""
        val donorAddress = intent.getStringExtra("address_donor") ?: ""
        val donorEmail = intent.getStringExtra("donorEmail") ?: ""
        val emailRider = intent.getStringExtra("email_rider") ?: ""
        val documentIdFromRider = intent.getStringExtra("documentIdFromRider") ?: ""
        val ticketTrackingId = intent.getStringExtra("ticketTrackingId") ?: ""

        setContent {
            MyComposeApplicationTheme {
            
                MyAppPackageView(packageViewType = packageViewType,donationPackageRequest, donationPackageView, documentId,
                donationIdMyRoute, donTitle,donDesc,
                donThumbnail,packageStatus,donQuantity,
                donorAddress,donorEmail,emailRider,
                documentIdFromRider,ticketTrackingId)
            }
        }
    }
}

@Composable
fun MyAppPackageView(packageViewType: String, donationPackageRequest: DonationPackageRequest, donationPackageView: DonationSchedule, documentId: String, 
myrouteOne: String, myrouteTwo: String, myrouteThree: String,
myrouteFour: String, myrouteFive: String, myrouteSix: String, 
myrouteSeven: String, myrouteEight: String, myrouteNine: String, 
myrouteTen: String, myrouteEleven: String)

{

Log.d("PackageView", "PackageViewType: $packageViewType")

    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as PackageView)

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
                TopBarBack(onMenuClick = { activity.finish() }, isScrolled = scrollState.value > 0)
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
            
            //RIDER MY ROUTES
            val documentIdRecipientMyRoutes = myrouteOne
            val donTitleMyRoutes = myrouteTwo
val donDescMyRoutes = myrouteThree
val donThumbMyRoutes = myrouteFour
val packageRequestMyRoutes = myrouteFive
val donQuantityMyRoutes = myrouteSix
val donDonorAddressMyRoutes = myrouteSeven
val donorEmailMyRoutes = myrouteEight
val recipientEmailMyRoutes = myrouteNine
val ticketIdMyRoutes = myrouteTen

              //REQUEST RIDER
                val address = donationPackageRequest.donorAddress
                val recipientEmail = donationPackageRequest.recipientEmail
                val quantity = donationPackageRequest.donationQuantity
                val donTitle =donationPackageRequest.userClaimedDonation
                val donDesc = donationPackageRequest.donationDescription
                val donThumb =donationPackageRequest.donationThumbnail
                val documentIdFromRec = donationPackageRequest.recipient_document_id_package
                val donTimePackedByDonor = donationPackageRequest.packaged_time_packed
                val donScheduleStatus = donationPackageRequest.donation_schedule_status
                
                //RECIPIENT SCHEDULES
                val donTitleTwo = donationPackageView.userClaimedDonation
                val donDescTwo = donationPackageView.donationDescription
                val donQuantity = donationPackageView.donationQuantity
                val donthumbnail = donationPackageView.donationThumbnail
                val donorEmail = donationPackageView.donorEmail
                val donThumbTwo = donationPackageView.donationThumbnail
                val emailRider = donationPackageView.emailRider
                val documentIdRec = donationPackageView.documentId
                val documentIdFromRider = donationPackageView.documentIdFromRider
                val timeRecieved = donationPackageView.timeRecieved
                var addressRecipient = SharedPreferences.getAdd(context) ?: "No email"
                
                var donorName by remember { mutableStateOf("") }
                val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                
                val accountStatus = SharedPreferences.getStatusType(context) ?: ""

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
                    donorName = documentSnapshot.getString("OrganizationName") ?: ""
                    accountFound= true
                    }
            }
    }
    
    fun listenToAccountChangesRecipient(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Donor")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    donorName = documentSnapshot.getString("OrganizationName") ?: ""
                    accountFound= true
                    }
            }
    }
    
    if (packageViewType == "ConfirmPickupRiderMyRoutes"){
    
    LaunchedEffect(Unit) {
        listenToAccountChanges("BasicAccounts", donorEmailMyRoutes)
        
        if(accountStatus == "Recipient"){
        listenToAccountChangesRecipient("BasicAccounts", donorEmail)
        
        }
        
        if (!accountFound) {
            listenToAccountChanges("VerifiedAccounts", donorEmailMyRoutes)
            
            if(accountStatus == "Recipient"){
            listenToAccountChangesRecipient("VerifiedAccounts", donorEmail)
            }
            
        }
    }
    
    }
     
    LaunchedEffect(Unit) {
        listenToAccountChanges("BasicAccounts", donationPackageRequest.donorEmail)
        
        
        if (!accountFound) {
            listenToAccountChanges("VerifiedAccounts", donationPackageRequest.donorEmail)
            
        }
    }
    

                when (packageViewType) {
            /* rider */     "Request" -> requestView(address, quantity,donTitle,donDesc,donorName,donThumb, donationPackageRequest.donorEmail,recipientEmail,documentId,donTimePackedByDonor,donScheduleStatus)
                    "ConfirmPickupRecipient" -> confirmPickupRecipient(address, quantity,donTitle,donDesc,donorName,donThumb, donationPackageRequest.donorEmail,recipientEmail,documentId)
                   //   /* rider */     "ConfirmPickupRider" -> confirmPickupRider(address, quantity, donTitle,donDesc,donorName,donThumb, donationPackageRequest.donorEmail,recipientEmail,documentId,documentIdRec)
                  "ConfirmPickupRiderMyRoutes" -> confirmPickupRider(donDonorAddressMyRoutes, donQuantityMyRoutes, donTitleMyRoutes,donDescMyRoutes,donorName,donThumbMyRoutes, donorEmailMyRoutes,recipientEmailMyRoutes,documentId,documentIdRecipientMyRoutes)
                  "DropOffMyRoutes" -> confirmDropOff(donDonorAddressMyRoutes, donQuantityMyRoutes,donTitleMyRoutes,donDescMyRoutes,donorName,donThumbMyRoutes, donorEmailMyRoutes,recipientEmailMyRoutes,documentId, documentIdRecipientMyRoutes)
                  "RecievedMyRoutes" -> packageRecieved(donDonorAddressMyRoutes, donQuantityMyRoutes,donTitleMyRoutes,donDescMyRoutes,donorName,donThumbMyRoutes, recipientEmailMyRoutes,documentId,donationPackageView.documentId)
                  
            /* rider */        "DropOff" -> confirmDropOff(address, quantity,donTitle,donDesc,donorName,donThumb, donationPackageRequest.donorEmail,recipientEmail,documentId, documentIdFromRec)
                    "InTransit" -> packageInTransit(addressRecipient, donQuantity, donTitleTwo, donDescTwo, donorName, donthumbnail, donationPackageView.emailRider)
            /* rider */     "Delivered" -> packageRecievedRecipient(address, quantity,donTitleTwo,donDescTwo,donorName,donThumbTwo, donationPackageView.emailRider,documentId, donationPackageView.documentId,timeRecieved)
                   "Confirmation" -> requestView(address, quantity,donTitle,donDesc,donorName,donThumb,donationPackageRequest.donorEmail,recipientEmail, documentId,donTimePackedByDonor,donScheduleStatus)
                    else -> requestView(address, quantity,donTitle,donDesc,donorName,donThumb,donationPackageRequest.donorEmail,recipientEmail, documentId,donTimePackedByDonor,donScheduleStatus)
                }
            }
        }
    }
}

@Composable
fun packageInTransit(Address: String, Quantity: String, donTitle: String, donDesc: String, donOwner: String, donThumb: String, emailRider: String){

val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                var riderFN by remember { mutableStateOf("") }
                var riderLN by remember { mutableStateOf("") }
                var riderPic by remember { mutableStateOf("") }
               
                var riderCourrierType by remember { mutableStateOf("") }

fun listenToAccountChangesDonorGet(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Rider")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    riderFN = documentSnapshot.getString("Rider First Name") ?: ""
                    riderLN = documentSnapshot.getString("Rider Last Name") ?: ""
                    riderPic = documentSnapshot.getString("profileImage") ?: ""
                    riderCourrierType = documentSnapshot.getString("volunteer_courrier") ?: ""
                    accountFound = true
                    }
            }
    }
    
    LaunchedEffect(Unit) {
        listenToAccountChangesDonorGet("BasicAccounts", emailRider)

        if (!accountFound) {
            listenToAccountChangesDonorGet("VerifiedAccounts", emailRider)
        }
    }
    
Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
    
    topHeadline("In-transit!", "Your donation package is on its way, rider wil notify you if your package has been drop off in your designated address", R.drawable.intransit)
    
    RiderInfo(
    "Delivery in-transit","Clinton","Ihegoro","Motor",R.drawable.dropoff_pickup, riderPic,
    onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
    DeliveryInformations(
    information = "To know more about your delivery details, press here...",
    resId = R.drawable.ic_donationpackage,  
    onRowClick = { 

    }
    )
    
    Box(modifier = Modifier.height(16.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
    
    displayPackageInformationTwo(
    "$donTitle","$donDesc", "$donOwner","On Transit", donThumb,onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    

    }
}
@Composable
fun packageRecievedRecipient(
    Address: String,
    Quantity: String,
    donTitle: String,
    donDesc: String,
    donOwner: String,
    donThumb: String,
    emailRider: String,
    documentIdFromRider: String,
    docIdRec: String,
    timeRecieved: String
) {
    val context = LocalContext.current
    val emailRecipient = SharedPreferences.getEmail(context) ?: "No Email"

    val db = FirebaseFirestore.getInstance()
    val accountsRef = db.collection("GivnGoAccounts")
    var accountFound = false
    var riderFN by remember { mutableStateOf("") }
    var riderLN by remember { mutableStateOf("") }
    var riderPic by remember { mutableStateOf("") }
    var riderCourrierType by remember { mutableStateOf("") }
    var documentIdFromRiders by remember { mutableStateOf("") }

    fun listenToAccountChangesDonorGet(collectionPath: String, email: String) {
        accountsRef.document(collectionPath).collection("Rider")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    riderFN = documentSnapshot.getString("Rider First Name") ?: ""
                    riderLN = documentSnapshot.getString("Rider Last Name") ?: ""
                    riderPic = documentSnapshot.getString("profileImage") ?: ""
                    riderCourrierType = documentSnapshot.getString("volunteer_courrier") ?: ""
                }
            }
    }

    LaunchedEffect(Unit) {
        listenToAccountChangesDonorGet("BasicAccounts", emailRider)

        if (!accountFound) {
            listenToAccountChangesDonorGet("VerifiedAccounts", emailRider)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color.White)
    ) {
            topHeadlineRecipient(
                "Recieved!",
                "Package Recieved, we do hope you enjoy and support our app and be with us again!",
                R.drawable.recieved
            )

            RiderInfo(
                "Recieved at $timeRecieved", "$riderFN", "$riderLN", "$riderCourrierType", R.drawable.boxchecked, riderPic,
                onRowClick = {}
            )

            Box(
                modifier = Modifier
                    .height(3.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFEAE8FF))
            )

            DeliveryInformationsRecipient(
                information = "To know more about your delivery details, press here...",
                resId = R.drawable.ic_donationpackage,
                onRowClick = {
                    val intent = Intent(context, trackingDelivery::class.java).apply {
                        putExtra("packageViewType", "Tracking")
                        putExtra("emailRider", emailRider)
                        putExtra("recipientEmail", emailRecipient)
                        putExtra("documentId", docIdRec)
                        putExtra("timeRecieved", timeRecieved)
                        putExtra("profileImage", riderPic)
                    }
                    context.startActivity(intent)
                }
            )

        
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFEAE8FF))
            )
        

            displayPackageInformationTwo(
                "$donTitle", "$donDesc", "$donOwner", "Mark Recieved by Rider", donThumb, onRowClick = {}
            )
    }
}


@Composable
fun packageRecieved(Address: String, Quantity: String, donTitle: String, donDesc: String, donOwner: String, donThumb: String, emailRider: String, documentIdFromRider: String, docIdRec: String){
val context = LocalContext.current
var emailRecipient = SharedPreferences.getEmail(context) ?: "No Email"

val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                var riderFN by remember { mutableStateOf("") }
                var riderLN by remember { mutableStateOf("") }
                var riderPic by remember { mutableStateOf("") }
                var riderCourrierType by remember { mutableStateOf("") }
                var documentIdFromRiders by remember { mutableStateOf("") }
                var timeRecieved by remember { mutableStateOf("") }
                
                
                
fun listenToAccountChangesDonorGet(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Rider")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    riderFN = documentSnapshot.getString("Rider First Name") ?: ""
                    riderLN = documentSnapshot.getString("Rider Last Name") ?: ""
                    riderCourrierType = documentSnapshot.getString("volunteer_courrier") ?: ""
                     riderPic = documentSnapshot.getString("profileImage") ?: ""
                      db.collection("GivnGoRiders")
                        .document("Rider")
                        .collection("MyRoutes")
                        .document(email)
                        .collection("Routes_Status")
                        .document(documentIdFromRider)
                                  .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                
            timeRecieved = documentSnapshot.getString("donation_time_mark_as_recieved") ?: ""
                      accountFound = true
            }
            
            }
            
                    }
            }
    }
    
    LaunchedEffect(Unit) {
        listenToAccountChangesDonorGet("BasicAccounts", emailRecipient)

        if (!accountFound) {
            listenToAccountChangesDonorGet("VerifiedAccounts", emailRecipient)
        }
    }
    
    
Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
    
    topHeadline("Complete!","Recipient recieved the package, we do hope you enjoy and support our app and be with us again!", R.drawable.recieved)
    
    RiderInfo(
    "Recieved at $timeRecieved","$riderFN","$riderLN","$riderCourrierType",R.drawable.boxchecked,riderPic,
    onRowClick = { 

    }
)

/* Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
    DeliveryInformations(
    information = "To know more about this delivery history, press here...",
    resId = R.drawable.ic_donationpackage,  
    onRowClick = { 
val intent = Intent(context, trackingDelivery::class.java).apply {
                                 putExtra("packageViewType", "Tracking")
                                 putExtra("emailRider", emailRecipient)
                                 putExtra("recipientEmail", emailRider)
                                 putExtra("documentId", docIdRec)
                                 putExtra("timeRecieved", timeRecieved)
                                 putExtra("profileImage", riderPic)
                             }
                             context.startActivity(intent) 
    }
    )
    
    */
    
    Box(modifier = Modifier.height(16.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
    
    displayPackageInformationTwo(
    "$donTitle","$donDesc", "$donOwner","Mark Recieved by Rider", donThumb,onRowClick = { 

    }
)

    }
}

@Composable
fun confirmDropOff(Address: String, Quantity: String, donationTitle: String, donationDesc: String, donorName: String,donationThumb: String, email: String, recipientEmail: String, documentId: String, documentIdRec: String){
val context = LocalContext.current
var emailRider = SharedPreferences.getEmail(context) ?: "No email"
 val activity = context as PackageView
 
 fun generateRandomTicketId(): String {
    // You can customize this to generate a more complex ID if needed
    return "TICKET-${Random.nextInt(100000, 999999)}"
}


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
    
    val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                var accountFoundTwo = false
                var accountFoundFive = false
                var recipientAddress by remember { mutableStateOf("") }
                var orgName by remember { mutableStateOf("") }
                val timestamp = System.currentTimeMillis()
                
                fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy, hh:mm a z", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date(timestamp))
    }
    
    fun formatTimestampOnlyDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

var timerecievedrecipient = formatTimestampOnlyDate(timestamp)

                fun listenToAccountChangesRecipientNotifications(collectionPath: String, email: String) {
                var notificationData = hashMapOf(
                "title" to "Delivery from rider",
                "description" to "Rider has drop-off your package at your location",
                "time" to formatTimestamp(timestamp)
                )
                    
                accountsRef.document(collectionPath).collection("Recipient")
            .document(email)
            .collection("MyNotifications")
            .document("DeliveryRequest") 
            .collection("Notification")
            .add(notificationData)
            .addOnSuccessListener { doc ->
            accountFoundFive = true
            activity.finish()
            }
    }
                
                fun listenToAccountChangesDonorUpdate(collectionPath: String, email: String) {
                

                     
                    val historyTracksTwo = hashMapOf(
                   "donation_schedule_status" to "Delivered",
                   "history_title" to  "Recieved!",
                    "history_description" to "Rider marked as recieved as it is confirmed that recipient has collected the package",
  "timestamp" to formatTimestamp(timestamp),
                    "query_timestamp" to timestamp
                    )
                    
                accountsRef.document(collectionPath).collection("Recipient")
            .document(email)
            .collection("MySchedules")
            .document("Donations") 
            .collection("Schedules")
            .document(documentIdRec)
            .update(
            "donation_schedule_status", "Delivered",
            "donation_time_marked_as_recieved", timerecievedrecipient
            )
            .addOnSuccessListener { doc ->
            accountFoundTwo = true
            
            accountsRef.document(collectionPath).collection("Recipient")
            .document(email)
            .collection("MySchedules")
            .document("Donations") 
            .collection("Tracking")
            .document(documentIdRec)
            .collection("History")
            .add(historyTracksTwo)
            .addOnSuccessListener { 
            
            
              activity.finish() 
              
            }
            }
    }
    
    
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
                    recipientAddress = documentSnapshot.getString("address") ?: ""
                    

                    }
            }
    }
    
    fun listenToAccountChangesDonorGetTwo(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Donor")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    orgName = documentSnapshot.getString("OrganizationName") ?: ""
                    accountFound= true
                    }
            }
    }
    
    LaunchedEffect(Unit) {
        listenToAccountChangesDonorGet("BasicAccounts", recipientEmail)
        listenToAccountChangesDonorGetTwo("BasicAccounts", email)
        if (!accountFound) {
            listenToAccountChangesDonorGet("VerifiedAccounts", recipientEmail)
            listenToAccountChangesDonorGetTwo("BasicAccounts", email)
        }
    }
   

Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
    
    topHeadline("Waiting for Drop-Off", "Confirm delivery once you have drop off the package from the recipient designated location.", R.drawable.dropoff)
    
    DeliveryInformations(
    information = "Address to Drop-Off: $recipientAddress",
    resId = R.drawable.dropoff_pickup,  
    onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
    DeliveryInformations(
    information = "Quantity: $Quantity ",
    resId = R.drawable.ic_donationpackage,  
    onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
displayPackageInformation("$donationTitle","$donationDesc","$orgName",donationThumb)
    
    
        Box(modifier = Modifier.height(78.dp).fillMaxWidth())
    
Box(
    modifier = Modifier
        .padding(bottom = 20.dp), 
    contentAlignment = Alignment.BottomCenter // Align content at the bottom center
) {
    Row(
    horizontalArrangement = Arrangement.End, // Align buttons to the end of the row
    verticalAlignment = Alignment.CenterVertically, // Vertically center the buttons
    modifier = Modifier
        .fillMaxWidth() // Ensure it fills the width of the parent
        .padding(end = 25.dp) // Add padding from the end
) {
    
    
    Button(
                onClick = {
                
                val ticketId = generateRandomTicketId()
                val timeDate = formatTimestampOnlyDate(timestamp)
                    db.collection("GivnGoRiders")
                        .document("Rider")
                        .collection("MyRoutes")
                        .document(emailRider)
                        .collection("Routes_Status")
                        .document(documentId)
                        .update(
                "donation_route_status", "Completed",
                "donation_time_mark_as_recieved", timeDate,
                "recipient_email_contact", recipientEmail
            )
                        .addOnSuccessListener { documentReference ->
                            listenToAccountChangesDonorUpdate("BasicAccounts", recipientEmail)

        if (!accountFoundTwo) {
            listenToAccountChangesDonorUpdate("VerifiedAccounts", recipientEmail)
        }
        
        listenToAccountChangesRecipientNotifications("BasicAccounts", recipientEmail)
    if (!accountFoundFive) {
    listenToAccountChangesRecipientNotifications("VerifiedAccounts", recipientEmail)
    }
                
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore", "Error adding document", exception)
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF8070F6)
                ),
                modifier = Modifier.wrapContentSize().padding(4.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Confirm Drop off",
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            
    }
}



    }
}

@Composable
fun confirmPickupRider(Address: String, Quantity: String, donationTitle: String, donationDesc: String, donorName: String,donationThumb: String, email: String, recipientEmail: String, documentId: String, documentIdRec: String){
val context = LocalContext.current
Log.d("PackageView", "Donation: $donationTitle")
                        Log.d("PackageView", "Donation: $donationDesc")
                        Log.d("PackageView", "Donation: $Address")
                        Log.d("PackageView", "Donation: $email")
Toast.makeText(context, "Donation: $donationTitle", Toast.LENGTH_LONG).show()
  Toast.makeText(context, "Recipient Email: $recipientEmail", Toast.LENGTH_LONG).show()
    

var emailRider = SharedPreferences.getEmail(context) ?: "No Email"
 val activity = context as PackageView

    fun generateRandomTicketId(): String {
    // You can customize this to generate a more complex ID if needed
    return "TICKET-${Random.nextInt(100000, 999999)}"
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
    
    fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy, hh:mm a z", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date(timestamp))
    }

 val timestamp = System.currentTimeMillis()
    
    
    val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                var accountFoundTwo = false
                var accountFoundThree = false
                var accountFoundFour = false
                var orgName by remember { mutableStateOf("") }
                var recipientAddress by remember { mutableStateOf("") }

                
                fun listenToAccountChangesDonorUpdate(collectionPath: String, email: String) {
                    
                       val historyTracks = hashMapOf(
                                        "donation_schedule_status" to "In-Transit Status: Rider is on its way to drop the donation at recipient location",
                   "history_title" to  "In-Transit",
                    "history_description" to "Rider is on its way to drop the donation at recipient location",
                    "timestamp" to formatTimestamp(timestamp),
                    "query_timestamp" to timestamp
                    )
                    
                    val historyTracksTwo = hashMapOf(
                                        "donation_schedule_status" to "Package Collected: Status: Rider is on its way to drop the donation at recipient location",
                   "history_title" to  "Rider has picked up the package",
                    "history_description" to "Rider is on its to deliver the package",
  "timestamp" to formatTimestamp(timestamp),
                    "query_timestamp" to timestamp
                    )
                    
                accountsRef.document(collectionPath).collection("Recipient")
            .document(email)
            .collection("MySchedules")
            .document("Donations") 
            .collection("Schedules")
            .document(documentIdRec)
            .update(
            "donation_schedule_status", "In-Transit Status: Rider is on its way",
            "rider_email", emailRider
            )
            .addOnSuccessListener { doc ->
            accountFoundFour = true
            
            accountsRef.document(collectionPath).collection("Recipient")
            .document(email)
            .collection("MySchedules")
            .document("Donations") 
            .collection("Tracking")
            .document(documentIdRec)
            .collection("History")
            .add(historyTracks)
            .addOnSuccessListener { 
            
            
              accountsRef.document(collectionPath).collection("Recipient")
            .document(email)
            .collection("MySchedules")
            .document("Donations") 
            .collection("Tracking")
            .document(documentIdRec)
            .collection("History")
            .add(historyTracksTwo)
            .addOnSuccessListener { 
            
            
              activity.finish() 
              
            }
              
            }
            }
    }
    
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
                    recipientAddress = documentSnapshot.getString("address") ?: ""
                    accountFoundThree = true
                    }
            }
    }
    
    
    fun listenToAccountChangesDonorGetTwo(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Donor")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    orgName = documentSnapshot.getString("OrganizationName") ?: ""
                    accountFound= true
                    }
            }
    }
    
    LaunchedEffect(Unit) {
        
        listenToAccountChangesDonorGetTwo("BasicAccounts", email)
        
        listenToAccountChangesDonorGet("BasicAccounts", recipientEmail)
        
        if (!accountFound) {
            
            listenToAccountChangesDonorGetTwo("BasicAccounts", email)
        }
        
        if (!accountFoundThree) {
            listenToAccountChangesDonorGet("VerifiedAccounts", recipientEmail)
            
        }
    }
    
    
    
    
Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
    
    topHeadline("Donor Waiting for Pick-Up", "Confirm Pick-Up once you recieved the package from the donor pickup address, then you may proccedd to deliver the package to the recipient address", R.drawable.pending)

DeliveryInformations(
    information = "Address to Pickup: $recipientAddress",
    resId = R.drawable.dropoff_pickup,  
    onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
    DeliveryInformations(
    information = "Quantity: $Quantity ",
    resId = R.drawable.ic_donationpackage,  
    onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
displayPackageInformation(donationTitle,"$donationDesc","$orgName","$donationThumb")
    
    
    Box(modifier = Modifier.height(78.dp).fillMaxWidth())
    
Box(
    modifier = Modifier
        .padding(bottom = 20.dp), 
    contentAlignment = Alignment.BottomCenter // Align content at the bottom center
) {
    Row(
    horizontalArrangement = Arrangement.End, // Align buttons to the end of the row
    verticalAlignment = Alignment.CenterVertically, // Vertically center the buttons
    modifier = Modifier
        .fillMaxWidth() // Ensure it fills the width of the parent
        .padding(end = 25.dp) // Add padding from the end
) {
    
Button(
                onClick = {
                
                val ticketId = generateRandomTicketId()

                    db.collection("GivnGoRiders")
                        .document("Rider")
                        .collection("MyRoutes")
                        .document(emailRider)
                        .collection("Routes_Status")
                        .document(documentId)
                        .update(
                "donation_route_status", "Delivery Status: Delivery in progress",
                "recipient_address", recipientAddress,
                "ticket_id", ticketId 
            )
                        .addOnSuccessListener { documentReference ->
                            
                            listenToAccountChangesDonorUpdate("BasicAccounts", recipientEmail)

        if (!accountFoundFour) {
            listenToAccountChangesDonorUpdate("VerifiedAccounts", recipientEmail)
        }
                
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore", "Error adding document", exception)
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF8070F6)
                ),
                modifier = Modifier.wrapContentSize().padding(4.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Confirm Pick-Up",
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            
    }
}



    }
}


@Composable
fun confirmPickupRecipient(Address: String, Quantity: String, donationTitle: String, donationDesc: String, donorName: String,donationThumb: String, email: String, recipientEmail: String, documentId: String) {

val context = LocalContext.current
var emailRider = SharedPreferences.getEmail(context) ?: "No Email"
 val activity = context as PackageView

    fun generateRandomTicketId(): String {
    // You can customize this to generate a more complex ID if needed
    return "TICKET-${Random.nextInt(100000, 999999)}"
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
    
    val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                var recipientAddress by remember { mutableStateOf("") }

                
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
                    recipientAddress = documentSnapshot.getString("address") ?: ""
                    accountFound = true
                    }
            }
    }
    
    LaunchedEffect(Unit) {
        listenToAccountChangesDonorGet("BasicAccounts", recipientEmail)

        if (!accountFound) {
            listenToAccountChangesDonorGet("BasicAccounts", recipientEmail)

        }
    }
    
Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
    
    topHeadline("Confirm for Pick-Up", "Confirm Pick-Up once you have the donation package, then you may mark it as Recieved", R.drawable.pending)
    
    DeliveryInformations(
    information = "Address to Pickup: $Address",
    resId = R.drawable.dropoff_pickup,  
    onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
    DeliveryInformations(
    information = "Quantity: $Quantity ",
    resId = R.drawable.ic_donationpackage,  
    onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
displayPackageInformation("$donationTitle","$donationDesc","$donorName",donationThumb)
    
    Box(
    modifier = Modifier
        .padding(bottom = 20.dp), 
    contentAlignment = Alignment.BottomCenter // Align content at the bottom center
) {
    Row(
    horizontalArrangement = Arrangement.End, // Align buttons to the end of the row
    verticalAlignment = Alignment.CenterVertically, // Vertically center the buttons
    modifier = Modifier
        .fillMaxWidth() // Ensure it fills the width of the parent
        .padding(end = 25.dp) // Add padding from the end
) {
    
Button(
                onClick = {
                
                val ticketId = generateRandomTicketId()

                    db.collection("GivnGoRiders")
                        .document("Rider")
                        .collection("MyRoutes")
                        .document(emailRider)
                        .collection("Routes_Status")
                        .document(documentId)
                        .update(
                "donation_route_status", "Delivery Status: Delivery in progress",
                "recipient_address", recipientAddress,
                "ticket_id", ticketId 
            )
                        .addOnSuccessListener { documentReference ->
                            activity.finish()
                
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore", "Error adding document", exception)
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF8070F6)
                ),
                modifier = Modifier.wrapContentSize().padding(4.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Mark as Recieved",
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            
    }
}


    }
}


@Composable
fun requestView(Address: String, Quantity: String, donationTitle: String, donationCategory: String, donorName: String,donationThumb: String, email: String, recipientEmail: String, documentId: String, packaged_timepacked: String, donation_schedule_status: String) {
val context = LocalContext.current

val activity = context as PackageView

fun generateRandomTicketId(): String {
    // You can customize this to generate a more complex ID if needed
    return "TICKET-${Random.nextInt(100000, 999999)}"
}

val ticketId = generateRandomTicketId()

val randomUuid = UUID.randomUUID().toString()
   
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



val recF = SharedPreferences.getFirstName(context) ?: "developer@gmail.com"
val emailRider = SharedPreferences.getEmail(context) ?: "developer@gmail.com"
var fcmToken by remember { mutableStateOf("") }
var postDesc by remember { mutableStateOf("") }


val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                var accountFoundTwo = false
                
                fun listenToAccountChangesDonorGet(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Donor")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    postDesc = documentSnapshot.getString("donation_post_description") ?: ""
                    fcmToken = documentSnapshot.getString("fcmToken") ?: ""
                    
                    accountFound = true
                    }
            }
    }
    
    fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy, hh:mm a z", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date(timestamp))
    }

 val timestamp = System.currentTimeMillis()
    
    fun listenToAccountChangesDonorSave(collectionPath: String, emailss: String) {
                
                val donationpackageData = hashMapOf(
                        "donation_schedule_status" to "Pick-Up Status: Rider on its way to collect the donation from the donor",
                        "donation_post_title" to donationTitle,
                        "donation_post_description" to postDesc,
                        "donation_quantity" to Quantity,
                        "donor_address" to Address,
                        "donor_email_contact" to email,
                        "donation_thumbnail" to donationThumb,
                        "rider_email" to emailRider,
                        "ticket_id" to ticketId,
                        "id" to randomUuid,
                        "rider_trackinf_documentId" to documentId,
                        "donation_time_mark_as_recieved" to "unknown"
                    )
                    
                    val donationpackageDataTracking = hashMapOf(
                        "donation_post_title" to donationTitle,
                        "donation_post_description" to postDesc,
                        "donation_quantity" to Quantity,
                        "donation_thumbnail" to donationThumb,
                        "rider_email" to emailRider,
                        "ticket_id" to ticketId,
                        "id" to randomUuid,
                        "rider_trackinf_documentId" to documentId,
                        "donation_time_mark_as_recieved" to "unknown"
                    )
                    
                     
                    val historyTracks = hashMapOf(
                    "donation_schedule_status" to "Pick-Up Status: Rider on its way to collect the package",
                    "history_title" to  "Rider on its way to collect the package",
                    "history_description" to "Rider is ok its way to collect the package from the donor",
  "timestamp" to formatTimestamp(timestamp),
                    "query_timestamp" to timestamp
                    )
                    
                    val historyTracksOne = hashMapOf(
                    "donation_schedule_status" to donation_schedule_status,
                    "history_title" to "Donation Packed and ready by the donor",
                    "history_description" to "Donor has packed the donation ready and its waiting for the rider",
                      "timestamp" to packaged_timepacked,
                    "query_timestamp" to timestamp
                    )
                    
                accountsRef.document(collectionPath).collection("Recipient")
            .document(emailss)
            .collection("MySchedules")
            .document("Donations") 
            .collection("Schedules")
            .document(randomUuid)
            .set(donationpackageData)
            .addOnSuccessListener { 
            
            accountsRef.document(collectionPath).collection("Recipient")
            .document(emailss)
            .collection("MySchedules")
            .document("Donations") 
            .collection("Tracking")
            .document(randomUuid)
            .set(donationpackageDataTracking)
            .addOnSuccessListener { 
            
            accountsRef.document(collectionPath).collection("Recipient")
            .document(emailss)
            .collection("MySchedules")
            .document("Donations") 
            .collection("Tracking")
            .document(randomUuid)
            .collection("History")
            .add(historyTracksOne)
            .addOnSuccessListener { 
            
            accountsRef.document(collectionPath).collection("Recipient")
            .document(emailss)
            .collection("MySchedules")
            .document("Donations") 
            .collection("Tracking")
            .document(randomUuid)
            .collection("History")
            .add(historyTracks)
            .addOnSuccessListener { 
            
            sendPushNotification(fcmToken, "Delivery Request", "Rider $recF has accepted your request", emailRider)
                    
              accountFoundTwo = true  
              
              activity.finish() 
              
            }
              
            }
            
            }
            
            
            }
    }
    
    LaunchedEffect(Unit) {
        listenToAccountChangesDonorGet("BasicAccounts", email)

        if (!accountFound) {
            listenToAccountChangesDonorGet("VerifiedAccounts", email)
        }
    }
    
    


Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
    
    topHeadline("Pending Request", "Press confirm pickup to accept the pickup request of a package with the given designated pickup location given by the doner.", R.drawable.pending)
    
    DeliveryInformations(
    information = "Address to Pickup: $Address",
    resId = R.drawable.dropoff_pickup,  
    onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
    DeliveryInformations(
    information = "Quantity: $Quantity ",
    resId = R.drawable.ic_donationpackage,  
    onRowClick = { 

    }
)

Box(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
    
displayPackageInformation(donationTitle,donationCategory,donorName,donationThumb)

Box(modifier = Modifier.height(78.dp).fillMaxWidth())
    
Box(
    modifier = Modifier
        .padding(bottom = 20.dp), 
    contentAlignment = Alignment.BottomCenter // Align content at the bottom center
) {
    Row(
    horizontalArrangement = Arrangement.End, // Align buttons to the end of the row
    verticalAlignment = Alignment.CenterVertically, // Vertically center the buttons
    modifier = Modifier
        .fillMaxWidth() // Ensure it fills the width of the parent
        .padding(end = 25.dp) // Add padding from the end
) {
    
Button(
                onClick = {
                
                
                
                    val donationData = hashMapOf(
                        "donation_route_status" to "Pick-Up Status: Pickup to Donor in progress",
                        "donation_post_title" to donationTitle,
                        "donation_post_description" to postDesc,
                        "donation_quantity" to Quantity,
                        "donor_address" to Address,
                        "donor_email_contact" to email,
                        "recipient_email_contact" to recipientEmail,
                        "donation_status" to "ConfirmPickupRider",
                        "donation_thumbnail" to donationThumb,
                        "ticket_id" to ticketId,
                        "recipient_address" to "***l city",
                        "recipient_document_id_package" to randomUuid,
                        "donation_time_mark_as_recieved" to "unknown"
                    )

                    db.collection("GivnGoRiders")
                        .document("Rider")
                        .collection("MyRoutes")
                        .document(emailRider)
                        .collection("Routes_Status")
                        .add(donationData)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Firestore", "Document added with ID: ${documentReference.id}")
                            
                            db.collection("GivnGoRiders")
                        .document("Rider")
                        .collection("AvailableDeliveries")
                        .document(emailRider)
                        .collection("RequestStatus")
                        .document(documentId)
                        .delete() // Delete the document
                .addOnSuccessListener {
                    Log.d("Firestore", "Document with ID $documentId successfully deleted.")
                    Toast.makeText(context, "Request Acceptef", Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, "Document: $documentId", Toast.LENGTH_LONG).show()
    
                    listenToAccountChangesDonorSave("BasicAccounts", recipientEmail)

        if (!accountFoundTwo) {
            listenToAccountChangesDonorSave("VerifiedAccounts", recipientEmail)
        }
                    
    
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error deleting document: ", exception)
                    Toast.makeText(context, "Failed to reject request", Toast.LENGTH_SHORT).show()
                }
                
                   }
                        .addOnFailureListener { exception ->
                            Log.e("Firestore", "Error adding document", exception)
                        }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF8070F6)
                ),
                modifier = Modifier.wrapContentSize().padding(4.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Accept Request",
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
        // Box for Reject Request
        Button(
                onClick = {
                    // Handle reject action here
                    
                    db.collection("GivnGoRiders")
                        .document("Rider")
                        .collection("AvailableDeliveries")
                        .document(emailRider)
                        .collection("RequestStatus")
                        .document(documentId)
                        .delete() // Delete the document
                .addOnSuccessListener {
                    Log.d("Firestore", "Document with ID $documentId successfully deleted.")
                    Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show()
                    
    activity.finish()
    
                            
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error deleting document: ", exception)
                    Toast.makeText(context, "Failed to reject request", Toast.LENGTH_SHORT).show()
                }
                        
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFC9C9FF)
                ),
                modifier = Modifier.wrapContentSize().padding(4.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Reject Request",
                    fontSize = 11.sp,
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold
                )
            }
            
    }
}

    }
}
@Composable
fun TopBarBack(onMenuClick: () -> Unit, isScrolled: Boolean) {
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

@Composable
fun topHeadlineRecipient(Request_type: String, description: String, resId: Int) {
        Box(
            modifier = Modifier.heightIn(min = 100.dp) 
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF7067ED), Color(0xFF9A85FF)) // Change colors as needed
                    )
                )
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(top = 14.dp, start = 23.dp)
                ) {
                    Text(
                        text = "$Request_type",
                        fontSize = 23.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = "$description",
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 18.dp,bottom = 12.dp).width(200.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }
            }

            // Image aligned to the top end, center vertically
            Image(
                painter = painterResource(id = resId),
                contentDescription = "Back button",
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 8.dp, end = 8.dp)
                    .align(Alignment.TopEnd) // Align image to top-end
            )
        }
}


@Composable
fun topHeadline(Request_type: String, description: String, resId: Int) {
        Box(
            modifier = Modifier.heightIn(min = 100.dp) 
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF7067ED), Color(0xFF9A85FF)) // Change colors as needed
                    )
                )
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp, start = 23.dp)
                ) {
                    Text(
                        text = "$Request_type",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = "$description",
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 18.dp,bottom = 12.dp).width(220.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }
            }

            // Image aligned to the top end, center vertically
            Image(
                painter = painterResource(id = resId),
                contentDescription = "Back button",
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 8.dp, end = 8.dp)
                    .align(Alignment.TopEnd) // Align image to top-end
            )
        }
}

@Composable
fun DeliveryInformations(
    information: String, 
    resId: Int, 
    onRowClick: () -> Unit // Pass the onClick handler for the entire Row
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 26.dp, top = 4.dp, bottom = 4.dp)
            .clickable(onClick = { onRowClick() }), // Add clickable modifier to Row
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
            Image(
                painter = painterResource(id = resId),
                contentDescription = "back",
                modifier = Modifier.size(25.dp)
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
fun DeliveryInformationsRecipient(
    information: String, 
    resId: Int, 
    onRowClick: () -> Unit // Pass the onClick handler for the entire Row
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 26.dp, top = 4.dp, bottom = 4.dp)
            .clickable(onClick = { onRowClick() }), // Add clickable modifier to Row
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
            Image(
                painter = painterResource(id = resId),
                contentDescription = "back",
                modifier = Modifier.size(25.dp)
            )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "$information",
            color = Color(0xFF8070F6),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RiderInfo(
    information: String, 
    riderF: String,
    riderL: String,
    riderVehicle: String,
    resId: Int, 
    imageUri: String,
    onRowClick: () -> Unit // Pass the onClick handler for the entire Row
) {

Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
                        .clickable(onClick = { onRowClick() })
    ) {
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
             .padding(start = 25.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
            Image(
                painter = painterResource(id = resId),
                contentDescription = "back",
                modifier = Modifier.size(25.dp)
            )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "$information",
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(top = 6.dp, start = 3.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
    
    
    
    Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top =26.dp, bottom = 26.dp, start = 46.dp, end =  36.dp)
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .clip(RoundedCornerShape(18.dp))
                        .background(color = Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = "Rider name: $riderF $riderL",
                        fontSize = 15.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = "Vehicle Type: $riderVehicle",
                        fontSize = 12.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    
                }
            }
    }
}


@Composable
fun displayPackageInformation(
    userClaimedDonation: String,
    donationDescription: String,
    donerName: String,
    imageUri: String
) {
val thumbnail =  Uri.parse(imageUri).toString() 
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 8.dp)
            .clickable { } //onClick() 
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFAF9FF))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = thumbnail,
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
                    modifier = Modifier
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = userClaimedDonation,
                        fontSize = 18.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = donationDescription,
                        fontSize = 11.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    
                    Text(
                        text = "Donor: $donerName",
                        fontSize = 13.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}



@Composable
fun displayPackageInformationTwo(
    userClaimedDonation: String,
    donationDescription: String,
    donerName: String,
    status: String,
    imageUri: String,
    onRowClick: () -> Unit
) {

Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.clickable(onClick = { onRowClick() })
        ){

Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 4.dp, bottom = 4.dp)
            , // Add clickable modifier to Row
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        Text(
            text = "Donor by: $donerName",
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(bottom = 2.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
    
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 8.dp)
            .clickable { } //onClick() 
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFAF9FF))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                    modifier = Modifier
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = userClaimedDonation,
                        fontSize = 16.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = donationDescription,
                        fontSize = 11.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                    
                    Text(
                        text = "Status: $status",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top =16.dp),
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
        }
    }
}
