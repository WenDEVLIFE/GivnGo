package com.go.givngo.ridersSide;

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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.animation.*
import androidx.compose.runtime.*

import androidx.activity.compose.BackHandler


import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID
import com.go.givngo.MainActivity
import androidx.compose.ui.window.Dialog

import android.util.Log
import com.go.givngo.SharedPreferences
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay


import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import com.google.firebase.messaging.FirebaseMessaging

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

 class RegistrationRiders : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
                MyAppRegistration()
            }
        }
    }
}





@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyAppRegistration() {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var firebasedatabaseProfilePicture: Uri? by remember { mutableStateOf(null) }
    var firebaseUserBio: String by remember { mutableStateOf("") }
    var firebaseUserFirstName: String by remember { mutableStateOf("") }
    var firebaseUserLastName: String by remember { mutableStateOf("") }
    var firebaseUserCompanyAddress: String by remember { mutableStateOf("") }
    
    var firebaseUserVehicleType: String by remember { mutableStateOf("") }
    var firebaseUserVehicleColor: String by remember { mutableStateOf("") }
    var firebaseUserVehicleLP: String by remember { mutableStateOf("") }
    var firebaseUserVehicleWeight: String by remember { mutableStateOf("") }
    var firebaseUserRiderETA: String by remember { mutableStateOf("") }
    
    
   var userComNumberCountryCode: String by remember { mutableStateOf("+63") }
var userComNumber: String by remember { mutableStateOf("") }
var firebaseUserCompanyContactNumber by remember { mutableStateOf("") }
var currentStep by remember { mutableStateOf(1) }


LaunchedEffect(userComNumber) {
    firebaseUserCompanyContactNumber = userComNumberCountryCode + userComNumber
}



    var firebaseUserCompanyEmailAddress: String by remember { mutableStateOf("") }
    var firebaseUserCompanyPassword: String by remember { mutableStateOf("") }
    var firebaseUserConfirmPassword: String by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = (context as RegistrationRiders)

    var isNextEnabled by remember { mutableStateOf(false) } // Step 1
    var isNextEnabledTwo by remember { mutableStateOf(false) } // Step 2
    var isNextEnabledThree by remember { mutableStateOf(false) } // Step 3
    var isNextEnabledFour by remember { mutableStateOf(false) } // Step 3
    BackHandler {
        if (currentStep > 1) {
            currentStep--
        } else {
            activity.finish() 
        }
    }
    

    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            ?.isAppearanceLightStatusBars = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background images and setup...
        Image(
            painter = painterResource(id = R.drawable.gradient_two),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.bottombg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.BottomCenter)
                .background(Color.Transparent),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(Color.White)
        )

        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color.Transparent,
            topBar = { TopAppBar() },
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .background(color = Color.Transparent)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> if (targetState > initialState) fullWidth else -fullWidth }
                        ) + fadeIn() with
                                slideOutHorizontally(
                                    targetOffsetX = { fullWidth -> if (targetState > initialState) -fullWidth else fullWidth }
                                ) + fadeOut()
                    },
                    modifier = Modifier.fillMaxSize()
                ) { targetStep ->
                    when (targetStep) {
                        1 -> {
                            registrationStepOne(
                                imageUri = firebasedatabaseProfilePicture,
                                bioText = firebaseUserBio
                            ) { imageUri, bioText, isPhotoUploaded, isBioEntered ->
                                firebasedatabaseProfilePicture = imageUri
                                firebaseUserBio = bioText
                                isNextEnabled = isPhotoUploaded && isBioEntered
                                isNextEnabledTwo = false // Disable for next step
                            }
                        }
                        2 -> {
                            registrationStepTwo(
                                firebaseUserFirstName,
                                firebaseUserLastName,
                                firebaseUserCompanyAddress,
                                userComNumber,
                                userComNumberCountryCode
                            ) { userFName,userLName, userAddress, userContact, userCountryCode, userCheckFName, userCheckLName, userOrgNumber, userAddressCheck, userOrgCountryCode ->
                                firebaseUserFirstName = userFName
                                firebaseUserLastName = userLName
                                firebaseUserCompanyAddress = userAddress
                                userComNumber = userContact
                                userComNumberCountryCode = userCountryCode
                                isNextEnabledTwo = userCheckFName && userCheckLName && userAddressCheck && userOrgNumber 
                                isNextEnabled = false // Disable for first step
                            }
                        }
                        3-> {
                        registrationStepTwoCarrouerDetails(
                        vehicleType = firebaseUserVehicleType,
    vehicleColor = firebaseUserVehicleColor,
    vehicleLicensePlate = firebaseUserVehicleLP,
    vehicleWeight = firebaseUserVehicleWeight
                        )
                         { riderType,riderColor, riderLP, riderVW, riderCheckType, riderCheckColor, riderCheckLP, riderCheckVW ->
                                firebaseUserVehicleType = riderType
                                firebaseUserVehicleColor = riderColor
                                firebaseUserVehicleLP = riderLP
                                firebaseUserVehicleWeight = riderVW
                                isNextEnabledThree = riderCheckColor && riderCheckColor && riderCheckLP && riderCheckVW 
                                isNextEnabledTwo = false // Disable for first step
                            }
                        }
                        4 -> {
                            registrationStepThree(
                            firebaseUserRiderETA,
                                firebaseUserCompanyEmailAddress,
                                firebaseUserCompanyPassword
                            ) { riderETA,userEmail, userPassword, userConfirmPass, userCheckEmail, userCheckPassword, userCheckConfirmPass, riderCheckETA ->
                                firebaseUserCompanyEmailAddress = userEmail
                                firebaseUserCompanyPassword = userPassword
                                firebaseUserConfirmPassword = userConfirmPass
                                firebaseUserRiderETA = riderETA
                                isNextEnabledFour = userCheckEmail && userCheckConfirmPass && riderCheckETA
                                isNextEnabledThree = false // Disable for second step
                            }
                        }
                    }
                }

    
                registrationNavigationButton(
                vehicleType = firebaseUserVehicleType,
    vehicleColor = firebaseUserVehicleColor,
    vehicleLicensePlate = firebaseUserVehicleLP,
    vehicleWeight = firebaseUserVehicleWeight,
    ridersETA = firebaseUserRiderETA,
                firebasedatabaseProfilePicture, firebaseUserBio, firebaseUserFirstName, firebaseUserLastName, firebaseUserCompanyAddress, firebaseUserCompanyEmailAddress, firebaseUserCompanyContactNumber, firebaseUserCompanyPassword,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 48.dp),
                    isNextEnabled = when (currentStep) {
                        1 -> isNextEnabled
                        2 -> isNextEnabledTwo
                        3 -> isNextEnabledThree
                        4 -> isNextEnabledFour
                        else -> false
                    },
                    showBackButton = currentStep > 1,
                    onNextClick = {
                        if (currentStep == 1 && isNextEnabled) {
                            currentStep = 2
                        } else if (currentStep == 2 && isNextEnabledTwo) {
                            currentStep = 3
                        }else if (currentStep == 3 && isNextEnabledThree) {
                            currentStep = 4
                        }
                    },
                    onBackClick = {
                        if (currentStep > 1) {
                            currentStep--
                        }
                    },
                    coroutineScope
                )
            }
        }
    }
}



