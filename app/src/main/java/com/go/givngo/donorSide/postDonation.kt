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
import java.util.UUID

class postDonation : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
                MyApp()
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostDonationScreen() {
    
}

@Composable
fun TopBar(onMenuClick: () -> Unit, isScrolled: Boolean) {
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
            text = "Post a donation",
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(bottom = 2.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun editTextsinglineLine(
    heightNumber: Dp,
    placeHolderTitle: String,
    titleDonation: String, // Initial value from the parent
    onTextValueChange: (String) -> Unit // Callback to notify the parent about changes
) {
    // State to hold the current text input value
    var textState by remember { mutableStateOf(titleDonation) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .height(heightNumber)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xFFFBF8FF),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.CenterStart // Align content to the top start
    ) {
        if (textState.isEmpty()) {
            Text(
                text = placeHolderTitle,
                color = Color.Black.copy(alpha = 0.90f),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // BasicTextField for user input
        BasicTextField(
            value = textState,
            onValueChange = { newValue ->
                textState = newValue // Update local state
                onTextValueChange(newValue) // Notify parent of the new value
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            textStyle = TextStyle(
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            ),
            singleLine = true // Restrict to a single line
        )
    }
}

@Composable
fun editTextMultipleLine(
    placeHolderTitle: String,
    descriptionInfo: String,
    descriptionDonation: (String) -> Unit
) {
    var textState by remember { mutableStateOf(descriptionInfo) }

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


@Composable
fun dropdownAddressSelector() {
    // State to control the visibility of the dropdown
    var isDropdownVisible by remember { mutableStateOf(false) }
    // State to check if an address has been pressed
    var isAddressPressed by remember { mutableStateOf(false) }
    // State to hold the selected address, defaults to "select/add address"
    var selectedAddress by remember { mutableStateOf("Select/Add address") }

    Column {
        // Box that toggles the visibility of the dropdown
        Box(
            modifier = Modifier
                .padding(8.dp)
                .height(40.dp)
                .fillMaxWidth()
                .clickable { 
                    isDropdownVisible = !isDropdownVisible 
                }
                .clip(RoundedCornerShape(16.dp))
                .background(
                    color = Color(0xFFFBF8FF),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            // Location Icon
            Image(
                painter = painterResource(id = R.drawable.ic_location_two),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(20.dp),
                contentScale = ContentScale.Crop
            )
            // Text that shows the selected address
            Text(
                text = selectedAddress,
                fontSize = if (isAddressPressed) 12.sp else 14.sp,
                color = if (isAddressPressed) Color(0xFF8070F6) else Color.Black.copy(alpha = 0.90f),
                fontWeight = if (isAddressPressed) FontWeight.Bold else FontWeight.Normal,
                modifier = if (isAddressPressed) Modifier.padding(start = 48.dp)
                else Modifier.padding(start = 110.dp)
            )
        }

        // Show the dropdown if it's visible
        if (isDropdownVisible) {
            AddressDropdown(onAddressSelected = { address ->
                selectedAddress = address
                isDropdownVisible = false // Close dropdown after selection
                isAddressPressed = true // Indicate that an address has been pressed
            })
        }
    }
}


@Composable
fun AddressDropdown(onAddressSelected: (String) -> Unit) {
val context = LocalContext.current

    // Address list or form that shows when the dropdown is visible
    val addressPrimary = SharedPreferences.getAdd(context) ?: "None"
    
    Box(
        modifier = Modifier
            .padding(8.dp)
            .heightIn(min = 40.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xFFFBF8FF),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            // Address option 1 child referenece from firebase user donor address
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAddressSelected(addressPrimary) }
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_location_one),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp).align(Alignment.Top),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = addressPrimary,
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 8.dp,bottom = 8.dp)
                        .wrapContentHeight()
                )
            }

           
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAddressSelected("add more address") }
                    .padding(start = 70.dp, top = 12.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_addlocation),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Add more address",
                    color = Color.Black.copy(alpha = 0.90f),
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 4.dp,top = 2.dp)
                        .wrapContentHeight()
                )
            }
        }
    }
}

