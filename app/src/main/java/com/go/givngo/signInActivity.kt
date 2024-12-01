package com.go.givngo

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
import com.go.givngo.donorSide.RegistrationDonor

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import coil.compose.rememberImagePainter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf

import androidx.compose.animation.core.tween
import androidx.compose.animation.AnimatedContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState

import com.go.givngo.recipientSide.RegistrationRecipient

import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID
import com.go.givngo.MainActivity
import androidx.compose.ui.window.Dialog

import android.util.Log
import com.go.givngo.SharedPreferences
import com.google.firebase.firestore.DocumentSnapshot // Make sure to import this
import com.google.firebase.messaging.FirebaseMessaging
 class signInActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
                MyAppSignIn()
            }
        }
    }
}

@Composable
fun MyAppSignIn() {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as signInActivity)

    var isNextEnabled by remember { mutableStateOf(false) } // Step 1
    var isNextEnabledTwo by remember { mutableStateOf(false) } // Step 2
    var isNextEnabledThree by remember { mutableStateOf(false) } // Step 3
    var currentStep by remember { mutableStateOf(1) }

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
                signInDonorRecipientRider()
                
                }
        }
    }
}


@Composable
fun signInDonorRecipientRider() {
    val context = LocalContext.current
    var organizationEmail by remember { mutableStateOf("") }
    var organizationPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

// Function to handle Firestore authentication
    fun signInWithFirestore(email: String, password: String) {
    val db = FirebaseFirestore.getInstance()
    val accountTypes = listOf("Recipient", "Donor", "Rider")
    val accountsRef = db.collection("GivnGoAccounts")

    var accountFound = false
    errorMessage = "" // Clear any previous error message

    // First check in BasicAccounts
    accountTypes.forEach { type ->
        if (accountFound) return@forEach // Exit loop if account is found

        accountsRef.document("BasicAccounts").collection(type)
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    accountFound = true
                    val storedPassword = document.getString("passwordAccount") ?: ""

                    if (storedPassword == password) {
                        // Get the FCM token
                        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                                return@addOnCompleteListener
                            }
                            val fcmToken = task.result

                            // Update FCM token in Firestore for this user
                            

                            val accountType = document.getString("Account_type") ?: ""
                            val statusAccount = document.getString("Status_account") ?: ""

                            when (accountType) {
                                "Donor" -> {
                                    val orgName = document.getString("OrganizationName") ?: ""
                                    val emailAccount = document.getString("email") ?: ""
                                    val userProfileImage = document.getString("profileImage") ?: ""
                                    
                                    // Save data to SharedPreferences
                                    SharedPreferences.saveEmail(context, emailAccount)
                                    SharedPreferences.saveStatus(context, accountType)
                                    SharedPreferences.saveOrgName(context, orgName)
                                    SharedPreferences.saveBasicType(context, statusAccount)
                                    SharedPreferences.saveProfileImageUri(context, userProfileImage)
                                    
                                    val address= document.getString("address") ?: ""
                                    SharedPreferences.saveAdd(context, address)
                                    
                                    // Redirect to MainActivity
                                    
                                    
                                    accountsRef.document("BasicAccounts")
                                .collection("Donor")
                                .document(email)
                                .update("fcmToken", fcmToken)
                                .addOnSuccessListener {
                                    Log.d("FCM", "FCM token updated successfully")
                                    
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("FCM", "Error updating FCM token", e)
                                }
                                
                                }
                                "Recipient" -> {
                                    val firstName = document.getString("Recipient First Name") ?: ""
                                    val lastName = document.getString("Recipient Last Name") ?: ""
                                    val emailAccount = document.getString("email") ?: ""
                                    val userProfileImage = document.getString("profileImage") ?: ""

                                    SharedPreferences.saveEmail(context, emailAccount)
                                    SharedPreferences.saveStatus(context, accountType)
                                    SharedPreferences.saveFirstName(context, firstName)
                                    SharedPreferences.saveLastName(context, lastName)
                                    SharedPreferences.saveBasicType(context, statusAccount)
                                    SharedPreferences.saveProfileImageUri(context, userProfileImage)
                                    
                                    val address= document.getString("address") ?: ""
                                    SharedPreferences.saveAdd(context, address)
                                    
                                    accountsRef.document("BasicAccounts")
                                .collection("Recipient")
                                .document(email)
                                .update("fcmToken", fcmToken)
                                .addOnSuccessListener {
                                    Log.d("FCM", "FCM token updated successfully")
                                    
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("FCM", "Error updating FCM token", e)
                                }
                                }
                                "Rider" -> {
                                    val firstName = document.getString("Rider First Name") ?: ""
                                    val lastName = document.getString("Rider Last Name") ?: ""
                                    val emailAccount = document.getString("email") ?: ""
                                    val userProfileImage = document.getString("profileImage") ?: ""

                                    SharedPreferences.saveEmail(context, emailAccount)
                                    SharedPreferences.saveStatus(context, accountType)
                                    SharedPreferences.saveFirstName(context, firstName)
                                    SharedPreferences.saveLastName(context, lastName)
                                    SharedPreferences.saveBasicType(context, statusAccount)
                                    SharedPreferences.saveProfileImageUri(context, userProfileImage)
                                    
                                    val address= document.getString("address") ?: ""
                                    SharedPreferences.saveAdd(context, address)
                                    
                                   accountsRef.document("BasicAccounts")
                                .collection("Rider")
                                .document(email)
                                .update("fcmToken", fcmToken)
                                .addOnSuccessListener {
                                    Log.d("FCM", "FCM token updated successfully")
                                    
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("FCM", "Error updating FCM token", e)
                                }
                                }
                            }
                        }

                    } else {
                        errorMessage = "Wrong password"
                    }
                }
            }
            .addOnFailureListener {
                errorMessage = "Error checking account, please check your email or password"
            }
    }

    // If no account found in BasicAccounts, then check VerifiedAccounts
    if (!accountFound) {
        accountTypes.forEach { type ->
            if (accountFound) return@forEach // Exit loop if account is found

            accountsRef.document("VerifiedAccounts").collection(type)
                .document(email)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        accountFound = true
                        val storedPassword = document.getString("passwordAccount") ?: ""

                        if (storedPassword == password) {
                            // Get the FCM token
                            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                                    return@addOnCompleteListener
                                }
                                val fcmToken = task.result

                                

                                val accountType = document.getString("Account_type") ?: ""
                                val statusAccount = document.getString("Status_account") ?: ""

                                when (accountType) {
                                    "Donor" -> {
                                        val orgName = document.getString("OrganizationName") ?: ""
                                        val emailAccount = document.getString("email") ?: ""
                                        val userProfileImage = document.getString("profileImage") ?: ""

                                        // Save data to SharedPreferences
                                        SharedPreferences.saveEmail(context, emailAccount)
                                        SharedPreferences.saveStatus(context, accountType)
                                        SharedPreferences.saveOrgName(context, orgName)
                                        SharedPreferences.saveBasicType(context, statusAccount)
                                        SharedPreferences.saveProfileImageUri(context, userProfileImage)

                                                                            accountsRef.document("VerifiedAccounts")
                                .collection("Donor")
                                .document(email)
                                .update("fcmToken", fcmToken)
                                .addOnSuccessListener {
                                    Log.d("FCM", "FCM token updated successfully")
                                    
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("FCM", "Error updating FCM token", e)
                                }
                                
                                
                                    }
                                    "Recipient" -> {
                                        val firstName = document.getString("Recipient First Name") ?: ""
                                        val lastName = document.getString("Recipient Last Name") ?: ""
                                        val emailAccount = document.getString("email") ?: ""
                                        val userProfileImage = document.getString("profileImage") ?: ""

                                        SharedPreferences.saveEmail(context, emailAccount)
                                        SharedPreferences.saveStatus(context, accountType)
                                        SharedPreferences.saveFirstName(context, firstName)
                                        SharedPreferences.saveLastName(context, lastName)
                                        SharedPreferences.saveBasicType(context, statusAccount)
                                        SharedPreferences.saveProfileImageUri(context, userProfileImage)

                                        accountsRef.document("VerifiedAccounts")
                                .collection("Recipient")
                                .document(email)
                                .update("fcmToken", fcmToken)
                                .addOnSuccessListener {
                                    Log.d("FCM", "FCM token updated successfully")
                                    
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("FCM", "Error updating FCM token", e)
                                }
                                    }
                                     "Rider" -> {
                                    val firstName = document.getString("Rider First Name") ?: ""
                                    val lastName = document.getString("Rider Last Name") ?: ""
                                    val emailAccount = document.getString("email") ?: ""
                                    val userProfileImage = document.getString("profileImage") ?: ""

                                    SharedPreferences.saveEmail(context, emailAccount)
                                    SharedPreferences.saveStatus(context, accountType)
                                    SharedPreferences.saveFirstName(context, firstName)
                                    SharedPreferences.saveLastName(context, lastName)
                                    SharedPreferences.saveBasicType(context, statusAccount)
                                    SharedPreferences.saveProfileImageUri(context, userProfileImage)

                                   accountsRef.document("VerifiedAccounts")
                                .collection("Rider")
                                .document(email)
                                .update("fcmToken", fcmToken)
                                .addOnSuccessListener {
                                    Log.d("FCM", "FCM token updated successfully")
                                    
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("FCM", "Error updating FCM token", e)
                                }
                                }
                                }
                            }

                        } else {
                            errorMessage = "Wrong password"
                        }
                    }
                }
                .addOnFailureListener {
                    errorMessage = "Error checking account, please check your email or password"
                }
        }
    }

    // Check if no account was found after all checks
    if (!accountFound) {
        errorMessage = "Account does not exist"
    }
}



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 32.dp, top = 45.dp)
    ) {
        OutlinedTextField(
            value = organizationEmail,
            onValueChange = { organizationEmail = it },
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
            onValueChange = { organizationPassword = it },
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
            isError = errorMessage == "Wrong password"
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(start = 32.dp, end = 32.dp)
            )
        }

        // Determine the text color based on field content
        val signInTextColor = if (organizationEmail.isNotEmpty() && organizationPassword.isNotEmpty()) {
            Color.White
        } else {
            Color.White.copy(alpha = 0.2f)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 5.dp, bottom = 8.dp, end = 32.dp)
                .clickable(
                    enabled = organizationEmail.isNotEmpty() && organizationPassword.isNotEmpty()
                ) {
                    signInWithFirestore(organizationEmail, organizationPassword)
                }
                .background(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign In",
                color = signInTextColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
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
        
        Spacer(modifier = Modifier.width(60.dp))

        Text(
            text = "Sign In",
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
    }
}