@Composable
fun registrationStepTwo(
    userFirstName: String,
    userLastName: String,
    comAddress: String,
    comNumber: String,
    comNumCountryCode: String,
    onStateChange: (String, String, String, String, String, Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit
) {
val context = LocalContext.current
                
    var userFirstName by remember { mutableStateOf(userFirstName) }
    var userLastName by remember { mutableStateOf(userLastName) }
    var companyAddress by remember { mutableStateOf(comAddress) }
    var countryCode by remember { mutableStateOf(comNumCountryCode) }
    var companyNumber by remember { mutableStateOf(comNumber) }
    var expanded by remember { mutableStateOf(false) }

onStateChange(
                            userFirstName,
                            userLastName,
                            companyAddress,
                            companyNumber,
                            countryCode,
                            userFirstName.isNotEmpty(),
                            userLastName.isNotEmpty(),
                            companyAddress.isNotEmpty(),
                            companyNumber.isNotEmpty(),
                            countryCode.isNotEmpty()
                        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, top = 45.dp)
    ) {
        Text(
            text = "Your Additional Information",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = userFirstName,
            onValueChange = {
                userFirstName = it
                onStateChange(
                            userFirstName,
                            userLastName,
                            companyAddress,
                            companyNumber,
                            countryCode,
                            userFirstName.isNotEmpty(),
                            userLastName.isNotEmpty(),
                            companyAddress.isNotEmpty(),
                            companyNumber.isNotEmpty(),
                            countryCode.isNotEmpty()
                        )
            },
            label = { Text("First Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color(0xFFF2F1FF),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF7967FF),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = userLastName,
            onValueChange = {
                userLastName = it
                onStateChange(
                            userFirstName,
                            userLastName,
                            companyAddress,
                            companyNumber,
                            countryCode,
                            userFirstName.isNotEmpty(),
                            userLastName.isNotEmpty(),
                            companyAddress.isNotEmpty(),
                            companyNumber.isNotEmpty(),
                            countryCode.isNotEmpty()
                        )
            },
            label = { Text("Last Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color(0xFFF2F1FF),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF7967FF),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = companyAddress,
            onValueChange = {
                companyAddress = it
                onStateChange(
                            userFirstName,
                            userLastName,
                            companyAddress,
                            companyNumber,
                            countryCode,
                            userFirstName.isNotEmpty(),
                            userLastName.isNotEmpty(),
                            companyAddress.isNotEmpty(),
                            companyNumber.isNotEmpty(),
                            countryCode.isNotEmpty()
                        )
            },
            label = { Text("House Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color(0xFFF2F1FF),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF7967FF),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 8.dp, end = 16.dp)
                    .clickable { expanded = true }
                    .background(Color.Transparent)
                    .border(1.dp, Color.White, shape = RoundedCornerShape(4.dp))
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = countryCode,
                    color = Color.White,
                    fontSize = 15.sp,
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("+63", "+1", "+44", "+91").forEach { code ->
                        DropdownMenuItem(onClick = {
                            countryCode = code
                            expanded = false
                        }) {
                            Text(text = code)
                        }
                    }
                }
            }

            OutlinedTextField(
                value = companyNumber,
                onValueChange = { input ->
                        companyNumber = input
                        onStateChange(
                            userFirstName,
                            userLastName,
                            companyAddress,
                            companyNumber,
                            countryCode,
                            userFirstName.isNotEmpty(),
                            userLastName.isNotEmpty(),
                            companyAddress.isNotEmpty(),
                            companyNumber.isNotEmpty(),
                            countryCode.isNotEmpty()
                        )
                        
                        if (companyNumber.isNotEmpty()) {
                Toast.makeText(context, "Company Number is: $companyNumber", Toast.LENGTH_SHORT).show()
            }
                   
                },
                label = { Text("Contact Number") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    focusedBorderColor = Color(0xFFF2F1FF),
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF7967FF),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun registrationStepTwoCarrouerDetails(
    vehicleType: String,
    vehicleColor: String,
    vehicleLicensePlate: String,
    vehicleWeight: String,
    onStateChange: (String, String, String, String, Boolean, Boolean, Boolean, Boolean) -> Unit
) {
    val context = LocalContext.current

    var riderVehicleType by remember { mutableStateOf(vehicleType) }
    var riderVehicleColor by remember { mutableStateOf(vehicleColor) }
    var riderVehicleLicensePlate by remember { mutableStateOf(vehicleLicensePlate) }
    var riderVehicleWeight by remember { mutableStateOf(vehicleWeight) }

    var showHelpDialog by remember { mutableStateOf(false) }
    var showHelpDialogTwo by remember { mutableStateOf(false) }
    var showHelpDialogThree by remember { mutableStateOf(false) }
    var showHelpDialogFour by remember { mutableStateOf(false) }
    var expandedTwo by remember { mutableStateOf(false) }
    
    onStateChange(
        riderVehicleType,
        riderVehicleColor,
        riderVehicleLicensePlate,
        riderVehicleWeight,
        riderVehicleType.isNotEmpty(),
        riderVehicleColor.isNotEmpty(),
        riderVehicleLicensePlate.isNotEmpty(),
        riderVehicleWeight.isNotEmpty()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, top = 45.dp)
    ) {
        Text(
            text = "Your Vehicle Information",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Vehicle Type
        Column {
        
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 32.dp)
                .clickable { expandedTwo = !expandedTwo }
                .background(Color.Transparent)
                .border(1.dp, Color.White, shape = RoundedCornerShape(4.dp))
                .padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Text(
        text = if (riderVehicleType.isEmpty()) "Vehicle Type?" else riderVehicleType,
        color = Color.White,
        fontSize = 15.sp,
    )

            DropdownMenu(
                expanded = expandedTwo,
                onDismissRequest = { expandedTwo = false }
            ) {
                listOf("Motorcycle", "Car", "Bike").forEach { type ->
                    DropdownMenuItem(onClick = {
                        riderVehicleType = type
                        expandedTwo = false
                    }) {
                        Text(text = type)
                    }
                }
            }
        }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .clickable { showHelpDialog = true }
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                            contentDescription = "Help Icon",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Why do we need your vehicle type?",
                            modifier = Modifier.clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) { showHelpDialog = false },
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 10.sp
                        )
                    }

                    if (showHelpDialog) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .background(
                                    Color.White.copy(alpha = 0.20f),
                                    shape = RoundedCornerShape(18.dp)
                                )
   .padding(start = 8.dp, top =4.dp, end = 16.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) { showHelpDialog = false }
                        ) {
                            Text(
                                text = "Providing your vehicle type helps recipients identify the delivery vehicle easily.",
                                color = Color(0xFF422C5C),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Vehicle Color
        Column {
            OutlinedTextField(
                value = riderVehicleColor,
                onValueChange = {
                    riderVehicleColor = it
                    onStateChange(
                        riderVehicleType,
                        riderVehicleColor,
                        riderVehicleLicensePlate,
                        riderVehicleWeight,
                        riderVehicleType.isNotEmpty(),
                        riderVehicleColor.isNotEmpty(),
                        riderVehicleLicensePlate.isNotEmpty(),
                        riderVehicleWeight.isNotEmpty()
                    )
                },
                label = { Text("Vehicle Color") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    focusedBorderColor = Color(0xFFF2F1FF),
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF7967FF),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Vehicle License Plate
        Column {
            OutlinedTextField(
                value = riderVehicleLicensePlate,
                onValueChange = {
                    riderVehicleLicensePlate = it
                    onStateChange(
                        riderVehicleType,
                        riderVehicleColor,
                        riderVehicleLicensePlate,
                        riderVehicleWeight,
                        riderVehicleType.isNotEmpty(),
                        riderVehicleColor.isNotEmpty(),
                        riderVehicleLicensePlate.isNotEmpty(),
                        riderVehicleWeight.isNotEmpty()
                    )
                },
                label = { Text("Vehicle License Plate Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    focusedBorderColor = Color(0xFFF2F1FF),
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF7967FF),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
        
                    Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .clickable { showHelpDialogTwo = true }
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                            contentDescription = "Help Icon",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Why do we need to know your Vehicle Licenses Plate?",
                            modifier = Modifier.clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) { showHelpDialogTwo = false },
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 10.sp
                        )
                    }

                    if (showHelpDialogTwo) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
  .background(
                                    Color.White.copy(alpha = 0.20f),
                                    shape = RoundedCornerShape(18.dp)
                                )
   .padding(start = 8.dp, top =4.dp, end = 16.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) { showHelpDialogTwo = false }
                        ) {
                            Text(
                                text = "Providing your vehicle type helps recipients identify the delivery vehicle easily.",
                                color = Color(0xFF422C5C),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Vehicle Weight
        Column {
            OutlinedTextField(
                value = riderVehicleWeight,
                onValueChange = {
                    riderVehicleWeight = it
                    onStateChange(
                        riderVehicleType,
                        riderVehicleColor,
                        riderVehicleLicensePlate,
                        riderVehicleWeight,
                        riderVehicleType.isNotEmpty(),
                        riderVehicleColor.isNotEmpty(),
                        riderVehicleLicensePlate.isNotEmpty(),
                        riderVehicleWeight.isNotEmpty()
                    )
                },
                label = { Text("Vehicle Weight") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    focusedBorderColor = Color(0xFFF2F1FF),
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color(0xFF7967FF),
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White
                )
            )
        
                    Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .clickable { showHelpDialogThree = true }
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                            contentDescription = "Help Icon",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Why do we need your vehicle weight?",
                            modifier = Modifier.clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) { showHelpDialogThree = false },
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 10.sp
                        )
                    }

                    if (showHelpDialogThree) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .background(
                                    Color.White.copy(alpha = 0.20f),
                                    shape = RoundedCornerShape(18.dp)
                                )
   .padding(start = 8.dp, top =4.dp, end = 16.dp)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) { showHelpDialogThree = false }
                        ) {
                            Text(
                                text = "Providing your vehicle type helps recipients identify the delivery vehicle easily.",
                                color = Color.White.copy(alpha = 0.40f),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun registrationNavigationButton(
vehicleType: String,
    vehicleColor: String,
    vehicleLicensePlate: String,
    vehicleWeight: String,
    ridersETA: String,
    firebaseOrgProfileImage: Uri?,
    firebaseOrgBio: String,
    firebaseUserFName: String,
    firebaseUserLName: String,
    firebaseOrgAddress: String,
    firebaseOrgEmail: String,
    firebaseOrgFullContact: String,
    firebaseOrgPasswordAccount: String,
    modifier: Modifier = Modifier,
    isNextEnabled: Boolean,
    showBackButton: Boolean,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    coroutineScope: CoroutineScope
) {
    var text by remember { mutableStateOf("Next") }
    var showProgressDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
     
                            // Initialize an empty donorData HashMap
val donorData = hashMapOf<String, Any?>()
val riderAvailable = hashMapOf<String, Any?>()

    if (showProgressDialog) {
        CustomProgressDialogWithCancel(
            onCancel = { showProgressDialog = false }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (showBackButton) Arrangement.SpaceBetween else Arrangement.End
        ) {
            if (showBackButton) {
                Box(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .size(40.dp)
                        .clickable {
                            text = "Next"
                            onBackClick()
                        }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back button",
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center)
                    )
                }
            }

            if (firebaseOrgProfileImage != null &&
                firebaseOrgBio.isNotEmpty() &&
                firebaseUserFName.isNotEmpty() &&
                firebaseUserLName.isNotEmpty() &&
                firebaseOrgAddress.isNotEmpty() &&
                firebaseOrgEmail.isNotEmpty() &&
                firebaseOrgFullContact.isNotEmpty() &&
                firebaseOrgPasswordAccount.isNotEmpty()
            ) {
                text = "Done"
                Text(
                    text = text,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .clickable(enabled = isNextEnabled) {
                        coroutineScope.launch {
                            showProgressDialog = true

                            val firestore = FirebaseFirestore.getInstance()
                            val userStatus = "BasicAccounts"
                            val statusType = "Rider"
                           
// Retrieve profile image URI
val profileImageStorage = saveImageAndRetrieveUri(firebaseOrgEmail, firebaseOrgProfileImage)

FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("FCM", "Fetching FCM registration token failed", task.exception)
            return@addOnCompleteListener
        }
        val token = task.result
        Log.d("FCM Token", token)
// Populate donorData with key-value pairs
donorData["profileImage"] = profileImageStorage.toString()
donorData["bio"] = firebaseOrgBio
donorData["Rider First Name"] = firebaseUserFName
donorData["Rider Last Name"] = firebaseUserLName
donorData["Status_account"] = userStatus
donorData["Account_type"] = statusType
donorData["address"] = firebaseOrgAddress
donorData["email"] = firebaseOrgEmail
donorData["fullContact"] = firebaseOrgFullContact
donorData["passwordAccount"] = firebaseOrgPasswordAccount
donorData["volunteer_licenseplate"] = vehicleLicensePlate
donorData["vehicle_weight"] = vehicleWeight
donorData["volunteer_courrier"] = vehicleType
donorData["vehicle_license_plate"] = vehicleLicensePlate
donorData["ridersETA"] = ridersETA
donorData["fcmToken"] = token

riderAvailable["volunteer_profileimage"] = profileImageStorage.toString()
riderAvailable["bio"] = firebaseOrgBio
riderAvailable["volunteer_firstname"] = firebaseUserFName
riderAvailable["volunteer_lastname"] = firebaseUserLName
riderAvailable["fullContact"] = firebaseOrgFullContact
riderAvailable["volunteer_email"] = firebaseOrgEmail
riderAvailable["vehicle_weight"] = vehicleWeight
riderAvailable["vehicle_license_plate"] = vehicleLicensePlate
riderAvailable["volunteer_courrier"] = vehicleType
riderAvailable["ridersETA"] = ridersETA
}
                            
                            val checkEmailExistence: (String) -> Task<DocumentSnapshot> = { collection ->
                                firestore.collection("GivnGoAccounts")
                                    .document(collection)
                                    .collection(statusType)
                                    .document(firebaseOrgEmail)
                                    .get()
                            }

                            checkEmailExistence("BasicAccounts").addOnSuccessListener { basicDoc ->
                                if (basicDoc.exists()) {
                                    showProgressDialog = false
                                    Toast.makeText(context, "Email already exists in BasicAccounts", Toast.LENGTH_LONG).show()
                                } else {
                                    checkEmailExistence("VerifiedAccounts").addOnSuccessListener { verifiedDoc ->
                                        if (verifiedDoc.exists()) {
                                            showProgressDialog = false
                                            Toast.makeText(context, "Email already exists in VerifiedAccounts", Toast.LENGTH_LONG).show()
                                        } else {
                                        
                                        
                                        
                                            firestore.collection("GivnGoAccounts")
                                                .document(userStatus)
                                                .collection(statusType)
                                                .document(firebaseOrgEmail)
                                                .set(donorData)
                                                .addOnSuccessListener {
                                                    
                                                    firestore.collection("GivnGoRiders")
            .document("Volunteers")
            .collection("AvailableVolunteers")
            .document(firebaseUserFName)
            .set(riderAvailable)
            .addOnSuccessListener {
                 SharedPreferences.saveEmail(context, firebaseOrgEmail)
                                                    SharedPreferences.saveAdd(context, firebaseOrgAddress)
                                                    SharedPreferences.saveHashPass(context, firebaseOrgPasswordAccount)
                                                    SharedPreferences.saveStatus(context, statusType)
                                                    SharedPreferences.saveFirstName(context, firebaseUserFName)
                                                    SharedPreferences.saveBasicType(context, userStatus)
                                                    SharedPreferences.saveLastName(context, firebaseUserLName)
                                                    SharedPreferences.saveProfileImageUri(context, profileImageStorage.toString())
                                                    showProgressDialog = false
                                                    val intent = Intent(context, MainActivity::class.java)
                                                    intent.putExtra("signup_finish_basic", "recipientFinishBasic")
                                                    context.startActivity(intent)
            }.addOnFailureListener { exception ->
            showProgressDialog = false
                                                    Toast.makeText(context, "Error 408: Failed to save data", Toast.LENGTH_LONG).show()
                                                    Log.e("FirestoreError", "Error 303: ${exception.localizedMessage}", exception)
            }
                                               
                                                }
                                                .addOnFailureListener { exception ->
                                                    showProgressDialog = false
                                                    Toast.makeText(context, "Error 303: Failed to save data", Toast.LENGTH_LONG).show()
                                                    Log.e("FirestoreError", "Error 303: ${exception.localizedMessage}", exception)
                                                }
                                        }
                                    }.addOnFailureListener { exception ->
                                        showProgressDialog = false
                                        Toast.makeText(context, "Error checking email in VerifiedAccounts: ${exception.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }.addOnFailureListener { exception ->
                                showProgressDialog = false
                                Toast.makeText(context, "Error checking email in BasicAccounts: ${exception.message}", Toast.LENGTH_LONG).show()
                            }
                          }
                        },
                    color = if (isNextEnabled) Color(0xFF8070F6) else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            } else {
                Text(
                    text = "Next",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .clickable(enabled = isNextEnabled) { onNextClick() },
                    color = if (isNextEnabled) Color(0xFF8070F6) else Color.Gray,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}



suspend fun saveImageAndRetrieveUri(
    email: String,
    uri: Uri
): Uri? {
    return try {
        val storage = FirebaseStorage.getInstance()
        val formattedEmail = email.replace(".", "_")
        val imagePath = "GivnGoAccounts/$formattedEmail/profileImages/image_1"
        val imageRef = storage.reference.child(imagePath)

        // Upload the image and wait for the task to complete
        imageRef.putFile(uri).await()

        // Retrieve and return the download URI
        val downloadUri = imageRef.downloadUrl.await()
        Log.d("Storage", "Image uploaded and URI retrieved: $downloadUri")
        downloadUri
    } catch (exception: Exception) {
        Log.e("Storage", "Error uploading image or retrieving URI", exception)
        null
    }
}


@Composable
fun CustomProgressDialogWithCancel(onCancel: () -> Unit) {
    Dialog(onDismissRequest = { /* Do nothing on dismiss */ }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Saving data..."
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Please wait while your data is being saved."
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { onCancel() }) {
                    Text(text = "Cancel", color = Color(0xFF8070F6))
                }
            }
        }
    }
}


@Composable
fun registrationStepThree(
   riderETA: String,
    orgEmail: String,
    orgPassword: String,
    onStateChange: (String,String, String, String,Boolean, Boolean, Boolean, Boolean) -> Unit
) {
    var organizationEmail by remember { mutableStateOf(orgEmail) }
    var organizationPassword by remember { mutableStateOf(orgPassword) }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
        var riderETA by remember { mutableStateOf(riderETA) }
        var showHelpDialog by remember { mutableStateOf(false) }

    val isPasswordValid = organizationPassword.length >= 8
    val isConfirmPasswordValid = organizationPassword == confirmPassword && isPasswordValid

    onStateChange(
    riderETA,
        organizationEmail,
        organizationPassword,
        confirmPassword,
        riderETA.isNotEmpty(),
        organizationEmail.isNotEmpty(),
        isPasswordValid,
        isConfirmPasswordValid
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, top = 45.dp)
    ) {
        Text(
            text = "Lastly",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        Column{
        OutlinedTextField(
            value = riderETA,
            onValueChange = {
                riderETA = it
                onStateChange(
    riderETA,
        organizationEmail,
        organizationPassword,
        confirmPassword,
        riderETA.isNotEmpty(),
        organizationEmail.isNotEmpty(),
        isPasswordValid,
        isConfirmPasswordValid
    )
            },
            label = { Text("Expected Time Arrival") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color(0xFFF2F1FF),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF7967FF),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .clickable { showHelpDialog = true }
        ) {
        Column{
        Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_info_details),
                    contentDescription = "Help Icon",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
    text = "Why do we need to know your ETA?",
    modifier = Modifier.clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { showHelpDialog = false },
    color = Color.White.copy(alpha = 0.6f), // Adjust alpha for opacity
    fontSize = 10.sp
)

            }
            
                         // Small dialog-like overlay for help text
        if (showHelpDialog) {
            
                Box(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .background(Color.White.copy(alpha = 0.20f), shape = RoundedCornerShape(18.dp))
                        .padding(8.dp)
                                            .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { showHelpDialog = false } 
                ) {
                    Text(
                        text = "Providing your vehicle type helps recipients identify the delivery vehicle easily.",
                        color = Color(0xFF422C5C),
                        fontSize = 14.sp
                    )
            }
        }
            
        }
            


        }

        }
        
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = organizationEmail,
            onValueChange = {
                organizationEmail = it
                onStateChange(
    riderETA,
        organizationEmail,
        organizationPassword,
        confirmPassword,
        riderETA.isNotEmpty(),
        organizationEmail.isNotEmpty(),
        isPasswordValid,
        isConfirmPasswordValid
    )
            },
            label = { Text("Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color(0xFFF2F1FF),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF7967FF),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = organizationPassword,
            onValueChange = {
                organizationPassword = it
                onStateChange(
    riderETA,
        organizationEmail,
        organizationPassword,
        confirmPassword,
        riderETA.isNotEmpty(),
        organizationEmail.isNotEmpty(),
        isPasswordValid,
        isConfirmPasswordValid
    )
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color(0xFFF2F1FF),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF7967FF),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            isError = !isPasswordValid && organizationPassword.isNotEmpty()
        )

        if (!isPasswordValid && organizationPassword.isNotEmpty()) {
            Text(
                text = "Password must be at least 8 characters",
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                onStateChange(
    riderETA,
        organizationEmail,
        organizationPassword,
        confirmPassword,
        riderETA.isNotEmpty(),
        organizationEmail.isNotEmpty(),
        isPasswordValid,
        isConfirmPasswordValid
    )
            },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (confirmPasswordVisible) "Hide confirm password" else "Show confirm password"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color(0xFFF2F1FF),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF7967FF),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            ),
            isError = !isConfirmPasswordValid && confirmPassword.isNotEmpty()
        )

        if (!isConfirmPasswordValid && confirmPassword.isNotEmpty()) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}



@Composable
fun registrationStepOne(
    imageUri: Uri?,
    bioText: String,
    onStateChange: (Uri?, String, Boolean, Boolean) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf(imageUri) }
    var croppedImageUri by remember { mutableStateOf<Uri?>(null) }
    var userName by remember { mutableStateOf(bioText) }

    // Update the state based on profile photo and bio presence
    onStateChange(croppedImageUri ?: selectedImageUri, userName, croppedImageUri != null || selectedImageUri != null, userName.isNotEmpty())

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uriList ->
        val filteredUriList = uriList.take(1)
        if (filteredUriList.isNotEmpty()) {
            selectedImageUri = filteredUriList.first()
            croppedImageUri = filteredUriList.first() // Replace with your cropping logic if needed
            onStateChange(croppedImageUri, userName, croppedImageUri != null, userName.isNotEmpty())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, top = 45.dp)
    ) {
        Text(
            text = "Your Profile Information",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .clickable {
                    // Launch the image picker
                    launcher.launch("image/*")
                }
                .background(
                    color = Color(0xFFFBF8FF),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (croppedImageUri != null) {
                AsyncImage(
                model = croppedImageUri,
                contentDescription = null,
                modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
            )
            } else if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
            )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userName,
            onValueChange = { 
                userName = it 
                onStateChange(croppedImageUri ?: selectedImageUri, userName, croppedImageUri != null || selectedImageUri != null, userName.isNotEmpty())
            },
            label = { Text("Add Interesting things about yourself") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp),
            singleLine = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                focusedBorderColor = Color(0xFFF2F1FF),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color(0xFF7967FF),
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )
    }
}




@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 35.dp)
            .background(color = Color.Transparent),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        

        Text(
            text = "Create account as a Rider",
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
    }
}