@Composable
fun editTextDonationQuantity(
    donationAmountQuantity: String, // Pass the initial value as a String
    onAmountChange: (String) -> Unit // Callback for when the donation amount changes
) {
    // State to hold the current text input value
    var textState by remember { mutableStateOf(donationAmountQuantity) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .height(40.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xFFFBF8FF),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.CenterStart // Align content to the top start
    ) {
        if (textState.isEmpty()) {
            Text(
                text = "Donation Quantity: ",
                color = Color.Black.copy(alpha = 0.90f),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // BasicTextField for user input, allowing only numbers
        BasicTextField(
            value = textState,
            onValueChange = { newValue ->
                textState = newValue // Update local state
                onAmountChange(newValue) // Notify parent of the new value
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            textStyle = TextStyle(
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            ),
            singleLine = true
        )
    }
}

@Composable
fun MyApp() {
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as postDonation)

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
                TopBar(onMenuClick = { activity.finish() }, isScrolled = scrollState.value > 0)
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
                var donationTitlePost by remember { mutableStateOf("") }
                var donationDescription by remember { mutableStateOf("") }
                var donationQuantity by remember { mutableStateOf("") }
                var selectedYear by remember { mutableStateOf("yyyy") }
                var selectedMonth by remember { mutableStateOf("mm") }
                var selectedDay by remember { mutableStateOf("dd") }
                var selectedCategory by remember { mutableStateOf<String?>(null) }
                    var selectedDeliveryOption by remember { mutableStateOf<String?>(null) }
                var imageUris by remember { mutableStateOf(listOf<Uri>()) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .background(color = Color.White)
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(2.dp))

                    editTextsinglineLine(
                        heightNumber = 40.dp,
                        "Add donation title",
                        donationTitlePost,
                        { donationTitlePost = it }
                    )

                    editTextMultipleLine(
                        "Add description",
                        donationDescription,
                        { donationDescription = it }
                    )

                    dropdownAddressSelector()

                    editTextDonationQuantity(
                        donationAmountQuantity = donationQuantity,
                        { donationQuantity = it }
                    )

                    editTextExpiration(
                        selectedYear = selectedYear,
                        selectedMonth = selectedMonth,
                        selectedDay = selectedDay,
                        onYearSelected = { selectedYear = it },
                        onMonthSelected = { selectedMonth = it },
                        onDaySelected = { selectedDay = it }
                    )

                    categoryHeadline()

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        categoryDonationChooser("Meals", selectedCategory) { selectedCategory = it }
                        categoryDonationChooser("Clothings", selectedCategory) { selectedCategory = it }
                        categoryDonationChooser("Furnitures", selectedCategory) { selectedCategory = it }
                        categoryDonationChooser("Books", selectedCategory) { selectedCategory = it }
                        categoryDonationChooser("Stationery", selectedCategory) { selectedCategory = it }
                        categoryDonationChooser("Households items", selectedCategory) { selectedCategory = it }
                    }
                    
                    deliveryoptionHeadline()
                    
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        deliveryCategoryOptionChooser("Pick-Up", selectedDeliveryOption) { selectedDeliveryOption = it }
                        deliveryCategoryOptionChooser("Self Delivery", selectedDeliveryOption) { selectedDeliveryOption = it }
                        deliveryCategoryOptionChooser("GivnGo Rider", selectedDeliveryOption) { selectedDeliveryOption = it }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // UploadPhotos composable
                    UploadPhotos(
                        selectedImageUris = imageUris,
                        onImageUrisChanged = { newUris -> 
                            imageUris = newUris
                        }
                    )

                    Spacer(modifier = Modifier.height(60.dp))
                }
                
                // Align the overlay shadow at the bottom of the screen.
                overlayShadowBottom(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    scaffoldState = scaffoldState,       // Pass scaffoldState here
                    coroutineScope = coroutineScope,     // Pass coroutineScope here
                    firebaseDonationPostTitle = donationTitlePost,
                    firebaseDonationPostDesc = donationDescription,
                    firebaseDonationCategory = selectedCategory,
                    //Delivery Option
                    firebaseDeliveryMethod= selectedDeliveryOption,
                    firebaseDonationQuantity = donationQuantity,
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    selectedDay = selectedDay,
                    selectedImageUris = imageUris
                )
            }
        }
    }
}


