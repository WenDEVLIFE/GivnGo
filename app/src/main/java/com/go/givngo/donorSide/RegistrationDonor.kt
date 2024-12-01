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

import androidx.activity.compose.BackHandler


class RegistrationDonor : ComponentActivity() {

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
    var firebaseUserOrganizationName: String by remember { mutableStateOf("") }
    var firebaseUserOrganizationType: String by remember { mutableStateOf("Business") }
    var firebaseUserCompanyAddress: String by remember { mutableStateOf("") }
     
    var userComNumberCountryCode: String by remember { mutableStateOf("+63") }
var userComNumber: String by remember { mutableStateOf("") }
var firebaseUserCompanyContactNumber by remember { mutableStateOf("") }

LaunchedEffect(userComNumber) {
    firebaseUserCompanyContactNumber = userComNumberCountryCode + userComNumber
}



    var firebaseUserCompanyEmailAddress: String by remember { mutableStateOf("") }
    var firebaseUserCompanyPassword: String by remember { mutableStateOf("") }
    var firebaseUserConfirmPassword: String by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = (context as RegistrationDonor)

    var isNextEnabled by remember { mutableStateOf(false) } // Step 1
    var isNextEnabledTwo by remember { mutableStateOf(false) } // Step 2
    var isNextEnabledThree by remember { mutableStateOf(false) } // Step 3
    var currentStep by remember { mutableStateOf(1) }

