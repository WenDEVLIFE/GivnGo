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

import java.text.SimpleDateFormat
import java.util.Locale

import com.google.firebase.Timestamp

import androidx.compose.foundation.lazy.itemsIndexed
import com.google.firebase.storage.StorageException
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.foundation.lazy.LazyColumn

class postHistory : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
            val categoryType = intent.getStringExtra("donation_category_meals") ?: "All"
                
                History(categoryType)
            }
        }
    }
}


@Composable
fun History(categories: String) {
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as postHistory)

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
                TopBarHistoru(onMenuClick = { activity.finish() }, isScrolled = scrollState.value > 0)
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
             Spacer(modifier = Modifier.height(32.dp))
                searchBarMyDonationList(categories)
                
            }
        }
    }
}


@Composable
fun TopBarHistoru(onMenuClick: () -> Unit, isScrolled: Boolean) {
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
            text = "History Donation",
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(bottom = 2.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/*
firestore.collection("GivnGoAccounts")
                    .document("DeveloperAccess")
                    .collection("Recipient")
                    .document(recipientEmail)
                    .collection("MyClaimedDonations")
                    .document(donationPost.documentId ?: "defaultDocId")
                    .set(claimedData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Donation claimed successfully.")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FirestoreError", "Failed to claim donation: ", exception)
                    }
                    */




@Composable
fun searchBarMyDonationList(categorySelected: String) {
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
        firestore.collection("GivnGoAccounts")
    .document("BasicAccounts")
    .collection("Donor")
    .document(userCurrentSignedInEmail)  // Use the signed-in user email here
    .collection("Donation_Post")
    .document("Categories")
    .collection(categorySelected)
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
        .padding(start = 30.dp, top = 26.dp, end = 30.dp)
        .background(color = Color.White.copy(alpha = 0.10f), shape = RoundedCornerShape(28.dp)),
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
                .heightIn(min = 80.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFAF9FF))
        ) {
        
        Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(start = 12.dp, top = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically)
             {
                AsyncImage(
                    model = image_thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .padding(start = 12.dp,top = 8.dp, bottom = 8.dp)
                        .height(120.dp)
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
                        fontSize = 14.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                   Text(
                        text = yearPosted,
                        fontSize = 12.sp,
                        color = Color(0xFFA498FC),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    )
                }
                
                
            }
                
                }
                
            
            
        }
    }
}