@Composable
fun overlayShadowBottom(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,          // Accept scaffoldState as a parameter
    coroutineScope: CoroutineScope,        // Accept coroutineScope as a parameter
    firebaseDonationPostTitle: String,
    firebaseDonationPostDesc: String,
    firebaseDonationCategory: String?,
    firebaseDeliveryMethod: String?,
    firebaseDonationQuantity: String,
    selectedYear: String,
    selectedMonth: String,
    selectedDay: String,
    selectedImageUris: List<Uri>
) {

    val context = LocalContext.current
    val activity = (context as postDonation)
    val firestore = FirebaseFirestore.getInstance()
    var points by remember { mutableStateOf(0) }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(color = Color.White),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Post",
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(top = 8.dp, end = 16.dp, bottom = 8.dp)
                    .clickable {
                        coroutineScope.launch {
                            val uuid = java.util.UUID.randomUUID()
                            var postIdentification = "POST_POINTS-$uuid"
                            val statusType = "Donor"
                            val statusClaim = "Unclaimed"
                            val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"
                            val userDonorName = SharedPreferences.getOrgName(context) ?: "Developer"
                            val firebaseDonationExpiration = "$selectedMonth/$selectedDay/$selectedYear"
                           var donorPostData = hashMapOf<String, Any?>()
                           
                            // Upload images and get URLs
                            val imageUrls = uploadImagesToStorage(
                                selectedImageUris,
                                userCurrentSignedInEmail,
                                firebaseDonationPostTitle
                            )

                            // Set points based on donation category
                            points = when (firebaseDonationCategory) {
                                "Meals" -> 10
                                "Clothings" -> 6
                                "Furniture" -> 4
                                "Books", "Stationery" -> 2
                                "Household items" -> 8
                                else -> 0
                            }
                            
                            if(firebaseDonationExpiration== "mm/dd/yyyy"){
                            // Prepare donor post data
                             donorPostData = hashMapOf(
                                "donation_post_title" to firebaseDonationPostTitle,
                                "donation_post_description" to firebaseDonationPostDesc,
                                 "donatioon_points_id" to postIdentification,
                                 "donation_points_to_earn" to points,
                                "donor_name" to userDonorName,
                                "donor_email" to userCurrentSignedInEmail,
                                "donation_images" to imageUrls,
                                "donation_category" to (firebaseDonationCategory ?: "Meals"),
                                "delivery_method" to (firebaseDeliveryMethod ?: "GivnGo Rider"),
                                "donation_quantity" to firebaseDonationQuantity,
                                                                "donation_points" to points,
                                                                "claimed by - " to "",
                                "donation_expiration_package" to firebaseDonationExpiration,
                                "donation_claim_status" to statusClaim,
                                "donation_timestamp" to FieldValue.serverTimestamp()
                            )

                            }else{
                            // Prepare donor post data
                             donorPostData = hashMapOf(
                                "donation_post_title" to firebaseDonationPostTitle,
                                "donation_post_description" to firebaseDonationPostDesc,
                                 "donatioon_points_id" to postIdentification,
                                 "donation_points_to_earn" to points,
                                "donor_name" to userDonorName,
                                "donor_email" to userCurrentSignedInEmail,
                                "donation_images" to imageUrls,
                                "donation_category" to (firebaseDonationCategory ?: "Meals"),
                                "donation_quantity" to firebaseDonationQuantity,
                                                                "delivery_method" to (firebaseDeliveryMethod ?: "GivnGo Rider"),
                                                                "donation_points" to points,
                                "donation_expiration_package" to "None",
                                "donation_claim_status" to statusClaim,
                                "donation_timestamp" to FieldValue.serverTimestamp()
                            )

                            }


                            
                            val donorPostDataWithPoints = hashMapOf(
                                "donation_post_title" to firebaseDonationPostTitle,
                                "donation_post_points_identification" to postIdentification,
                                "donation_category" to (firebaseDonationCategory ?: "Meals"),
                                "donation_points_for_this_post" to points,
                                "donation_points_to_claim" to 0
                            )

                            // Save to Firestore with points and show snackbar
                            try {
                                val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"

                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection(statusType)
                                    .document(userCurrentSignedInEmail)
                                    .collection("MyPoints")
                                    .document("Post")
                                    .collection("Unclaimed_Points")
                                    .document(postIdentification)
                                    .set(donorPostDataWithPoints)
                                    .addOnSuccessListener {
                                     /*   coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "You received $points donation points! Go to donation category to claim it!"
                                            )
                                        }*/

                                        firebaseDonationCategory?.let { category ->
    firestore.collection("GivnGoAccounts")
        .document("BasicAccounts")
        .collection(statusType)
        .document(userCurrentSignedInEmail)
        .collection("Donation_Post")
        .document("Categories")
        .collection(category)
                                            .add(donorPostData)
                                            .addOnSuccessListener {
                                                firestore.collection("GivnGoPublicPost")
                                                    .document("DonationPosts")
                                                    .collection("Posts")
                                                    .add(donorPostData)
                                                    .addOnSuccessListener {
                                                        activity.finish()
                                                    }
                                                    .addOnFailureListener { exception ->
                                                        coroutineScope.launch {
                                                            scaffoldState.snackbarHostState.showSnackbar(
                                                                "Error saving post. Check your internet connection and try again."
                                                            )
                                                        }
                                                        Log.e("Firestore", "Error saving public donation post", exception)
                                                    }
                                            }
                                            .addOnFailureListener { exception ->
                                                coroutineScope.launch {
                                                    scaffoldState.snackbarHostState.showSnackbar("Error saving post.")
                                                }
                                                Log.e("Firestore", "Error saving donation post", exception)
                                            }
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                "Error: ${exception.message}. Report issue?"
                                            )
                                        }
                                        reportErrorByEmail(context, exception.message)
                                    }
                            } catch (e: Exception) {
                                Log.e("Firestore", "Exception saving data", e)
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar("An error occurred. Try again.")
                                }
                            }
                        }
                    },
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End
            )
    }
}

