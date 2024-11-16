package com.go.givngo.recipientSide

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
import androidx.compose.foundation.lazy.*
import com.google.firebase.firestore.Query

class trackingDelivery : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

val documentId = intent.getStringExtra("documentId") ?: "Unknown Ticket"

val packageViewType = intent.getStringExtra("packageViewType") ?: "Request"
        val timeRecieved = intent.getStringExtra("timeRecieved") ?: ""
        val emailRider = intent.getStringExtra("emailRider") ?: ""
        val emailRecipient = intent.getStringExtra("recipientEmail") ?: ""
        val riderPic = intent.getStringExtra("profileImage") ?: ""
        
        setContent {
            MyComposeApplicationTheme {
            
                MyAppPackageView(packageViewType = packageViewType,timeRecieved,emailRider,emailRecipient,documentId,riderPic)
            }
        }
    }
}

@Composable
fun MyAppPackageView(packageViewType: String,timeRecieved: String, emailRider: String, emailRecipient: String, documentId: String, riderPic: String)

{

Log.d("PackageView", "PackageViewType: $packageViewType")

    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as trackingDelivery)

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
            
            
                val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                
    

                when (packageViewType) {
            /* recipient */     "Tracking" -> tracking(emailRider,timeRecieved, emailRecipient, documentId,riderPic)
                    else -> tracking(emailRider, timeRecieved, emailRecipient, documentId,riderPic)
                }
            }
        }
    }
}
data class DeliveryHistory(
    val timeReceived: String = "",
    val status: String = "",
    val historyDescription: String = ""
)

@Composable
fun tracking(emailRider: String, timeRecieved: String, recipientEmail: String, documentId: String, riderPic: String){
val context = LocalContext.current

 val activity = context as trackingDelivery
 
    val db = FirebaseFirestore.getInstance()
                val accountsRef = db.collection("GivnGoAccounts")
                var accountFound = false
                var accountFoundTwo = false
                
                var riderVehicleType by remember { mutableStateOf("") }
                var riderLName by remember { mutableStateOf("") }
                var riderFName by remember { mutableStateOf("") }
                var trackingId by remember { mutableStateOf("") }
                
    
    fun listenToAccountChangesRiderGet(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Rider")
            .document(email)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    riderFName = documentSnapshot.getString("Rider First Name") ?: ""
                    riderLName = documentSnapshot.getString("Rider Last Name") ?: ""
                    riderVehicleType = documentSnapshot.getString("volunteer_courrier") ?: ""
                    
                    accountFound = true
                    }
            }
    }
    
    fun listenToAccountChangesRecipientGet(collectionPath: String, email: String) {
                
                accountsRef.document(collectionPath).collection("Recipient")
            .document(email).collection("MySchedules")
            .document("Donations") 
            .collection("Tracking")
            .document(documentId)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Extract data from the document
                    trackingId = documentSnapshot.getString("ticket_id") ?: ""
                    
                    accountFoundTwo = true
                    }
            }
    }
    
    LaunchedEffect(Unit) {
        listenToAccountChangesRiderGet("BasicAccounts", emailRider)
        if (!accountFound) {
            listenToAccountChangesRiderGet("VerifiedAccounts", emailRider)
        }
    }
    
    LaunchedEffect(Unit) {
    listenToAccountChangesRecipientGet("BasicAccounts", recipientEmail)
    if (!accountFoundTwo) {
    listenToAccountChangesRecipientGet("VerifiedAccounts", recipientEmail)
    }
    }
    
    var historyList by remember { mutableStateOf<List<DeliveryHistory>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

fun listenToAccountChangesDonorUpdate(
    collectionPath: String,
    email: String,
    documentIdRec: String,
    onSuccess: (List<DeliveryHistory>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val historyList = mutableListOf<DeliveryHistory>()
    val historyRef = accountsRef.document(collectionPath)
        .collection("Recipient")
        .document(email)
        .collection("MySchedules")
        .document("Donations")
        .collection("Tracking")
        .document(documentIdRec)
        .collection("History")
            .orderBy("query_timestamp", Query.Direction.DESCENDING)
    historyRef.get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                val timeReceived = document.getString("timestamp") ?: ""
                val status = document.getString("history_title") ?: ""
                val historyDescription = document.getString("history_description") ?: ""

                val deliveryHistory = DeliveryHistory(
                    timeReceived = timeReceived,
                    status = status,
                    historyDescription = historyDescription
                )
                historyList.add(deliveryHistory)
            }
            onSuccess(historyList)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

    LaunchedEffect(Unit) {
        listenToAccountChangesDonorUpdate(
            collectionPath = "BasicAccounts",
            email = "$recipientEmail",
            documentIdRec = documentId,
            onSuccess = { list ->
                historyList = list
            },
            onFailure = { exception ->
                errorMessage = exception.message
            }
        )
    }


Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
    
    topHeadline("Recieved!","$timeRecieved","You recieved your package, we do hope you enjoy and support our app and be with us again!", R.drawable.recieved)
    
    RiderInfo(
    "$riderFName","$riderLName","$riderVehicleType",R.drawable.boxchecked,riderPic,
    onRowClick = { 

    }
)

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
        .padding(end = 10.dp) // Add padding from the end
) {
    
Button(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF8070F6)
                ),
                modifier = Modifier.width(200.dp).height(40.dp).padding(4.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Leave a happy feedback",
                    fontSize = 11.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            }
            
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp, start = 28.dp, bottom = 16.dp)
                ) {
                Text(
                    text = "Tracking Id: $trackingId",
                    fontSize = 15.sp,
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold
                )
                }
}       
                
            Box(modifier = Modifier.height(16.dp).fillMaxWidth().background(Color(0xFFEAE8FF)))
            
                            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(top = 6.dp, start = 20.dp)
                ) {
                Text(
                    text = "Tracking of your delivery",
                    fontSize = 14.sp,
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold
                )
                }
          }
                
                    if (!errorMessage.isNullOrEmpty()) {
        Text(
            text = errorMessage ?: "Unknown error",
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        HistoryList(historyList)
    }
   

    }
}

