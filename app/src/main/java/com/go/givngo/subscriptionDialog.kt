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
import java.util.UUID
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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.remember

import com.android.billingclient.api.*

import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView

 class subscriptionDialog : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
                MyAppSubscription()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyAppSubscription() {
    val scaffoldState = rememberScaffoldState()
    val currentStep = remember { mutableStateOf(0) }
    val selectedSub = remember { mutableStateOf<String?>(null) }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        topBar = {
            TopBarSubscription()
        },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
        
        Image(
            painter = painterResource(id = R.drawable.gradient_one),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentScale = ContentScale.Crop
        )
            AnimatedContent(
                targetState = currentStep.value,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) with slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
                }
            ) { step ->
                when (step) {
                    0 -> subscriptionOptions(
                        onNext = {
                            selectedSub.value = it
                            currentStep.value++
                        }
                    )
                    1 -> subscriptionFillMock(selectedSubscription = selectedSub.value.orEmpty())
                }
            }
        }
    }
}

@Composable
fun subscriptionOptions(onNext: (String) -> Unit) {
    val selectedSubscription = remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp, top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_premium),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Break The Limit!",
                color = Color(0xFF9070EF),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Unlock higher claim limits by subscribing weekly, monthly, or yearly. Each subscription renews your claim allowance every week, ensuring you never miss out!",
                color = Color(0xFF8070F6),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            FeatureSubscription(
                "Yearly",
                "features 10 claim limits per week",
                "₱5,525/Yearly",
                selectedSubscription
            )

            FeatureSubscription(
                "Monthly",
                "7 claim limits per week",
                "₱450/Monthly",
                selectedSubscription
            )

            FeatureSubscription(
                "Weekly",
                "5 claim limits per week",
                "₱125/Weekly",
                selectedSubscription
            )

            NextButton(
                isEnabled = selectedSubscription.value != null,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp),
                onClick = { onNext(selectedSubscription.value ?: "") }
            )
        }
    }
}

class WebAppInterface(
    private val context: Context,
    private val onPurchaseCompleteCallback: (String) -> Unit
) {

    @JavascriptInterface
    fun onPurchaseComplete(transactionId: String, status: String) {
        // Update the UI or perform actions based on the completion
        val message = when (status) {
            "Yearly" -> "Yearly subscription purchased successfully! Transaction ID: $transactionId"
            "Monthly" -> "Monthly subscription purchased successfully! Transaction ID: $transactionId"
            "Weekly" -> "Weekly subscription purchased successfully! Transaction ID: $transactionId"
            else -> "Unknown subscription status."
        }
        onPurchaseCompleteCallback(message)
    }
}