fun reportErrorByEmail(context: Context, errorMessage: String?) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822"
        putExtra(Intent.EXTRA_EMAIL, arrayOf("myfavoritemappingswar@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, "Error Report - GivnGo App")
        putExtra(Intent.EXTRA_TEXT, "Error details:\n$errorMessage")
    }
    context.startActivity(Intent.createChooser(intent, "Send Email"))
}


suspend fun uploadImagesToStorage(
    imageUris: List<Uri>,
    userEmail: String,
    donationTitle: String
): Pair<String?, List<String>> {
    val storage = FirebaseStorage.getInstance()
    val formattedEmail = userEmail.replace(".", "_")
    val formattedTitle = donationTitle.replace(" ", "_")

    // Separate thumbnail and other images
    val thumbnailUri = imageUris.firstOrNull()
    val otherImageUris = if (imageUris.size > 1) imageUris.drop(1) else emptyList()

    // Upload thumbnail
    val thumbnailUrl = thumbnailUri?.let { uri ->
        val thumbnailRef = storage.reference.child("DonationPost/$formattedEmail/$formattedTitle/thumbnail_image")
        thumbnailRef.putFile(uri).await()
        thumbnailRef.downloadUrl.await().toString()
    }

    // Upload other images
    val otherImageUrls = mutableListOf<String>()
    for ((index, uri) in otherImageUris.withIndex()) {
        val imageRef = storage.reference.child("DonationPost/$formattedEmail/$formattedTitle/image_$index")
        imageRef.putFile(uri).await()
        otherImageUrls.add(imageRef.downloadUrl.await().toString())
    }

    return Pair(thumbnailUrl, otherImageUrls)
}


