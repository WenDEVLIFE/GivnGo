package com.go.givngo.Extras;

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

;
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import android.util.Log

import com.go.givngo.SharedPreferences

import androidx.activity.compose.BackHandler


class VoucherMarket : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
                MyVoucher()
            }
        }
    }
}


@Composable
fun MyVoucher() {
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as VoucherMarket)

    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.Transparent.toArgb()

        WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            ?.isAppearanceLightStatusBars = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
    
    Image(
            painter = painterResource(id = R.drawable.bubbles),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize() 
                .background(Color.Transparent),
            contentScale = ContentScale.Crop
        )
        
        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color.Transparent,
            topBar = {
                TopBarVouch(onMenuClick = { activity.finish() }, isScrolled = scrollState.value > 0)
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
             
                CardPoints()
            }
        }
    }
}


@Composable
fun TopBarVouch(onMenuClick: () -> Unit, isScrolled: Boolean) {
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

        Spacer(modifier = Modifier.width(60.dp))

        Text(
            text = "Voucher Market",
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(bottom = 2.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun CardPoints() {
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
            .padding(end = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Points display box aligned to center start
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 20.dp)
            ) {
                if (userPoints != 0) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_glowcircle_active),
                        contentDescription = null,
                        modifier = Modifier.size(138.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_glowcircle),
                        contentDescription = null,
                        modifier = Modifier
                            .size(138.dp)
                            .graphicsLayer { alpha = 0.25f },
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = "$userPoints",
                    fontSize = 40.sp,
                    color = Color(0xFF8070F6),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // "Your Total Points" text aligned to center with additional padding
            Text(
                text = "Your Total Points",
                fontSize = 14.sp,
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.dp)
            )
        }
    }
}

