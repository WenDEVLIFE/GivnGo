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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        
        setContent {
            MyComposeApplicationTheme {
                val context = LocalContext.current
                val statusUser = SharedPreferences.getStatusType(context) ?: "Developer"
                val finishBasic = intent.getStringExtra("signup_finish_basic") ?: "Developer"
                
                BackHandler {
                    // Handle back press here
                    // For example, to quit the app:
                    finish()
                }
                
                when (statusUser) {
                    "Donor" -> {
                        MyAppDonor(userFinishBasic = finishBasic)
                    }
                    "Recipient" -> {
                        MyAppRecipient(userFinishBasic = finishBasic)
                    }
                    "Rider" -> {
                        // Add logic for Rider if needed
                    }
                    "Developer" -> {
                        val intent = Intent(context, greetings::class.java)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun MyAppDonor(userFinishBasic: String) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val navigationItems = listOf(
        MainNavigation.Home,
        MainNavigation.Points,
        MainNavigation.MyDonations
    )
    
    

    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val activity = (context as MainActivity)
    
    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.Transparent.toArgb() 
    
WindowCompat.getInsetsController(activity.window, activity.window.decorView)?.isAppearanceLightStatusBars = false
}

    Box(
        modifier = Modifier
            .fillMaxSize()
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
            topBar = { TopBar{ coroutineScope.launch { scaffoldState.drawerState.open() } } },
            drawerContent = {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .clip(RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)) 
            .background(Color.White, shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
            .padding(end=25.dp)
    ) {
        AppDrawer(
        userFinishBasic,
            onItemClicked = {
                coroutineScope.launch { scaffoldState.drawerState.close() }
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
                            color = Color.Black, // Shadow color
                            alpha = 0.1f, // Shadow alpha
                            borderRadius = 30.dp, // Rounded corners
                            blurRadius = 2.dp, // Blur radius
                            offsetY = 5.dp // Y offset for the shadow
                        )
                        .background(Color(0xFFFAF9FE), shape = RoundedCornerShape(23.dp))
                        .padding(top = 6.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center, // Centers items vertically within the Column
                        horizontalAlignment = Alignment.CenterHorizontally // Centers items horizontally within the Column
                    ) {
                        AnimatedBottomBar(
                            selectedItem = selectedItem,
                            itemSize = navigationItems.size,
                            indicatorDirection = IndicatorDirection.BOTTOM,
                            containerColor = Color.Transparent,
                            indicatorColor = Color(0xFF8070F6),
                            indicatorStyle = IndicatorStyle.DOT,
                            modifier = Modifier
                                .padding(bottom = 8.dp) // Add bottom padding here
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
                                   modifier = Modifier
                                .padding(bottom = 8.dp), 
                                    label = navigationItem.title,
                                    containerColor = Color.Transparent,
                                    iconColor = Color(0xFF8070F6),
                                    textColor = Color(0xFF8070F6),
                                    itemStyle = ItemStyle.STYLE6WITHPOINTS
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = MainNavigation.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(MainNavigation.Home.route) { HomeScreenDonor() }
        composable(MainNavigation.Points.route) { donationPoints() }
     composable(MainNavigation.MyDonations.route) { myDonations() }
            }
        }
    }
}



/*

DONOR SIDE FLOW

*/



// Navigation Host
@Composable
fun NavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MainNavigation.Home.route) {
        composable(MainNavigation.Home.route) { HomeScreenDonor() }
        composable(MainNavigation.Points.route) { donationPoints() }
        composable(MainNavigation.MyDonations.route) { myDonations() }
    }
}
sealed class MainNavigation(val route: String, val iconResId: Int, val title: String) {
    object Home : MainNavigation("Home", R.drawable.ic_homeheart, "Home")
    object Points : MainNavigation("Points", R.drawable.ic_mypoints, "Points")
    object MyDonations : MainNavigation("My Donations", R.drawable.ic_deliveries, "My Donations")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenDonor() {
    val context = LocalContext.current
    val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"
    
    var selectedCategory by remember { mutableStateOf("Meals") }
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()

    val maxPadding = 100.dp
    val paddingTop = derivedStateOf {
        val paddingPx = with(density) { maxPadding.toPx() - scrollState.value }
        paddingPx.coerceIn(0f, with(density) { maxPadding.toPx() })
    }.value
    
    userProfileAlignment()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = with(density) { paddingTop.toDp() }) // Apply padding directly
            .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
            .overScroll(overscrollEffect = pushDownOverscrollEffect
      )
            .background(color = Color.White)
    ) {
        GreetingSection()
        Spacer(modifier = Modifier.height(16.dp))
        CardPoints()
        Spacer(modifier = Modifier.height(18.dp))
        HorizontalLine()
        Spacer(modifier = Modifier.height(18.dp))
        CategoryHeadline()
        CategoryCardSection(
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = category
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        currentSelectedCategory(categoryN = selectedCategory)
        Spacer(modifier = Modifier.height(4.dp))
        CategoryButtons(selectedCategory = selectedCategory)
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(color = Color.Transparent)
        )
    }
}

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




@Composable
fun NavigationItem(navItem: MainNavigation) {
    Column(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = painterResource(id = navItem.iconResId),
            contentDescription = navItem.title
        )
        Text(text = navItem.title)
    }
}


