package com.go.givngo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*

import kotlinx.coroutines.launch
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.*
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.go.givngo.*
import com.go.givngo.Model.DonationPackageRequest
import com.go.givngo.OverscrollEffect.*
import com.go.givngo.SharedPreferences
import com.go.givngo.bottomBar.*
import com.go.givngo.bottomBar.components.*
import com.go.givngo.bottomBar.model.*
import com.go.givngo.donorSide.*
import com.go.givngo.progressIndicator.DotProgressIndicator
import com.go.givngo.recipientSide.ClaimedDonations
import com.go.givngo.ui.modifer.drawColoredShadow
import com.go.givngo.ui.theme.MyComposeApplicationTheme
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.go.givngo.ridersSide.*
import com.go.givngo.Model.DonationPackageMyRoutes

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.forEachGesture
import com.go.givngo.Messenger.messengerUserList

@Composable
fun MyAppRider(userFinishBasic: String) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val navigationItems = listOf(
        RiderNavigationScreen.Home,
        RiderNavigationScreen.Deliv,
        RiderNavigationScreen.MyRoutes
    )

    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val activity = (context as MainActivity)

    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.Transparent.toArgb()

        WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            ?.isAppearanceLightStatusBars = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_theme),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter)
                .background(Color.White)
        )

        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color.Transparent,
            drawerElevation = 0.dp,
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .background(color = Color.Transparent),
            topBar = {
                TopBar {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            },
            drawerContent = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(280.dp)
                        .clip(RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
                        .background(Color.White, shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
                        .padding(end = 25.dp)
                ) {
                    AppDrawer(
                        userFinishBasic,
                        onItemClicked = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }
                    )
                }
            },
            drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
            drawerScrimColor = Color.Black.copy(alpha = 0.12f),
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp, bottom = 23.dp)
                        .drawColoredShadow(
                            color = Color.Black,
                            alpha = 0.1f,
                            borderRadius = 30.dp,
                            blurRadius = 2.dp,
                            offsetY = 5.dp
                        )
                        .background(Color(0xFFFAF9FE), shape = RoundedCornerShape(23.dp))
                        .padding(top = 6.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedBottomBar(
                            selectedItem = selectedItem,
                            itemSize = navigationItems.size,
                            indicatorDirection = IndicatorDirection.BOTTOM,
                            containerColor = Color.Transparent,
                            indicatorColor = Color(0xFF8070F6),
                            indicatorStyle = IndicatorStyle.DOT,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            navigationItems.forEachIndexed { index, navigationItem ->
                                val imageVector = ImageVector.vectorResource(id = navigationItem.iconResId)
                                BottomBarItem(
                                    selected = currentRoute == navigationItem.route,
                                    onClick = {
                                        if (currentRoute != navigationItem.route) {
                                            selectedItem = index
                                            navController.navigate(navigationItem.route) {
                                                navController.graph.startDestinationRoute?.let { route ->
                                                    popUpTo(route) {
                                                        saveState = true
                                                    }
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    imageVector = imageVector,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    label = navigationItem.title,
                                    containerColor = Color.Transparent,
                                    iconColor = Color(0xFF8070F6),
                                    textColor = Color(0xFF8070F6),
                                    itemStyle = ItemStyle.STYLE2
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            androidx.navigation.compose.NavHost(
                navController = navController,
                startDestination = RiderNavigationScreen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(RiderNavigationScreen.Home.route) {
                    HomeScreenRider(navController) {
                        selectedItem = 0
                    }
                }
                composable(RiderNavigationScreen.Deliv.route) { AvailableDeliveriesSection() }
                composable(RiderNavigationScreen.MyRoutes.route) { MyRoutesSection() }
            }
            Box(
                            modifier = Modifier
                                                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            // Update the offsets based on drag gesture
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .offset {
                        IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
                    }                        
                        )
                    {
                    
                       // Movable ChatBox
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEB4747))
                        .clickable {
                            val intent = Intent(context, messengerUserList::class.java).apply {
                                putExtra("accountType", "Rider")
                            }
                            context.startActivity(intent)
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_chat),
                        contentDescription = "Chat",
                        modifier = Modifier.align(Alignment.Center).size(50.dp)
                    )
                }
                
                    }
        }
    }
}

sealed class RiderNavigationScreen(val route: String, val iconResId: Int, val title: String) {
    object Home : RiderNavigationScreen("Home", R.drawable.ic_homeheart, "Home")
    object Deliv : RiderNavigationScreen("Deliveries", R.drawable.ic_deliveries, "Deliveries")
    object MyRoutes : RiderNavigationScreen("My Routes", R.drawable.ic_deliveries, "My Routes")
}

@Composable
fun RiderBrowseOptions(navController: NavController, setSelectedItem: (Int) -> Unit) {
val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 8.dp),
            horizontalAlignment = Alignment.Start // Align the boxes to the start
        ) {
            Box(
                modifier = Modifier
                    .height(45.dp)
                    .background(color = Color(0xFFF6F5FF), shape = RoundedCornerShape(25.dp))
                     .clickable {
                         setSelectedItem(1)
                         navController.navigate(RiderNavigationScreen.Deliv.route)
                    }
                    .wrapContentWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_boxwithheart),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            
                            .padding(2.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Available Deliveries",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 2.dp),
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space between buttons

            Box(
                modifier = Modifier
                    .height(45.dp)
                    .background(color = Color(0xFFF6F5FF), shape = RoundedCornerShape(25.dp))
                        .clickable {
                        setSelectedItem(1)
                        navController.navigate(RiderNavigationScreen.MyRoutes.route)
                    }
                    .wrapContentWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_boxwithcheck),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "My Routes",
                        fontSize = 14.sp,
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

// Model will be donation column box then inside row Image then width space 10.dp Column text Donation title and bottom donation description after column then Image ArrowRight align end padding end 10.dp then outside the box with row and column another column where nested inside the rows columns is Text pending request status spacing height 10.dp
@Composable
fun AvailableDeliveriesSection() {
    val context = LocalContext.current
    val emailRider = SharedPreferences.getEmail(context) ?: "developer@gmail.com"
    val firestore = FirebaseFirestore.getInstance()
    var donationPackages by remember { mutableStateOf<List<DonationPackageRequest>>(emptyList()) }
    var documentId by remember { mutableStateOf("") }
LaunchedEffect(Unit) {
    firestore.collection("GivnGoRiders")
    .document("Rider")
    .collection("AvailableDeliveries")
    .document(emailRider)
    .collection("RequestStatus")
    .get()
    .addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
            donationPackages = querySnapshot.documents.mapNotNull { document ->
                document.toObject(DonationPackageRequest::class.java)?.apply {
                        this.documentId = document.id // Add the document ID to the object
                    }
            }
            Log.d("FirestoreData", "Filtered donation packages: $donationPackages")
            Toast.makeText(
                context,
                "Fetched ${donationPackages.size} pending donation packages",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Log.d("FirestoreData", "No pending donation packages found.")
            Toast.makeText(
                context,
                "No pending donation packages found.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    .addOnFailureListener { exception ->
        Log.e("FirestoreError", "Error fetching filtered data: ${exception.message}", exception)
        Toast.makeText(
            context,
            "Failed to load data: ${exception.message}",
            Toast.LENGTH_LONG
        ).show()
    }

}


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 3.dp)
            .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
            .background(color = Color.White)
    ) {
        Text(
            text = "Available Deliveries For You!",
            fontSize = 18.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(start = 25.dp, top = 25.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
        
        Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start // Align content to start in the column
    ) {
    
    Row(
            modifier = Modifier.fillMaxWidth(), 
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween 
        ) {
        
        Text(
                text = "Here are the available delivering packages request available for you.",
                fontSize = 13.sp,
                color = Color(0xFF8070F6),
                modifier = Modifier
                .width(210.dp)
                    .padding(start = 25.dp),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start
            )

            // Image aligned to center end
            Image(
                painter = painterResource(id = R.drawable.ic_donationpackage),
                contentDescription = "Back button",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 25.dp)
            )
            
        }
    }
    

        Spacer(modifier = Modifier.height(16.dp))

        // Check if `donationPackages` is empty
        if (donationPackages.isEmpty()) {
            // Show a message or an illustration when no data is available
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            LottieAnimationFromUrl("https://lottie.host/77242390-d37d-4bcb-8386-cfc8cf6036a5/jh89yhzmb5.json")
                Text(
                    text = "No deliveries available at the moment.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
                
            }
        } else {
            // Show the list of donation packages using LazyColumn
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(donationPackages) { packageRequest ->
                    displayPackageInformation(
                        userClaimedDonation = packageRequest.userClaimedDonation,
                        donationDescription = packageRequest.donationDescription,
                        imageUri = packageRequest.donationThumbnail,
                        packageStatus = packageRequest.packageStatus,
                        onClick = {
                        val intent = Intent(context, PackageView::class.java).apply {
                        putExtra("documentId", packageRequest.documentId)
    putExtra("packageViewType", "Request")
    putExtra("donationPackageRequest", packageRequest)
}
context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}


//A status of delivery informations of the rider with three rounded corners tabs "Completed" "Delivery" "Pick-ups"
@Composable
fun MyRoutesSection(){
val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("Deliveries") }
    val pickupSchedules = remember { mutableStateListOf<DonationPackageMyRoutes>() }
val deliverySchedules = remember { mutableStateListOf<DonationPackageMyRoutes>() }
val completedSchedules = remember { mutableStateListOf<DonationPackageMyRoutes>() }

    
    val firestore = FirebaseFirestore.getInstance()

    var documentIdRider by remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
    var packageViewType by remember { mutableStateOf("") }
    val statusType = "Rider"
    val emailRider = SharedPreferences.getEmail(context) ?: "developer@gmail.com"

    LaunchedEffect(Unit) {
        firestore.collection("GivnGoRiders")
            .document(statusType)
            .collection("MyRoutes")
            .document(emailRider)
            .collection("Routes_Status") // Assuming schedules are stored in this sub-collection
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                 documentIdRider = document.id 
                    val claimStatus = document.getString("donation_route_status")
                    val donationPost = DonationPackageMyRoutes(
                        userClaimedDonation = document.getString("donation_post_title") ?: "",
                        donationDescription = document.getString("donation_post_description") ?: "",
                        donationQuantity = document.getString("donation_quantity") ?: "",
                       donorAddress = document.getString("donor_address") ?: "",
                        donorEmail = document.getString("donor_email_contact") ?: "",
                        recipientEmail = document.getString("recipient_email_contact") ?: "",
                        packageStatus = document.getString("donation_route_status") ?: "",
                        donationThumbnail = document.getString("donation_thumbnail") ?: "",
                        ticketId = document.getString("ticket_id") ?: "",
                        documentId = document.getString("recipient_document_id_package") ?: ""
                        
                    )

                    // Add to respective lists based on claimStatus
                    if (claimStatus == "Pick-Up Status: Pickup to Donor in progress") {
                        pickupSchedules.add(donationPost)
                        
                        packageViewType = "ConfirmPickupRiderMyRoutes"
                    } else if (claimStatus == "Delivery Status: Delivery in progress") {
                        deliverySchedules.add(donationPost)
                        
                        packageViewType = "DropOffMyRoutes"
                    }else if (claimStatus == "Completed"){
                    completedSchedules.add(donationPost)
                    
                    packageViewType = "RecievedMyRoutes"
                    }
                }
                // Set error state to true if both lists are empty
                isError.value = deliverySchedules.isEmpty() && pickupSchedules.isEmpty() && completedSchedules.isEmpty()
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

        topCategoryRiderMyRoutes()

        // Category selection row
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 30.dp, end = 40.dp)
        ) {
            categoryDonationChooser("Completed", selectedCategory) { selectedCategory = it }
            categoryDonationChooser("Deliveries", selectedCategory) { selectedCategory = it }
            categoryDonationChooser("Pickups", selectedCategory) { selectedCategory = it }
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalLine()
        Spacer(modifier = Modifier.height(4.dp))

        // Determine the selected items list based on the category
        val itemsList = if (selectedCategory == "Deliveries") deliverySchedules else if (selectedCategory == "Completed") completedSchedules else pickupSchedules

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
                    text = "Empty Routes",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8070F6)
                )
            }
        } else {
            // Display the LazyRow for the selected items list
            LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)
) {
    items(count = itemsList.size) { index ->
        val donation = itemsList.getOrNull(index) ?: return@items
        displayPackageInformation(
            userClaimedDonation = donation.userClaimedDonation,
            imageUri = donation.donationThumbnail,
            donationDescription = donation.donationDescription,
            packageStatus = donation.packageStatus,
            onClick = {
                val intent = Intent(context, PackageView::class.java).apply {
                    putExtra("packageViewType", packageViewType)
                    putExtra("documentId", documentIdRider)
                    putExtra("documentidmyroutes", donation.documentId)
                    putExtra("title", donation.userClaimedDonation)
                    putExtra("description", donation.donationDescription)
                    putExtra("thumbnail", donation.donationThumbnail)
                    putExtra("packageStatus", donation.packageStatus)
                    putExtra("quantity", donation.donationQuantity)
                    putExtra("address_donor", donation.donorAddress)
                    putExtra("donorEmail", donation.donorEmail)
                    putExtra("email_rider", donation.recipientEmail)
                    putExtra("documentIdFromRider", donation.documentId)
                    putExtra("ticketTrackingId", donation.ticketId)
                    
                }
                context.startActivity(intent)
            }
        )
    }
}


        }
    }
}

@Composable
fun topCategoryRiderMyRoutes() {

    Column(
        modifier = Modifier.fillMaxWidth(),  // Ensures the column takes the full width
        horizontalAlignment = Alignment.Start // Aligns the content to the start
    ) {
        Text(
            text = "My Routes",
            fontSize = 23.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
                .padding(start = 25.dp) // Padding from the start for centering purpose
                .fillMaxWidth(),  // Ensures the text takes the full width
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start // Align text to the start within the text box
        )
    }
}


@Composable
fun displayPackageInformation(
    userClaimedDonation: String,
    donationDescription: String,
    imageUri: String,
    packageStatus: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .clickable { onClick() }
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
                        .padding(start = 10.dp)
                        .weight(1f)
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

                if (packageStatus == "Request Status: Pending") {
                    Spacer(modifier = Modifier.weight(1f)) // Push the Image to the end
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrowright),
                        contentDescription = "Arrow right",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 10.dp)
                    )
                }
            }
        }
        Text(
            text = packageStatus,
            fontSize = 13.sp,
            color = Color(0xFF8171F8),
            modifier = Modifier.padding(top = 6.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
