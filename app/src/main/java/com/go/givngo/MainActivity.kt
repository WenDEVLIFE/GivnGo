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
                        MyAppRider(userFinishBasic = finishBasic)
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

sealed class RiderNavigationScreen(val route: String, val iconResId: Int, val title: String) {
    object Home : RiderNavigationScreen("Home", R.drawable.ic_homeheart, "Home")
    object Deliv : RiderNavigationScreen("Deliveries", R.drawable.ic_deliveries, "Deliveries")
    object MyRoutes : RiderNavigationScreen("My Routes", R.drawable.ic_deliveries, "My Routes")
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
fun searchBarDonations(){


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