@Composable
fun donationPoints() {
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    // State to hold points for each category
    var userCategoryPoints by remember { mutableStateOf(0) }
    var mealsPoints by remember { mutableStateOf(0) }
    var clothingsPoints by remember { mutableStateOf(0) }
    var furnituresPoints by remember { mutableStateOf(0) }
    var booksPoints by remember { mutableStateOf(0) }
    var stationeryPoints by remember { mutableStateOf(0) }
    var householdsPoints by remember { mutableStateOf(0) }

    val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
    val statusType = "Donor"
    val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

    // Fetch category points from Firestore every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            firestore.collection("GivnGoAccounts")
                .document(userAccountType)
                .collection(statusType)
                .document(userCurrentSignedInEmail)
                .collection("MyPoints")
                .document("Post")
                .collection("Unclaimed_Points")
                .get()
                .addOnSuccessListener { documents ->
                    // Reset points for all categories
                    mealsPoints = 0
                    clothingsPoints = 0
                    furnituresPoints = 0
                    booksPoints = 0
                    stationeryPoints = 0
                    householdsPoints = 0

                    for (document in documents) {
                        val category = document.getString("donation_category") ?: ""
                        val points = (document.getLong("donation_points_to_claim") ?: 0L).toInt()

                        // Update points based on category
                        when (category) {
                            "Meals" -> mealsPoints = points
                            "Clothings" -> clothingsPoints = points
                            "Furnitures" -> furnituresPoints = points
                            "Books" -> booksPoints = points
                            "Stationery" -> stationeryPoints = points
                            "Households items" -> householdsPoints = points
                        }
                    }

                    // Calculate total points
                    userCategoryPoints = listOf(
                        mealsPoints,
                        clothingsPoints,
                        furnituresPoints,
                        booksPoints,
                        stationeryPoints,
                        householdsPoints
                    ).sum()
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreError", "Failed to retrieve points: ${e.message}")
                }

            // Wait for 5 seconds before the next update
            delay(5000L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 3.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp)
            )
    ) {
        // Container for the title and total points
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, top = 30.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Donation Points Category",
                fontSize = 18.sp,
                color = Color(0xFFF9B733),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )

            Text(
                text = "Your Total Points: $userCategoryPoints",
                fontSize = 13.sp,
                color = Color(0xFF312C3F),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
            ) {
                pointsCategoryMeal(
                    pointsCardsTitles = "Meals",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQB1lygTI9wDhY4yJYk-v4xvUgzhUMqf_OHt1CX7tuM7JOzW_Dwmh4NDlV1&s=10",
                    eachPointsCard = 10,
                    initialNewPointsEachCategory = mealsPoints
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                pointsCategoryClothings(
                    pointsCardsTitles = "Clothings",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcToALnvCoH1UuiCETyJNZeWxbeG0KxHkqq69-OWWHb5U_w7SuXFB19eVCE&s=10",
                    eachPointsCard = 6,
                    initialNewPointsEachCategory = clothingsPoints
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                pointsCategoryFurnitures(
                    pointsCardsTitles = "Furnitures",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy4sMWflhyMMxwYxxYjPt5Z5SP62WZhLam40lxef5RZmtQcFp0u5uEFns&s=10",
                    eachPointsCard = 4,
                    initialNewPointsEachCategory = furnituresPoints
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                pointsCategoryBooks(
                    pointsCardsTitles = "Books",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy4sMWflhyMMxwYxxYjPt5Z5SP62WZhLam40lxef5RZmtQcFp0u5uEFns&s=10",
                    eachPointsCard = 2,
                    initialNewPointsEachCategory = booksPoints
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                pointsCategoryStationery(
                    pointsCardsTitles = "Stationery",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy4sMWflhyMMxwYxxYjPt5Z5SP62WZhLam40lxef5RZmtQcFp0u5uEFns&s=10",
                    eachPointsCard = 2,
                    initialNewPointsEachCategory = stationeryPoints
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                pointsCategoryHouseholds(
                    pointsCardsTitles = "Households items",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy4sMWflhyMMxwYxxYjPt5Z5SP62WZhLam40lxef5RZmtQcFp0u5uEFns&s=10",
                    eachPointsCard = 8,
                    initialNewPointsEachCategory = householdsPoints
                )
            }
        }
    }
}



@Composable
fun pointsCategoryMeal(
    pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



val context = LocalContext.current


var latestNewPoints by remember { mutableStateOf(0) }

latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()

    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }
    
    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
        val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                             //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0
                                                    
                                                
                
                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun pointsCategoryClothings(
    pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



val context = LocalContext.current


var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()
                    

    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }
    
    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
        val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                             //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0
                                                    
                                                
                
                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun pointsCategoryFurnitures(
    pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



val context = LocalContext.current


var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()
                    

    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }
    
    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
        val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                             //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0
                                                    
                                                
                
                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun pointsCategoryBooks(
      pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



val context = LocalContext.current


var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()
                    

    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }
    
    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
        val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                             //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0
                                                    
                                                
                
                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun pointsCategoryStationery(
      pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



val context = LocalContext.current


var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()
                    

    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }
    
    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
        val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                             //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0
                                                    
                                                
                
                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun pointsCategoryHouseholds(
      pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



val context = LocalContext.current


var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()
                    

    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }
    
    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
        val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                             //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0
                                                    
                                                
                
                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}





sealed class ImageLoadState {
    object Success : ImageLoadState()
    object Error : ImageLoadState()
    object Loading : ImageLoadState()
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



data class MyDonation(
    val title: String,
    val description: String,
    val category: String,
    val points: String,
    val quantity: String,
    val timestamp: Timestamp,
    val thumbnailUri: Uri? = null,
    val storedImages: List<Uri> = emptyList()
)


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
fun GreetingSection() {
    val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (currentTime) {
        in 0..11 -> "Good Morning!"
        in 12..17 -> "Good Afternoon!"
        else -> "Good Evening!"
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = greeting,
            fontSize = 22.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
                .padding(top = 23.dp, start = 25.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}



@Composable
fun CardPoints() {
    val newUser = "Start Earning Points Today!"
    val bottomInstructions = "Start by posting a donation or claim points on my points section below."

    val oldUser = "Keep Going, points can get higher and higher!"
    val newBottonInstruction = "You can use the points to claim free vouchers by heading to voucher market."

    var userPoints by remember { mutableStateOf(0) }

    val context = LocalContext.current

    val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
    val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

    val firestore = FirebaseFirestore.getInstance()
    val documentRef = firestore.collection("GivnGoAccounts")
        .document(userAccountType)
        .collection("Donor")
        .document(userCurrentSignedInEmail)

    // Set up a snapshot listener to get real-time updates
    DisposableEffect(Unit) {
        val listenerRegistration = documentRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("Firestore", "Error listening for document changes", error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val currentPoints = (snapshot.getLong("donor_account_points") ?: 0L).toInt()
                userPoints = currentPoints
            }
        }

        // Clean up the listener when CardPoints is removed from composition
        onDispose {
            listenerRegistration.remove()
        }
    }

    Box(
        modifier = Modifier
            .height(210.dp)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
            .background(
                color = Color(0xFFF6F5FF),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = "My Points",
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(start = 8.dp),
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (userPoints != 0) oldUser else newUser,
                modifier = Modifier
                    .width(160.dp)
                    .padding(start = 8.dp),
                fontSize = 16.sp,
                color = Color(0xFF7469CB),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = if (userPoints != 0) newBottonInstruction else bottomInstructions,
                fontSize = 10.sp,
                modifier = Modifier
                    .width(160.dp)
                    .padding(start = 8.dp),
                color = Color(0xFF7469CB),
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
        ) {
            if (userPoints != 0) {
                Image(
                    painter = painterResource(id = R.drawable.ic_glowcircle_active),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_glowcircle),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .graphicsLayer { this.alpha = 0.25f },
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = "$userPoints",
                fontSize = 38.sp,
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}



@Composable
fun HorizontalLine() {
    Box( 
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp) 
            .height(3.dp) 
            .background(
                color = Color.Black.copy(alpha = 0.08f),
                shape = RoundedCornerShape(25.dp)
            )
    )
}

@Composable
fun currentSelectedCategory(categoryN: String) {
    Column {
        Text(
            text = "$categoryN",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8070F6),
            modifier = Modifier
                .padding(start = 25.dp)
                .width(230.dp),
            textAlign = TextAlign.Left
        )
    }
}


@Composable
fun AssignTargetButton() {
  
    Button(
        onClick = { /* Create another card */ },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFF6F5FF) 
        ),
        modifier = Modifier
            .padding(start = 28.dp)
            .height(55.dp) 
            .width(220.dp),
        shape = RoundedCornerShape(25.dp) 
    ) {
       
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🎯 Set a Target points", 
                fontSize = 15.sp,
                color = Color(0xFF8070F6),
            fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CategoryCardSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 25.dp)
        ) {

            CategoryCards("Meals", R.drawable.ic_meals_category, selectedCategory, onCategorySelected)
            CategoryCards("Clothings", R.drawable.ic_clothings_category, selectedCategory, onCategorySelected)
            CategoryCards("Furnitures", R.drawable.ic_electronics_category, selectedCategory, onCategorySelected)
        }
    }

}