@Composable
fun HistoryList(historyList: List<DeliveryHistory>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Space between items
    ) {
        itemsIndexed(historyList) { index, history ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Vertical line behind the content
                if (index != historyList.lastIndex) { // Avoid drawing the line for the last item
                    
                    Box(
    modifier = Modifier
        .width(2.dp)
        .fillMaxHeight()
        .background(Color(0xFFD5D3E8))
        .padding(start = 8.dp)
        .offset(x = 22.dp) // Slightly move up or down
)

                }

                // History content
                historyRecieved(
                    timeRecived = history.timeReceived,
                    status = history.status,
                    historyDescription = history.historyDescription
                )
            }
        }
    }
}

@Composable
fun historyRecieved(timeRecived: String, status: String, historyDescription: String) {
    // Get the current date in the same format as `timeRecived`
    val currentDate = remember {
        val formatter = SimpleDateFormat("MMM dd, yyyy, hh:mm a z", Locale.getDefault())
        val todayFormatter = SimpleDateFormat("MMM dd, yyyy, hh:mm a z", Locale.getDefault())
        try {
            val currentTime = formatter.parse(timeRecived)
            todayFormatter.format(Date()) == todayFormatter.format(currentTime)
        } catch (e: Exception) {
            false // If parsing fails, assume it's not the current date
        }
    }

    // Determine the colors based on the condition
    val boxColor = if (status == "Recieved!") Color(0xFF8070F6) else Color(0xFFD5D3E8)
    val timeColor = if (currentDate) Color(0xFF8070F6) else Color(0xFF8C829B)

    Row(
        modifier = Modifier
            .padding(18.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Time text
        Text(
            text = timeRecived,
            fontSize = 12.sp,
            modifier = Modifier.width(60.dp),
            color = timeColor,
            fontWeight = FontWeight.Bold
        )

        // Box with conditional color
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(boxColor) // Conditional color
                .padding(start = 12.dp)
        ) {
            // Optional content inside the box
        }

        // Status and description
        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = status,
                fontSize = 16.sp,
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = historyDescription,
                fontSize = 12.sp,
                color = Color(0xFF8C829B),
                fontWeight = FontWeight.Bold
            )
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
fun topHeadline(Request_type: String, timeRecieved: String, description: String, resId: Int) {
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
                        fontSize = 23.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    
                    Text(
                        text = "$timeRecieved",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )


                    Text(
                        text = "$description",
                        fontSize = 13.sp,
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
fun RiderInfo(
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
    ) {
    
    Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 26.dp,end = 26.dp,bottom = 20.dp)
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clip(RoundedCornerShape(18.dp))
                        .background(color = Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(start = 14.dp)
                ) {
                    Text(
                        text = "Rider name: $riderF $riderL",
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
            fontSize = 14.sp,
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
                        fontSize = 13.sp,
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