@Composable
fun editTextExpiration(
    selectedYear: String,
    selectedMonth: String,
    selectedDay: String,
    onYearSelected: (String) -> Unit,
    onMonthSelected: (String) -> Unit,
    onDaySelected: (String) -> Unit
) {
    val years = (2020..2030).map { it.toString() } // List of years
    val months = (1..12).map { it.toString().padStart(2, '0') } // List of months
    val days = (1..31).map { it.toString().padStart(2, '0') } // List of days

    Box(
        modifier = Modifier
            .padding(8.dp)
            .height(40.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xFFFBF8FF),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Expiration date: ",
                color = Color.Black.copy(alpha = 0.90f),
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.width(4.dp))

            // Year Selector
            DropdownSelector(
                label = selectedYear,
                options = years,
                onOptionSelected = onYearSelected
            )

            Text(
                text = "/",
                color = Color.Black.copy(alpha = 0.90f),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 2.dp)
            )

            // Month Selector
            DropdownSelector(
                label = selectedMonth,
                options = months,
                onOptionSelected = onMonthSelected
            )

            Text(
                text = "/",
                color = Color.Black.copy(alpha = 0.90f),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 2.dp)
            )

            // Day Selector
            DropdownSelector(
                label = selectedDay,
                options = days,
                onOptionSelected = onDaySelected
            )
        }
    }
}


@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .height(40.dp)
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .clickable { expanded = true }
            .padding(top = 10.dp)
    ) {
        Text(text = label, color = Color.Black.copy(alpha = 0.90f), fontWeight = FontWeight.Normal)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(text = option)
                }
            }
        }
    }
}

@Composable
fun categoryHeadline(){

Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Choose Donation Category type:",
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 8.dp),
            color = Color(0xFF8070F6),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
    
}


@Composable
fun deliveryoptionHeadline(){

Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Choose Delivery Method:",
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 8.dp),
            color = Color(0xFF8070F6),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
    
}

@Composable
fun UploadPhotos(
    selectedImageUris: List<Uri>,
    onImageUrisChanged: (List<Uri>) -> Unit // Callback to update image URIs
) {
    var imageCount by remember { mutableStateOf(selectedImageUris.size) }
    val maxImages = 3

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uriList ->
        // Update the image count and list of URIs with the number of selected images
        val newUris = uriList.take(maxImages)
        onImageUrisChanged(newUris) // Update the URIs through the callback
        imageCount = newUris.size
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        // Conditionally render the main image picker only if the maxImages limit is not reached
        if (imageCount < maxImages) {
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .height(260.dp)
                    .width(180.dp)
                    .clickable {
                        if (imageCount < maxImages) {
                            launcher.launch("image/*")
                        }
                    }
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        color = Color(0xFFFBF8FF),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_createpost),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Display selected images inside the same row
        selectedImageUris.forEach { uri ->
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .height(260.dp)
                    .width(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        color = Color(0xFFFBF8FF),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Image painter for displaying the selected image
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay delete icon
                IconButton(
                    onClick = {
                        val updatedUris = selectedImageUris.filter { it != uri }
                        onImageUrisChanged(updatedUris) // Update the URIs through the callback
                        imageCount = updatedUris.size
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(40.dp)
                        .padding(8.dp) // Padding for the delete icon
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_createpost), // Use your delete icon here
                        contentDescription = "Delete Image",
                        tint = Color.Red // Change the tint color if needed
                    )
                }
            }
        }

        // Display image count at the bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "$imageCount/$maxImages",
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 24.dp),
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun categoryDonationChooser(categoryType: String, selectedCategory: String?, onCategorySelected: (String) -> Unit) {
    val isButtonPressed = selectedCategory == categoryType

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(30.dp)
            .wrapContentWidth()
            .clickable {
                onCategorySelected(categoryType) 
            }
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = if (isButtonPressed) Color(0xFFD9C8F9) else Color(0xFFFBF8FF),
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
                color = Color(0xFF8070F6),
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
fun deliveryCategoryOptionChooser(categoryType: String, selectedCategory: String?, onCategorySelected: (String) -> Unit) {
    val isButtonPressed = selectedCategory == categoryType

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(30.dp)
            .wrapContentWidth()
            .clickable {
                onCategorySelected(categoryType) 
            }
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = if (isButtonPressed) Color(0xFFD9C8F9) else Color(0xFFFBF8FF),
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
                color = Color(0xFF8070F6),
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