@Composable
fun CategoryButtons(selectedCategory: String) {
val context = LocalContext.current

var slideOffset by remember { mutableStateOf(1f) }

    val enterTransition = slideInVertically(
        initialOffsetY = { height -> height },
        animationSpec = tween(durationMillis = 300)
    )

    val exitTransition = slideOutVertically(
        targetOffsetY = { height -> height },
        animationSpec = tween(durationMillis = 300)
    )

    val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
    onResult = { result -> 
        // Handle the result from the launched activity
    }
)


if(selectedCategory == "Meals"){

AnimatedVisibility(
            visible = true,
            enter = enterTransition,
            exit = exitTransition
        ) {
        
Row(
        verticalAlignment = Alignment.CenterVertically, // Aligns items in the center vertically
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Button(
            onClick = { 
            
             val intent = Intent(context, postDonation::class.java)
                launcher.launch(intent)
                
                },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF6F5FF)
            ),
            modifier = Modifier
                .height(35.dp)
                .padding(start = 30.dp, end = 8.dp)
                .wrapContentWidth(), // Adjusts to wrap the content
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_createpost),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Post a donation",
                    fontSize = 10.sp,
                    modifier = Modifier
                        .padding(start = 2.dp),
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Button(
            onClick = {
             val intent = Intent(context, postHistory::class.java).apply{
             putExtra("donation_category_meals", "Meals")
             }
                 context.startActivity(intent) 
                },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF6F5FF)
            ),
            modifier = Modifier
                .height(35.dp)
                .padding(end = 30.dp) 
                .wrapContentWidth(),
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_posthistory),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Donation history",
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
}else if (selectedCategory == "Clothings"){
Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Button(
            onClick = { 
            
             val intent = Intent(context, postDonation::class.java)
                launcher.launch(intent)
                
                },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF6F5FF)
            ),
            modifier = Modifier
                .height(35.dp)
                .padding(start = 30.dp, end = 8.dp)
                .wrapContentWidth(), 
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_createpost),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Post a donation",
                    fontSize = 10.sp,
                    modifier = Modifier
                        .padding(start = 2.dp),
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Button(
            onClick = {
             val intent = Intent(context, postHistory::class.java).apply{
             putExtra("donation_category_meals", "Clothings")
             }
                 context.startActivity(intent) 
                },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF6F5FF)
            ),
            modifier = Modifier
                .height(35.dp)
                .padding(end = 30.dp)
                .wrapContentWidth(),

            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_posthistory),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Donation history",
                    fontSize = 10.sp,
                    modifier = Modifier
                        .padding(start = 2.dp),
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}else if ( selectedCategory == "Furnitures"){
Row(
        verticalAlignment = Alignment.CenterVertically, 
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Button(
            onClick = { 
            
             val intent = Intent(context, postDonation::class.java)
                launcher.launch(intent)
                
                },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF6F5FF)
            ),
            modifier = Modifier
                .height(35.dp)
                .padding(start = 30.dp, end = 8.dp)
                .wrapContentWidth(), // wrap the content
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_createpost),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Post a donation",
                    fontSize = 10.sp,
                    modifier = Modifier
                        .padding(start = 2.dp),
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Button(
            onClick = {
             val intent = Intent(context, postHistory::class.java).apply{
             putExtra("donation_category_meals", "Furnitures")
             }
                 context.startActivity(intent) 
                },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFF6F5FF)
            ),
            modifier = Modifier
                .height(35.dp)
                .padding(end = 30.dp) 
                .wrapContentWidth(), 
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_posthistory),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Donation history",
                    fontSize = 10.sp,
                    modifier = Modifier
                        .padding(start = 2.dp),
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}else{
            Spacer(modifier = Modifier.height(22.dp))
            DotProgressIndicator(modifier = Modifier.size(60.dp, 30.dp))
}
    
}



