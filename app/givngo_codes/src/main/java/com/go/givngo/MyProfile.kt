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
import android.widget.Toast
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


import androidx.compose.material.ripple.rememberRipple

class MyProfile : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
                MyProfileAccount()
            }
        }
    }
}


@Composable
fun MyProfileAccount() {
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as MyProfile)

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
                TopBarMyProfile(onMenuClick = { activity.finish() }, isScrolled = scrollState.value > 0)
            },
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
             
                MyProfileInfo()
            }
        }
    }
}
@Composable
fun MyProfileInfo() {
    val context = LocalContext.current
    val profileImageUri = SharedPreferences.getProfileImageUri(context) ?: "Developer"
    val profileImageUriParsed = Uri.parse(profileImageUri)
    val statusUser = SharedPreferences.getStatusType(context) ?: "Developer"
    val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"
    val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
    val userFirstName = SharedPreferences.getFirstName(context) ?: "BasicAccounts"
    val userLastName = SharedPreferences.getLastName(context) ?: "BasicAccounts"
    val companyDName = SharedPreferences.getOrgName(context) ?: "BasicAccounts"



// Recipient
var userFName: String = ""
var userLName: String = ""
var locationAddress: String = ""
var phonenumber: String = ""
var recipientBio: String = ""

// Donor
var donorName: String = ""
var bussinessAddress: String = ""
var companyEstablished: String = ""
var companyPhone: String = ""
var donorBio: String = ""


    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(userCurrentSignedInEmail) {
        if (userCurrentSignedInEmail.isNotEmpty()) {
        
            firestore.collection("GivnGoAccounts")
                .document(userAccountType)
                .collection(statusUser)
                .document(userCurrentSignedInEmail)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        when (statusUser) {
                            "Donor" -> {
                                donorName = document.getString("OrganizationName") ?: ""
                                bussinessAddress = document.getString("address") ?: ""
                                companyPhone = document.getString("fullContact") ?: ""
                                donorBio = document.getString("bio") ?: ""
                                
                                Toast.makeText(context, "Hello Donor!" , Toast.LENGTH_LONG).show()
                            }
                            "Recipient" -> {
                                userFName = document.getString("Recipient First Name") ?: ""
                                userLName = document.getString("Recipient Last Name") ?: ""
                                locationAddress = document.getString("address") ?: ""
                                phonenumber = document.getString("fullContact") ?: ""
                                recipientBio = document.getString("bio") ?: ""
                                
                                Toast.makeText(context, "Hello $userFName!" , Toast.LENGTH_LONG).show()
                            }
                            "Volunteer" -> {
                                
                            }
                            else -> {}
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error 303: " + exception, Toast.LENGTH_LONG).show()
                }
        }
    }
    
    
    Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(color = Color(0xFF3C356C))
        )

    Column(modifier = Modifier
            .fillMaxSize()
                .padding(top = 155.dp)){
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 13.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(126.dp)
                    .background(color = Color.White, shape = CircleShape)
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = profileImageUriParsed,
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            

            Box(
                modifier = Modifier
                    .height(70.dp)
                    .padding(start = 24.dp, top = 35.dp)
                    .background(
                        color = Color(0xFF917BFF),
                        shape = RoundedCornerShape(topStart = 25.dp, bottomStart = 25.dp)
                    )
                    .align(Alignment.CenterVertically)
            ) {
            
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "$statusUser Account!",
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            
        }

        Column(
            modifier = Modifier.padding(top = 3.dp, start = 12.dp)
        ) {
            when (statusUser) {
                "Donor" -> {
                    Text(
                        text = "$userFirstName $userLastName ",
                        fontSize = 22.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$recipientBio",
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "EditButton",
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 25.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(color = Color.Gray, bounded = false)
                                ) {}
                        )
                    }
                }
                "Recipient" -> {
                    Text(
                        text = "$companyDName - Org Account",
                        fontSize = 22.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$donorBio",
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "EditButton",
                            modifier = Modifier
                                .size(25.dp)
                                .padding(end = 25.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(color = Color.Gray, bounded = false)
                                ) {}
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(color = Color(0xFFFBF8FF))
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "My Informations",
                fontSize = 18.sp,
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            when (statusUser) {
                "Donor" -> {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.company_address),
                            contentDescription = null,
                            modifier = Modifier
                            
                                .size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Company Address: $bussinessAddress",
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.company_date),
                            contentDescription = null,
                            modifier = Modifier

                                .size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Company Date Established: ",
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }
                }
                "Recipient" -> {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_location_one),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Location Address: $locationAddress",
                            fontSize = 13.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_phone),
                            contentDescription = null,
                            modifier = Modifier
                                
                                .size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Date of birth: ",
                            fontSize = 13.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }
                }
                }
            }
                            Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(color = Color(0xFFFBF8FF))
        )
        
        Column(
            modifier = Modifier
                                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "My Contacts",
                fontSize = 18.sp,
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            when (statusUser) {
                "Donor" -> {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_phone),
                            contentDescription = null,
                            modifier = Modifier
                            
                                .size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Company Contact No. : $companyPhone",
                            fontSize = 14.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_mail),
                            contentDescription = null,
                            modifier = Modifier
                                
                                .size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Email: $userCurrentSignedInEmail",
                            fontSize = 13.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }
                }
                "Recipient" -> {
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_phone),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Contact Number: $phonenumber",
                            fontSize = 13.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_mail),
                            contentDescription = null,
                            modifier = Modifier
                                
                                .size(20.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Email: $userCurrentSignedInEmail",
                            fontSize = 13.sp,
                            color = Color.Black.copy(alpha = 0.90f),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }
                }
                
                }
        
        }
    }
}

@Composable
fun TopBarMyProfile(onMenuClick: () -> Unit, isScrolled: Boolean) {
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

        Spacer(modifier = Modifier.width(85.dp))

        Text(
            text = "My Profile",
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(bottom = 2.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