@Composable
fun subscriptionFillMock(selectedSubscription: String) {
    var purchaseStatus by remember { mutableStateOf("Waiting for user action...") }
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    
    val url = when (selectedSubscription) {
                    "Yearly" -> "https://feedsbeta.vercel.app/client/subscription/yearly/checkout/"
                    "Monthly" -> "https://feedsbeta.vercel.app/client/subscription/daily/checkout/"
                    "Weekly" -> "https://feedsbeta.vercel.app/client/subscription/weekly/checkout/"
                    else -> null
                }

                if (url != null) {
                    purchaseStatus = "Opening checkout for $selectedSubscription..."
                    webView.loadUrl(url)
                } else {
                    purchaseStatus = "No valid subscription selected."
                }
                
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Simulated Billing for $selectedSubscription",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                
            }) {
                Text(text = "Pay Purchase")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = purchaseStatus, color = Color.Gray)
        }
    }

    // WebView integration
    AndroidView(
        factory = {
            webView.apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                addJavascriptInterface(
                    WebAppInterface(context) { message ->
                        // Update purchaseStatus in the UI
                        purchaseStatus = message
                    },
                    "AndroidInterface"
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun subscriptionFill(selectedSubscription: String) {
    val context = LocalContext.current

    // Trigger Google Play Billing
    LaunchedEffect(Unit) {
        val billingClient = BillingClient.newBuilder(context)
            .setListener { billingResult, purchases ->
                // Handle purchase results here
            }
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                // Handle disconnection
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Fetch and launch the appropriate subscription
                    val skuList = when (selectedSubscription) {
                        "Yearly" -> listOf("yearly_subscription_id")
                        "Monthly" -> listOf("monthly_subscription_id")
                        "Weekly" -> listOf("weekly_subscription_id")
                        else -> emptyList()
                    }

                    val params = SkuDetailsParams.newBuilder()
                        .setSkusList(skuList)
                        .setType(BillingClient.SkuType.SUBS)
                        .build()

                    billingClient.querySkuDetailsAsync(params) { result, skuDetailsList ->
                        if (result.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                            skuDetailsList.firstOrNull()?.let { skuDetails ->
                                val flowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build()
                                billingClient.launchBillingFlow(context as subscriptionDialog, flowParams)
                            }
                        }
                    }
                }
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Launching Google Play Billing for $selectedSubscription",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }

}




@Composable
fun FeatureSubscription(
    featureTitle: String,
    featureDesc: String,
    featurePrice: String,
    selectedSubscription: MutableState<String?>
) {
    val isChecked = selectedSubscription.value == featureTitle

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
            .background(if (isChecked) Color(0xFF9070EF) else Color(0xFFDFD7FF))
            .clickable {
                selectedSubscription.value = if (isChecked) null else featureTitle
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 6.dp, bottom = 6.dp, start = 6.dp, end = 6.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
                .background(if (isChecked) Color(0xFFE5DDFF) else Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 12.dp)
            ) {
                // Checkbox
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        selectedSubscription.value = if (isChecked) null else featureTitle
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF8070F6),
                        uncheckedColor = Color(0xFF8B6DEC)
                    )
                )

                Spacer(modifier = Modifier.width(6.dp))

                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
                ) {
                    Text(
                        text = featureTitle,
                        color = Color(0xFF453081),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = featureDesc,
                        color = Color(0xFFA497CB),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = featurePrice,
                        color = Color(0xFF453081),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun NextButton(
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(45.dp)
            .width(120.dp)
            .clip(RoundedCornerShape(25.dp))
            .background(
                color = if (isEnabled) Color(0xFF9070EF) else Color(0xFFDFD7FF)
            )
            .clickable(enabled = isEnabled) {
                // Transition to the next screen
                onClick() 
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Next",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}



 /*
@Composable
fun subscriptionFill() {
    var isWeeklySelected by remember { mutableStateOf(true) } // Initial state for weekly selection
    var isYearlySelected by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Image
            Image(
                painter = painterResource(id = R.drawable.premiumoffer),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Title Text
            Text(
                text = "Get QRCode Scanner pro with Full access of Pro Features",
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Features Section
            FeatureItem(
                icon = R.drawable.ic_noadds,
                text = "No more annoying ads"
            )
            FeatureItem(
                icon = R.drawable.ic_multiplescan,
                text = "Batch Scanning Multiple qr scan"
            )
            FeatureItem(
                icon = R.drawable.ic_analyticspro,
                text = "Have your own Qr Analytics Each Generated qr code"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Weekly Access Section
            SubscriptionOption(
                title = "WEEKLY ACCESS",
                price = "$5.30",
                description = "Per Week",
                backgroundResource = if (isWeeklySelected) R.drawable.pro_glowingbutton else R.drawable.googlebutton,
                isSelected = isWeeklySelected
            ) {
                isWeeklySelected = true
                isYearlySelected = false
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Best Offer Text
            Text(
                text = "BEST OFFER",
                color = Color(0xFFFFBF3E),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )

            // Yearly Access Section
SubscriptionOption(
    title = "YEARLY ACCESS",
    price = "$2.50",
    description = "Per Week",
    secondaryDescription = "Only 30 dollars Yearly!",
    backgroundResource = if (isYearlySelected) R.drawable.pro_glowingbutton else R.drawable.googlebutton,
    isSelected = isYearlySelected
) {
    // Update selection state
    isYearlySelected = true
    isWeeklySelected = false
}

// Spacer for spacing
Spacer(modifier = Modifier.height(10.dp))

// Subscribe Section
SubscriptionOption(
    title = "SUBSCRIBE",
    price = "WITH 3 DAYS",
    description = "FREE TRIAL",
    backgroundResource = R.drawable.pro_glowingbutton,
    isPrimary = true,
    isSelected = isYearlySelected // Highlight "SUBSCRIBE" if related to "YEARLY ACCESS"
) {
    // Update selection state for the subscribe option
    isYearlySelected = true
    isWeeklySelected = false
}

        }
    }
}

@Composable
fun FeatureItem(icon: Int, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .background(MaterialTheme.colors.surface, CircleShape)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = MaterialTheme.colors.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SubscriptionOption(
    title: String,
    price: String,
    description: String,
    secondaryDescription: String? = null,
    backgroundResource: Int,
    isPrimary: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFF3F4F6), Color(0xFFEFEFEF))
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colors.primary,
                fontSize = if (isPrimary) 18.sp else 15.sp,
                fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Normal
            )
            if (secondaryDescription != null) {
                Text(
                    text = secondaryDescription,
                    color = MaterialTheme.colors.secondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = price,
                color = MaterialTheme.colors.primary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = MaterialTheme.colors.primary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Composable
fun SubscriptionOption(
    title: String,
    price: String,
    unit: String, // New field for specifying the unit (e.g., "Per Week")
    secondaryDescription: String? = null,
    backgroundResource: Int,
    isPrimary: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .background(
                brush = if (isSelected) Brush.horizontalGradient(
                    colors = listOf(Color(0xFF1E90FF), Color(0xFF00BFFF))
                ) else Brush.horizontalGradient(
                    colors = listOf(Color(0xFFF3F4F6), Color(0xFFEFEFEF))
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = if (isSelected) Color.White else MaterialTheme.colors.primary,
                fontSize = if (isPrimary) 18.sp else 15.sp,
                fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Normal
            )
            if (secondaryDescription != null) {
                Text(
                    text = secondaryDescription,
                    color = if (isSelected) Color.White else MaterialTheme.colors.secondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = price,
                color = if (isSelected) Color.White else MaterialTheme.colors.primary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = unit, // Display the new unit field
                color = if (isSelected) Color.White else MaterialTheme.colors.primary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
*/

@Composable
fun TopBarSubscription() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
    }
}