@Composable
fun CategoryCards(
    title: String,
    drawableResId: Int?,
    selectedCategory: String, 
    onCategorySelected: (String) -> Unit
) {
    val textColor = if (selectedCategory == title) Color(0xFF8070F6) else Color(0xFF8070F6)    
    val boldText = if (selectedCategory == title) FontWeight.Bold else FontWeight.Normal

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Align contents to center horizontally
        modifier = Modifier
            .padding(top = 20.dp) // Add top padding for the entire component
    ) {
        // Box with image
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable { onCategorySelected(title) }
                .size(100.dp)
                .padding(6.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            if (drawableResId != null) {
                Image(
    painter = painterResource(id = drawableResId), // Load drawable here
    contentDescription = title,
    modifier = Modifier
        .matchParentSize()
        .padding(2.dp)
        .clip(RoundedCornerShape(30.dp)),
    contentScale = ContentScale.Crop
)
            } else {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = Color(0xFF8070F6),
                            shape = RoundedCornerShape(30.dp)
                        )
                )
            }
        }

        // Text outside the Box, positioned at the bottom
        Text(
            text = title,
            fontSize = 11.sp,
            color = textColor,
            fontWeight = boldText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 3.dp) // Space between image and text
        )
    }
}


@Composable
fun DeliveryJourneySection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Begin your delivery journey",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DeliveryButton("Available deliveries")
            DeliveryButton("My Routes")
        }
    }
}



data class DonorDetails(
    val organizationName: String = "",
    val statusAccount: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val uriProfileImage: String = ""
)



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
                modifier = Modifier.align(Alignment.Start).height(45.dp).padding(start = 30.dp).clip(RoundedCornerShape(30.dp))
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





@Composable
fun DrawerItem(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFEFEDFF) else Color.Transparent

 val textColor = if (isSelected) Color(0xFF463C96) else Color(0xFF917BFF)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(start = 16.dp, end = 5.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 20.dp,top = 12.dp, bottom = 12.dp),
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}




/*

RECIPIENT SIDE FLOW

*/


