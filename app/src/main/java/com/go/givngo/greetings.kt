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
import com.go.givngo.ridersSide.RegistrationRiders


class greetings : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)

        
        setContent {
            val context = LocalContext.current
            
            val finishBasic = SharedPreferences.getStatusType(context) ?: "Developer"

            when (finishBasic) {

                "Donor" -> {

                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                }
                "Recipient" -> {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
                "Rider" -> {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)

                }"Developer" -> {
                MyComposeApplicationTheme {
                    MyAppGreetings()
                }
            }

            }
        }
    }
}



@Composable
fun MyAppGreetings() {
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var isExpanded by remember { mutableStateOf(true) }
    var isGetStartedVisible by remember { mutableStateOf(true) }
    var isDescriptionVisible by remember { mutableStateOf(true) }
    var isFadingOut by remember { mutableStateOf(false) }

    var showDisclaimer by remember { mutableStateOf(true) }
    var disclaimerAlpha by remember { mutableStateOf(1f) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            // Handle the result from the launched activity
        }
    )

    val activity = (context as greetings)

    val images = listOf(
        R.drawable.transition_one,
        R.drawable.transition_two
    )
    var currentImageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(10000) // Change image every 10 seconds
            currentImageIndex = (currentImageIndex + 1) % images.size
        }
    }

    // Timer for the disclaimer with fade-out animation
    LaunchedEffect(showDisclaimer) {
        if (showDisclaimer) {
            delay(8000) // Wait 8 seconds before starting fade-out
            val fadeOutDuration = 2000 // 2 seconds for fade-out
            val stepDelay = fadeOutDuration / 20 // 20 steps to fade out
            repeat(20) {
                delay(stepDelay.toLong())
                disclaimerAlpha -= 0.05f // Gradually reduce alpha
            }
            disclaimerAlpha = 0f
            showDisclaimer = false
        }
    }

    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            ?.isAppearanceLightStatusBars = false
    }

    if (showDisclaimer) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = disclaimerAlpha)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "GivnGo is a personal project created for testing purposes on the Google Play Console. It is not affiliated with any organization, business, or registered entity.",
                  textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6.copy(color = Color.Black),
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        Crossfade(targetState = images[currentImageIndex], animationSpec = tween(durationMillis = 1000)) { image ->
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentScale = ContentScale.Crop
            )
        }

        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color.Transparent,
            topBar = { TopBarGreetings() },
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Crossfade(targetState = isExpanded, animationSpec = tween(durationMillis = 1000)) { expanded ->
                        greetingsTitle(
                            isExpanded = expanded,
                            isDescriptionVisible = isDescriptionVisible,
                            onTransitionEnd = {
                                isGetStartedVisible = true
                                isDescriptionVisible = true
                            },
                            isFadingOut = isFadingOut
                        )
                    }
                }

                asOptions(isVisible = isFadingOut)

                // Align Get Started and Sign in buttons at the bottom
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 60.dp)
                        .align(Alignment.BottomCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    getStarted(
                        isVisible = isFadingOut,
                        onClick = {
                            isFadingOut = true
                        }
                    )

                    signIn(
                        isVisible = isFadingOut,
                        onClick = {
                            val intent = Intent(context, signInActivity::class.java)
                            launcher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}



@Composable
fun signInPart(isVisible: Boolean, onClick: () -> Unit) {
val context = LocalContext.current
val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
    onResult = { result -> 
        // Handle the result from the launched activity
    })
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp,start = 55.dp, end = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
    AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
        
        Text(
            text = "Sign in",
            color = Color.White,
            modifier = Modifier.padding(bottom = 400.dp).clickable { onClick() },
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        }
        
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
        
        }
        
        }
        
        }
        
    


@Composable
fun greetingsTitle(
    isExpanded: Boolean,
    isDescriptionVisible: Boolean,
    onTransitionEnd: () -> Unit,
    isFadingOut: Boolean
) {
    val paddingTop by animateDpAsState(
        targetValue = if (isFadingOut) 80.dp else 240.dp,
        animationSpec = tween(durationMillis = 1000),
        finishedListener = {
            onTransitionEnd()
        }
    )

    var text by remember { mutableStateOf("GivnGo") }
    var textSize by remember { mutableStateOf(52.sp) }
    var descriptionText by remember { mutableStateOf("Give with a smile and Go with a heart!") }
    
    val textAlpha by animateFloatAsState(
        targetValue = if (isFadingOut) 0f else 1f,
        animationSpec = tween(durationMillis = 1000),
        finishedListener = {
            if (isFadingOut) {
                text = ""
                descriptionText = ""
                textSize = 30.sp
            }
        }
    )
   

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Title with fade-out transition
        Text(
            text = text,
            color = Color.White.copy(alpha = textAlpha),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingTop, bottom = 6.dp),
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        // Description text with fade-out transition
        AnimatedVisibility(
            visible = isDescriptionVisible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Text(
                text = descriptionText,
                color = Color.White.copy(alpha = textAlpha),
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp, start = 32.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun getStarted(isVisible: Boolean, onClick: () -> Unit) {
    var text by remember { mutableStateOf("Get Started") }
    val textAlpha by animateFloatAsState(
        targetValue = if (!isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        finishedListener = {
            // Hide text when the animation finishes and the button is no longer visible
            if (!isVisible) text = "Get Started" else text = ""
        }
    )
    val backgroundAlpha by animateFloatAsState(
        targetValue = if (!isVisible) 0.12f else 0.0f,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 55.dp, end = 55.dp)
            .background(
                color = Color.White.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(30.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White.copy(alpha = textAlpha),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}



@Composable
fun signIn(isVisible: Boolean, onClick: () -> Unit) {

        
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(top = 8.dp)
            .clickable { onClick() }
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(30.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Sign in",
            color = Color(0xFF8070F6),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
}

}

@Composable
fun asOptions(isVisible: Boolean) {
val context = LocalContext.current
val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
    onResult = { result -> 
        // Handle the result from the launched activity
    })
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp,start = 55.dp, end = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
    AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
    Text(
            text = "Get Started",
            color = Color.White,
            modifier = Modifier.padding(bottom = 150.dp),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        }
        
    AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
    Text(
            text = "Create account as",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        }
        // Rider
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable {
                val intent = Intent(context, RegistrationRiders::class.java)
                launcher.launch(intent)
            }
                    .padding(bottom = 8.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Rider",
                    color = Color(0xFF483CA2),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Recipient
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 8.dp)
                     .clickable {
                val intent = Intent(context, RegistrationRecipient::class.java)
                launcher.launch(intent)
            }
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Recipient",
                    color = Color(0xFF8677F6),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Donor
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                val intent = Intent(context, RegistrationDonor::class.java)
                launcher.launch(intent)
            }
                    .height(60.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Donor",
                    color = Color(0xFF7163D9),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




@Composable
fun TopBarGreetings() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
    }
}