    BackHandler {
     if (currentStep > 1) {
        currentStep--
    } else {
        activity.finish() // Safely call finish() on the activity
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
                                firebaseUserOrganizationName,
                                firebaseUserOrganizationType,
                                firebaseUserCompanyAddress,
                                userComNumber,
                                userComNumberCountryCode
                            ) { orgType, userOrgName, userAddress, userContact, userCountryCode, userOrgNameCheck, userAddressCheck, userOrgNumber, userOrgCountryCode ->
                                firebaseUserOrganizationName = userOrgName
                                firebaseUserOrganizationType = orgType
                                firebaseUserCompanyAddress = userAddress
                                userComNumber = userContact
                                userComNumberCountryCode = userCountryCode
                                isNextEnabledTwo = userOrgNameCheck && userAddressCheck && userOrgNumber
                                isNextEnabled = false // Disable for first step
                            }
                        }
                        3 -> {
                            registrationStepThree(
                                firebaseUserCompanyEmailAddress,
                                firebaseUserCompanyPassword
                            ) { userEmail, userPassword, userConfirmPass, userCheckEmail, userCheckPassword, userCheckConfirmPass ->
                                firebaseUserCompanyEmailAddress = userEmail
                                firebaseUserCompanyPassword = userPassword
                                firebaseUserConfirmPassword = userConfirmPass
                                isNextEnabledThree = userCheckEmail && userCheckConfirmPass
                                isNextEnabledTwo = false // Disable for second step
                            }
                        }
                    }
                }

                registrationNavigationButton(
                coroutineScope,
                firebaseUserOrganizationType,
                firebasedatabaseProfilePicture, firebaseUserBio, firebaseUserOrganizationName, firebaseUserCompanyAddress, firebaseUserCompanyEmailAddress, firebaseUserCompanyContactNumber, firebaseUserCompanyPassword,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 48.dp),
                    isNextEnabled = when (currentStep) {
                        1 -> isNextEnabled
                        2 -> isNextEnabledTwo
                        3 -> isNextEnabledThree
                        else -> false
                    },
                    showBackButton = currentStep > 1,
                    onNextClick = {
                        if (currentStep == 1 && isNextEnabled) {
                            currentStep = 2
                        } else if (currentStep == 2 && isNextEnabledTwo) {
                            currentStep = 3
                        }
                    },
                    onBackClick = {
                        if (currentStep > 1) {
                            currentStep--
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun registrationNavigationButton(
coroutineScope: CoroutineScope,
firebaseOrgType: String,
    firebaseOrgProfileImage: Uri?,
    firebaseOrgBio: String,
    firebaseOrgName: String,
    firebaseOrgAddress: String,
    firebaseOrgEmail: String,
    firebaseOrgFullContact: String,
    firebaseOrgPasswordAccount: String,
    modifier: Modifier = Modifier,
    isNextEnabled: Boolean,
    showBackButton: Boolean,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var text by remember { mutableStateOf("Next") }
    var showProgressDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
                                // Declare donorData as a HashMap
val donorData = hashMapOf<String, Any?>()

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
                firebaseOrgName.isNotEmpty() &&
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
                               
                            val statusType = "Donor"
                            val basicDefaultAccount = "BasicAccounts"
                            
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
donorData["OrganizationName"] = firebaseOrgName
donorData["Status_account"] = basicDefaultAccount
donorData["Account_type"] = statusType
donorData["address"] = firebaseOrgAddress
donorData["email"] = firebaseOrgEmail
donorData["fcmToken"] = token
donorData["fullContact"] = firebaseOrgFullContact
donorData["Organization_Type"] = firebaseOrgType
donorData["passwordAccount"] = firebaseOrgPasswordAccount

                            }
                            
                            // First check in BasicAccounts
                            firestore.collection("GivnGoAccounts")
                                .document("BasicAccounts")
                                .collection(statusType)
                                .document(firebaseOrgEmail)
                                .get()
                                .addOnSuccessListener { basicDoc ->
                                    if (basicDoc.exists()) {
                                        // Email already exists in BasicAccounts
                                        showProgressDialog = false
                                        Toast.makeText(
                                            context,
                                            "Email already used",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        // Check in VerifiedAccounts
                                        firestore.collection("GivnGoAccounts")
                                            .document("VerifiedAccounts")
                                            .collection(statusType)
                                            .document(firebaseOrgEmail)
                                            .get()
                                            .addOnSuccessListener { verifiedDoc ->
                                                if (verifiedDoc.exists()) {
                                                    // Email already exists in VerifiedAccounts
                                                    showProgressDialog = false
                                                    Toast.makeText(
                                                        context,
                                                        "Email already used",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                } else {
                                                    // Email does not exist in either, proceed with saving
                                                   

                                                    firestore.collection("GivnGoAccounts")
                                                        .document("BasicAccounts")
                                                        .collection(statusType)
                                                        .document(firebaseOrgEmail)
                                                        .set(donorData)
                                                        .addOnSuccessListener {
                                                            showProgressDialog = false
                                                            
                                                            SharedPreferences.saveEmail(context, firebaseOrgEmail)
                                                            SharedPreferences.saveAdd(context, firebaseOrgAddress)
                                                            SharedPreferences.saveHashPass(context, firebaseOrgPasswordAccount)
                                                            SharedPreferences.saveStatus(context, statusType)
                                                            SharedPreferences.saveOrgName(context, firebaseOrgName)
                                                            SharedPreferences.saveBasicType(context, basicDefaultAccount)
                                                            SharedPreferences.saveProfileImageUri(context, profileImageStorage.toString())
                                                            
                                                            val intent = Intent(context, MainActivity::class.java)
                                                            intent.putExtra("signup_finish_basic", "Donors")
                                                            context.startActivity(intent)
                                                        }
                                                        .addOnFailureListener { exception ->
                                                            showProgressDialog = false
                                                            Toast.makeText(context, "Error 303: Failed to save data", Toast.LENGTH_LONG).show()
                                                            Log.e("FirestoreError", "Error 303: ${exception.localizedMessage}", exception)
                                                        }
                                                }
                                            }
                                            .addOnFailureListener { exception ->
                                                showProgressDialog = false
                                                Toast.makeText(context, "Error checking VerifiedAccounts", Toast.LENGTH_LONG).show()
                                                Log.e("FirestoreError", "Error checking VerifiedAccounts: ${exception.localizedMessage}", exception)
                                            }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    showProgressDialog = false
                                    Toast.makeText(context, "Error checking BasicAccounts", Toast.LENGTH_LONG).show()
                                    Log.e("FirestoreError", "Error checking BasicAccounts: ${exception.localizedMessage}", exception)
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
    orgEmail: String,
    orgPassword: String,
    onStateChange: (String, String, String, Boolean, Boolean, Boolean) -> Unit
) {
    var organizationEmail by remember { mutableStateOf(orgEmail) }
    var organizationPassword by remember { mutableStateOf(orgPassword) }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isPasswordValid = organizationPassword.length >= 8
    val isConfirmPasswordValid = organizationPassword == confirmPassword && isPasswordValid

    onStateChange(
        organizationEmail,
        organizationPassword,
        confirmPassword,
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

        OutlinedTextField(
            value = organizationEmail,
            onValueChange = {
                organizationEmail = it
                onStateChange(
                    organizationEmail,
                    organizationPassword,
                    confirmPassword,
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
                    organizationEmail,
                    organizationPassword,
                    confirmPassword,
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
                    organizationEmail,
                    organizationPassword,
                    confirmPassword,
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
fun registrationStepTwo(
    orgName: String,
    donorOrgType: String,
    comAddress: String,
    comNumber: String,
    comNumCountryCode: String,
    onStateChange: (String,String, String, String, String, Boolean, Boolean, Boolean, Boolean) -> Unit
) {
    val context = LocalContext.current
    var organizationName by remember { mutableStateOf(orgName) }
    var companyAddress by remember { mutableStateOf(comAddress) }
    var countryCode by remember { mutableStateOf(comNumCountryCode) }
    var companyNumber by remember { mutableStateOf(comNumber) }
    var selectedType by remember { mutableStateOf(donorOrgType) }  // Default selection
    var expanded by remember { mutableStateOf(false) }
    var expandedTwo by remember { mutableStateOf(false) }
    var companyNumberInput by remember { mutableStateOf(companyNumber) }

    // Trigger state change when any field is updated
    onStateChange(
    selectedType,
        organizationName,
        companyAddress,
        companyNumberInput,
        countryCode,
        organizationName.isNotEmpty(),
        companyAddress.isNotEmpty(),
        companyNumberInput.isNotEmpty(),
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

        // Dropdown for selecting type (Organization/Charity, Business, Self Proprietor)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp)
                .clickable { expandedTwo = !expandedTwo }
                .background(Color.Transparent)
                .border(1.dp, Color.White, shape = RoundedCornerShape(4.dp))
                .padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Text(
                text = selectedType,
                color = Color.White,
                fontSize = 15.sp,
            )

            DropdownMenu(
                expanded = expandedTwo,
                onDismissRequest = { expandedTwo = false }
            ) {
                listOf("Organization/Charity", "Business", "Self Proprietor").forEach { type ->
                    DropdownMenuItem(onClick = {
                        selectedType = type
                        expandedTwo = false
                    }) {
                        Text(text = type)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Change the label based on selected type
        val label = when (selectedType) {
            "Organization/Charity" -> "Organization/Charity Name"
            "Self Proprietor" -> "Self Proprietor Name"
            else -> "Business Name"
        }

        // Organization/Business Name input field
        OutlinedTextField(
            value = organizationName,
            onValueChange = {
                organizationName = it
                onStateChange(
    selectedType,
        organizationName,
        companyAddress,
        companyNumberInput,
        countryCode,
        organizationName.isNotEmpty(),
        companyAddress.isNotEmpty(),
        companyNumberInput.isNotEmpty(),
        countryCode.isNotEmpty()
    )
            },
            label = { Text(label) },
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
    selectedType,
        organizationName,
        companyAddress,
        companyNumberInput,
        countryCode,
        organizationName.isNotEmpty(),
        companyAddress.isNotEmpty(),
        companyNumberInput.isNotEmpty(),
        countryCode.isNotEmpty()
    )
            },
            label = { Text("Company Office Address") },
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

        // Country code and number input
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
                value = companyNumberInput,
                onValueChange = { input ->
                    companyNumberInput = input  

                    onStateChange(
    selectedType,
        organizationName,
        companyAddress,
        companyNumberInput,
        countryCode,
        organizationName.isNotEmpty(),
        companyAddress.isNotEmpty(),
        companyNumberInput.isNotEmpty(),
        countryCode.isNotEmpty()
    )

                    // Show Toast when companyNumberInput has a value
                    if (companyNumberInput.isNotEmpty()) {
                        Toast.makeText(context, "Company Number is: $companyNumberInput", Toast.LENGTH_SHORT).show()
                    } else {  // Handle case when input is cleared
                        Toast.makeText(context, "Contact number cleared", Toast.LENGTH_SHORT).show()
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
            label = { Text("Add Interesting things about your Organization") },
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
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        Spacer(modifier = Modifier.width(75.dp))

        Text(
            text = "Create account as a Donor",
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
    }
}