@Composable
fun MyAppRecipient(userFinishBasic: String) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val navigationItems = listOf(
        MainNavRecipient.Home,
        MainNavRecipient.Donations,
        MainNavRecipient.Schedules
    )
    
    

    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val activity = (context as MainActivity)
    
    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.Transparent.toArgb() 
    
WindowCompat.getInsetsController(activity.window, activity.window.decorView)?.isAppearanceLightStatusBars = false
}

    Box(
        modifier = Modifier
            .fillMaxSize()
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
            topBar = { TopBar{ coroutineScope.launch { scaffoldState.drawerState.open() } } },
            drawerContent = {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .clip(RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)) 
            .background(Color.White, shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
            .padding(end=25.dp)
    ) {
        AppDrawer(
        userFinishBasic,
            onItemClicked = {
                coroutineScope.launch { scaffoldState.drawerState.close() }
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
                            color = Color.Black, // Shadow color
                            alpha = 0.1f, // Shadow alpha
                            borderRadius = 30.dp, // Rounded corners
                            blurRadius = 2.dp, // Blur radius
                            offsetY = 5.dp // Y offset for the shadow
                        )
                        .background(Color(0xFFFAF9FE), shape = RoundedCornerShape(23.dp))
                        .padding(top = 6.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center, // Centers items vertically within the Column
                        horizontalAlignment = Alignment.CenterHorizontally // Centers items horizontally within the Column
                    ) {
                        AnimatedBottomBar(
                            selectedItem = selectedItem,
                            itemSize = navigationItems.size,
                            indicatorDirection = IndicatorDirection.BOTTOM,
                            containerColor = Color.Transparent,
                            indicatorColor = Color(0xFF8070F6),
                            indicatorStyle = IndicatorStyle.DOT,
                            modifier = Modifier
                                .padding(bottom = 8.dp) // Add bottom padding here
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
                                   modifier = Modifier
                                    .padding(bottom = 8.dp), 
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
        NavHost(
            navController = navController,
            startDestination = MainNavRecipient.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainNavRecipient.Home.route) { HomeScreenRecipient(navController, setSelectedItem = { selectedItem = 0 }) }
            composable(MainNavRecipient.Donations.route) { BrowseDonations() }
            composable(MainNavRecipient.Schedules.route) { mySchedules() }
        }
        }
    }
}

sealed class MainNavRecipient(val route: String, val iconResId: Int, val title: String) {
    object Home : MainNavRecipient("Home", R.drawable.ic_homeheart, "Home")
    object Donations : MainNavRecipient("Donations", R.drawable.ic_donationpackage, "Donations")
    object Schedules : MainNavRecipient("Schedules", R.drawable.ic_deliveries, "My Schedules")
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenRecipient(navController: NavController, setSelectedItem: (Int) -> Unit) {
        
          var selectedCategory by remember { mutableStateOf("Meals") }
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()

    val maxPadding = 100.dp
    val paddingTop = derivedStateOf {
        val paddingPx = with(density) { maxPadding.toPx() - scrollState.value }
        paddingPx.coerceIn(0f, with(density) { maxPadding.toPx() })
    }.value
    
    userProfileAlignment()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = with(density) { paddingTop.toDp() }) // Apply padding directly
            .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
            .overScroll(overscrollEffect = pushDownOverscrollEffect
      )
            .background(color = Color.White)
    ) {
        GreetingSection()
        Spacer(modifier = Modifier.height(20.dp))
        topCategoryRecipient()
        Spacer(modifier = Modifier.height(8.dp))
        CarouselRow()
        Spacer(modifier = Modifier.height(18.dp))
        HorizontalLine()
        Spacer(modifier = Modifier.height(18.dp))
        BottomHeadline()
        Spacer(modifier = Modifier.height(18.dp))
        RecipientBrowseOptions(navController, setSelectedItem)  
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(color = Color.Transparent)
        )
    }
}

@Composable
fun RecipientBrowseOptions(navController: NavController, setSelectedItem: (Int) -> Unit) {
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
                        navController.navigate(MainNavRecipient.Donations.route)
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
                        text = "Browse donations",
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
                    val intent = Intent(context, ClaimedDonations::class.java)
                context.startActivity(intent)
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
                        text = "Claimed Donations",
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

@Composable
fun CarouselRow() {
    Box(
        modifier = Modifier.fillMaxWidth(), // Fill the width of the screen
        contentAlignment = Alignment.CenterStart // Align content to start but centered horizontally
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 25.dp)
        ) {
           CarouselExplores(
                title = "13 Ways to Donate",
                width = 230.dp,
                drawableResId = R.drawable.ic_homeheart,
                websiteUrl = "https://www.usatoday.com/story/money/personalfinance/budget-and-spending/2017/10/27/13-ways-give-charity-without-breaking-your-budget/792720001/",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYh5tt4OajOwQouFN0hBn-J66QrREp-_Gm43NKG-MKiovxJZ0Q8CeLlIQ&s=10"
            )
            CarouselExplores(
                title = "Fight Hunger",
                width = 200.dp,
                drawableResId = R.drawable.ic_homeheart,
                websiteUrl = "https://muslimi.com/food-donation-how-giving-surplus-food-can-help-fight-hunger-and-reduce-waste/",
                imageUrl = "https://muslimi.com/wp-content/uploads/2023/04/da.jpg"
            )
        }
    }
}

@Composable
fun CarouselExplores(
    title: String,
    drawableResId: Int,
    width: Dp,
    websiteUrl: String,
    imageUrl: String // Using a URL for async image loading
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Box with async image as background
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .clickable { /* add intent to open based on the selected title */ }
                .height(210.dp)
                .width(width)
                .padding(6.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            // Async image loading
            AsyncImage(
                model = imageUrl, // Load image from URL
                contentDescription = title,
                modifier = Modifier
                    .matchParentSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )

            // Overlaying the second image and text at the bottom start
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp) // Padding at bottom and start
            ) {
                // Second image (text icon)
                Image(
                    painter = painterResource(id = drawableResId), // Replace with your icon resource
                    contentDescription = "Text icon",
                    modifier = Modifier
                        .size(20.dp),
                    contentScale = ContentScale.Crop
                )

                // Title text
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color(0xFF8070F6),
                    modifier = Modifier
                        .padding(start = 4.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
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

// Data model with a single thumbnail URI
data class DonationPost(
    val documentId: String,
    val title: String,
    val description: String,
    val donor: String,
    val donor_email: String,
    val image_thumbnail: Uri?,
    val images_donation: List<Uri>
)

@Composable
fun BrowseDonations() {
    var selectedPost by remember { mutableStateOf<DonationPost?>(null) }
    val firestore = FirebaseFirestore.getInstance()
    val isError = remember { mutableStateOf(false) }
    val storage = FirebaseStorage.getInstance()
    val context = LocalContext.current

    val myDonations = remember { mutableStateListOf<DonationPost>() }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        firestore.collection("GivnGoPublicPost")
            .document("DonationPosts")
            .collection("Posts")
            .orderBy("donation_timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val donations = documents.mapNotNull { doc ->
                    val data = doc.data
                    val documentId = doc.id
                    val claimStatus = data["donation_claim_status"] as? String ?: ""

                    if (claimStatus == "Unclaimed") {
                        DonationPost(
                            documentId = documentId,
                            title = data["donation_post_title"] as? String ?: "No Title",
                            description = data["donation_post_description"] as? String ?: "No Description",
                            donor = data["donor_name"] as? String ?: "Unknown Donor",
                            donor_email = data["donor_email"] as? String ?: "Unknown Email",
                            image_thumbnail = null,
                            images_donation = emptyList()
                        )
                    } else null
                }

                myDonations.addAll(donations)

                donations.forEachIndexed { index, donation ->
                    val formattedEmail = donation.donor_email.replace(".", "_")
                    val formattedTitle = donation.title.replace(" ", "_")
                    val storagePath = "DonationPost/$formattedEmail/$formattedTitle"

                    // Retrieve thumbnail image
                    fetchImageUri(storage, "$storagePath/thumbnail_image", context) { uri ->
                        uri?.let {
                            myDonations[index] = myDonations[index].copy(image_thumbnail = uri)
                        }
                    }

                    // Retrieve additional images (up to 5)
                    val storedImages = mutableListOf<Uri>()
                    for (i in 0..4) {
                        fetchImageUri(storage, "$storagePath/image_$i", context) { uri ->
                            uri?.let {
                                storedImages.add(uri)
                                myDonations[index] = myDonations[index].copy(images_donation = storedImages)
                            }
                        }
                    }
                }

                isError.value = donations.isEmpty()
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
        browseDonationHeadline()

        // Search bar
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
                        .padding(start = 12.dp, top = 8.dp)
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
                                text = "Search available donations...",
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

        if (isError.value) {
            // Display error message if no donations are available
            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                LottieAnimationFromUrl("https://lottie.host/eb406c2a-ca40-4dcf-9539-b14f28cc6bed/DR1tL8FJr8.json")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No donations available",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8070F6)
                )
            }
        } else {
            // Filter donations based on the search query
            val filteredDonations = if (searchQuery.isEmpty()) {
                myDonations
            } else {
                myDonations.filter { it.title.contains(searchQuery, ignoreCase = true) }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredDonations) { donation ->
                    PostDisplayModel(
                        userDonationPost = donation.title,
                        imageUrl = donation.image_thumbnail,
                        onClick = { selectedPost = donation }
                    )
                }
            }
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
    fun sendPushNotification(fcmToken: String, title: String, body: String) {
        val url = "https://lt-ruletagalog.vercel.app/api/givngo/trigger"

        val json = JSONObject()
        json.put("fcmToken", fcmToken)
        json.put("title", title)
        json.put("body", body)

        // Send the POST request to the server
        sendPostRequest(url, json)
    }


    // Display CustomDonationDialog when a donation is selected
    selectedPost?.let { donationPost ->
        CustomDonationDialog(
            donationPost = donationPost,
            onDismissRequest = { selectedPost = null },
            onClaimDonation = {
                Log.d("DonationClaim", "Claimed donation with ID: ${donationPost.documentId}")

                val firestore = FirebaseFirestore.getInstance()
                val recipientEmail = SharedPreferences.getEmail(context) ?: "Developer"
                val recipientFirstNamw = SharedPreferences.getFirstName(context) ?: "Developer"
                val claimedData = mutableMapOf<String, Any>(
                    "documentId" to donationPost.documentId,
                    "title" to donationPost.title,
                    "description" to donationPost.description,
                    "donor" to donationPost.donor,
                    "donor_email" to donationPost.donor_email,
                    "image_thumbnail" to (donationPost.image_thumbnail?.toString() ?: ""),
                    "images_donation" to donationPost.images_donation.map { it.toString() }
                )

                try {
                    // Update `donation_claim_status` to "Claimed"
                    firestore.collection("GivnGoPublicPost")
                        .document("DonationPosts")
                        .collection("Posts")
                        .document(donationPost.documentId ?: "defaultDocId")
                        .update("donation_claim_status", "Claimed")
                        .addOnSuccessListener {
                            // Get the donor’s FCM token and notify the backend
                            firestore.collection("GivnGoAccounts")
                                .document("BasicAccounts")
                                .collection("Donor")
                                .document(donationPost.donor_email)
                                .get()
                                .addOnSuccessListener { donorDoc ->
                                    val fcmToken = donorDoc.getString("fcmToken")
                                    if (fcmToken != null) {
                                        sendPushNotification(fcmToken, "Your donation was claimed!", "Your Donation $donationPost.title was claimed by $recipientFirstNamw")
                                    }
                                }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("FirestoreError", "Failed to update donation status: ", exception)
                            Toast.makeText(context, "Failed to update donation status: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                } catch (e: Exception) {
                    Log.e("FirestoreError", "Unexpected error during status update: ", e)
                    Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
                }

                try {
                    // Store the claimed donation in the user's claimed list
                    firestore.collection("GivnGoAccounts")
                        .document("BasicAccounts")
                        .collection("Recipient")
                        .document(recipientEmail)
                        .collection("MyClaimedDonations")
                        .document(donationPost.documentId ?: "defaultDocId")
                        .set(claimedData)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Donation claimed successfully.")
                            selectedPost = null 
                        }
                        .addOnFailureListener { exception ->
                            Log.e("FirestoreError", "Failed to claim donation: ", exception)
                            Toast.makeText(context, "Failed to claim donation: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                } catch (e: Exception) {
                    Log.e("FirestoreError", "Unexpected error during claiming: ", e)
                    Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}




@Composable
fun CustomDonationDialog(
    donationPost: DonationPost,
    onDismissRequest: () -> Unit,
    onClaimDonation: () -> Unit
) {
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

// This fetch function now handles a single URI for the thumbnail
fun fetchImageUri(
    storage: FirebaseStorage,
    imagePath: String,
    context: Context,
    onUriFetched: (Uri?) -> Unit
) {
    val imageRef = storage.reference.child(imagePath)
    imageRef.downloadUrl
        .addOnSuccessListener { uri ->
            onUriFetched(uri)
        }
        .addOnFailureListener { exception ->
            if ((exception as? StorageException)?.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                Log.d("Storage", "Image at path $imagePath not found.")
                onUriFetched(null)
            } else {
                Log.e("Storage", "Error retrieving image URI", exception)
                onUriFetched(null)
            }
        }
}



@Composable
fun PostDisplayModel(
    userDonationPost: String,
    imageUrl: Uri?,
    onClick: () -> Unit
) {
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .padding(4.dp)
            .clickable { onClick() } // Make clickable to show the dialog
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // AsyncImage at the top
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(180.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(18.dp))
                    ,
                contentScale = ContentScale.Crop,
                onSuccess = {
                    imageLoadState = ImageLoadState.Success
                },
                onError = {
                    imageLoadState = ImageLoadState.Error
                },
                onLoading = {
                    imageLoadState = ImageLoadState.Loading
                }
            )
            // Text aligned at the bottom
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = userDonationPost,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                                .fillMaxWidth(),
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }
                    ImageLoadState.Loading -> {
                        // Loading state UI if needed
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = userDonationPost,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth(),
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun mySchedules() {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("Deliveries") }
    val pickupSchedules = remember { mutableStateListOf<DonationSchedule>() }
    val deliverySchedules = remember { mutableStateListOf<DonationSchedule>() }
    val firestore = FirebaseFirestore.getInstance()

    val isError = remember { mutableStateOf(false) }

    val statusType = "Donor"
    val emailDonor = SharedPreferences.getOrgName(context) ?: "developer@gmail.com"

    LaunchedEffect(Unit) {
        firestore.collection("GivnGoAccounts")
            .document(statusType)
            .collection("MySchedules")
            .document(emailDonor)
            .collection("schedules") // Assuming schedules are stored in this sub-collection
            .get()
            .addOnSuccessListener { querySnapshot ->
                pickupSchedules.clear()
                deliverySchedules.clear()
                for (document in querySnapshot.documents) {
                    val claimStatus = document.getString("donation_schedule_status")
                    val donationPost = DonationSchedule(
                        userClaimedDonation = document.getString("donation_post_title") ?: "",
                        donationDescription = document.getString("donation_post_description") ?: "",
                        packageStatus = document.getString("donation_status") ?: "",
                        imageUri = null // Update this if there’s an image field
                    )

                    // Add to respective lists based on claimStatus
                    if (claimStatus == "Pickup") {
                        pickupSchedules.add(donationPost)
                    } else if (claimStatus == "Delivery") {
                        deliverySchedules.add(donationPost)
                    }
                }
                // Set error state to true if both lists are empty
                isError.value = deliverySchedules.isEmpty() && pickupSchedules.isEmpty()
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

        topCategoryRecipientMySchdules()

        // Category selection row
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 30.dp)
        ) {
            categoryDonationChooser("Deliveries", selectedCategory) { selectedCategory = it }
            categoryDonationChooser("Pickups", selectedCategory) { selectedCategory = it }
        }

        Spacer(modifier = Modifier.height(4.dp))
        HorizontalLine()
        Spacer(modifier = Modifier.height(4.dp))

        // Determine the selected items list based on the category
        val itemsList = if (selectedCategory == "Deliveries") deliverySchedules else pickupSchedules

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
            // Display the LazyRow for the selected items list
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
                           /* val intent = Intent(context, YourTargetActivity::class.java).apply {
                                putExtra("userClaimedDonation", donation.userClaimedDonation)
                                putExtra("donationDescription", donation.donationDescription)
                                putExtra("packageStatus", donation.packageStatus)
                                donation.imageUri?.let { uri ->
                                    putExtra("imageUri", uri.toString())
                                }
                            }
                            context.startActivity(intent) */
                        }
                    )
                }
            }
        }
    }
}




data class DonationSchedule(
    val userClaimedDonation: String,
    val donationDescription: String,
    val imageUri: Uri?,
    val packageStatus: String
)

@Composable
fun displaySchedulesDonationModel(
    userClaimedDonation: String,
    donationDescription: String,
    imageUri: Uri?,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    modifier = Modifier.padding(start = 10.dp)
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


@Composable
fun categoryDonationChooser(categoryType: String, selectedCategory: String?, onCategorySelected: (String) -> Unit) {
    val isButtonPressed = selectedCategory == categoryType

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(28.dp)
            .wrapContentWidth()
            .clickable {
                onCategorySelected(categoryType) 
            }
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = if (isButtonPressed) Color(0xFFFBF8FF) else Color(0xFFE9E7FF),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = categoryType,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 3.dp, bottom = 3.dp, start = 24.dp, end = 24.dp),
                color =  if (isButtonPressed) Color(0xFF483D9B) else Color(0xFF8070F5),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }

    if (isButtonPressed) {
        when (categoryType) {
            "Electronics" -> {
            /*    // Handle Electronics pressed action
                Text(
                    text = "You selected Electronics!",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                ) */
            }
            "Meals" -> {
              /*  // Handle Meals pressed action
                Text(
                    text = "You selected Meals!",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                ) */
            }
            "Clothings" -> {
             /*   // Handle Clothings pressed action
                Text(
                    text = "You selected Clothings!",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                ) */
            }
        }
    }
}


@Composable
fun topCategoryRecipientMySchdules() {

    Column(
        modifier = Modifier.fillMaxWidth(),  // Ensures the column takes the full width
        horizontalAlignment = Alignment.Start // Aligns the content to the start
    ) {
        Text(
            text = "Schedules",
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
fun searchBarDonations(){


}



@Composable
fun browseDonationHeadline() {
val context = LocalContext.current
val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
    onResult = { result -> 
        // Handle the result from the launched activity
    }
)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start // Align content to start in the column
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), // Ensure the Row fills the available width
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Space out the text and image
        ) {
            // Text aligned to center start
            Text(
                text = "Browse donations",
                fontSize = 22.sp,
                color = Color(0xFF8070F6),
                modifier = Modifier
                    .padding(start = 25.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start // Align text to the start within the text block
            )

            // Image aligned to center end
            Image(
    painter = painterResource(id = R.drawable.ic_donationpackage),
    contentDescription = "Back button",
    modifier = Modifier
        .size(60.dp)
        .padding(end = 25.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = Color.Gray, bounded = false) // Circular ripple
        ) {
            val intent = Intent(context, ClaimedDonations::class.java)
            launcher.launch(intent)
        }
)
        }
    }
}

@Composable
fun topCategoryRecipient() {

    Column(
        modifier = Modifier.fillMaxWidth(),  // Ensures the column takes the full width
        horizontalAlignment = Alignment.Start // Aligns the content to the start
    ) {
        Text(
            text = "Discover your support!",
            fontSize = 20.sp,
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
fun CategoryHeadline() {
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Explore!",
            fontSize = 23.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
            .padding(start = 25.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BottomHeadline() {
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Get Started by browsing donations!",
            fontSize = 18.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
            .padding(start = 25.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}









@Composable
fun DeliveryButton(text: String) {
    Button(onClick = { /* Handle button click */ }) {
        Text(text = text)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyComposeApplicationTheme {
        MyApp()
    }
}


