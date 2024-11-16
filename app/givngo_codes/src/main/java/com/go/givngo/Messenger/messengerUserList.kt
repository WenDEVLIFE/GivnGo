package com.go.givngo.Messenger;


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

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import android.util.Log

import com.go.givngo.SharedPreferences

import androidx.activity.compose.BackHandler

import androidx.compose.foundation.shape.CircleShape

import com.go.givngo.Messenger.messengerTalkList

import com.go.givngo.Model.UserDonor
import com.go.givngo.Model.UserRider

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FieldPath



class messengerUserList  : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)
        
        
        val accountType = intent.getStringExtra("accountType") ?: "No User"
        
        setContent {
            MyComposeApplicationTheme {
                MyAppUserList(accountType)
            }
        }
    }
}


@Composable
fun MyAppUserList(accountType: String) {
    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()
    val scrollState = rememberScrollState()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as messengerUserList)

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
                TopBarSettings(onMenuClick = { activity.finish() }, isScrolled = scrollState.value > 0)
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
             
             when (accountType) {
             "Donor" -> ChatListDonor()
             "Rider" -> ChatListRider()
             }
                
            }
        }
    }
}
@Composable
fun ChatListRider() {
    val db = FirebaseFirestore.getInstance()
    var userList by remember { mutableStateOf<List<UserDonor>>(emptyList()) }
    val context = LocalContext.current
    val emailRider = SharedPreferences.getEmail(context) ?: "developer@gmail.com"
    // Fetch users from Firestore
    LaunchedEffect(Unit) {
        db.collection("GivnGoChat") 
            .document(emailRider)
            .collection("Chat_To_Donor")
            .get()
            .addOnSuccessListener { documents ->
                val users = documents.map { doc ->
                
                    UserDonor(
                        documentId = doc.id,
                        user_email = doc.getString("donor_email") ?: "",
                        donor_org = doc.get(FieldPath.of("Bussiness/Organization_Name")) as? String ?: "",
                        profileImage = doc.getString("profileImage") ?: "",
                        fcmToken = doc.getString("fcmToken") ?: ""
                    )
                }
                userList = users
            }
            .addOnFailureListener {
                // Handle errors here
            }
    }

    // UI
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(userList) { user ->
            UserItemRider(user = user) { selectedUser ->
                // Navigate to ChatScreen with selectedUser details
                val intent = Intent(context, messengerTalkList::class.java).apply {
                   putExtra("userDocumentId", selectedUser.documentId)
                   putExtra("Account_Type", "RiderAccount")
                    putExtra("donorEmail", selectedUser.user_email)
                    putExtra("userOrgName", selectedUser.donor_org)
                    putExtra("donorProfileImg", selectedUser.profileImage) 
                    putExtra("fcmToken", selectedUser.fcmToken)
                }
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun ChatListDonor() {
    val db = FirebaseFirestore.getInstance()
    var userList by remember { mutableStateOf<List<UserRider>>(emptyList()) }
    val context = LocalContext.current
    val emailDonor = SharedPreferences.getEmail(context) ?: "developer@gmail.com"
    // Fetch users from Firestore
    LaunchedEffect(Unit) {
        db.collection("GivnGoChat") 
            .document(emailDonor)
            .collection("Chat_To_Rider")
            .get()
            .addOnSuccessListener { documents ->
                val users = documents.map { doc ->
                val userFirstName =  doc.getString("firstName") ?: ""
                val userLastName =  doc.getString("lastName") ?: ""
                    UserRider(
                        documentId = doc.id,
                        user_email = doc.getString("rider_email") ?: "",
                        user_firstName = userFirstName,
                        user_lastName =userLastName,
                        profileImage = doc.getString("profileImage") ?: "",
                        fcmToken = doc.getString("fcmToken") ?: ""
                    )
                }
                userList = users
            }
            .addOnFailureListener {
                // Handle errors here
            }
    }

    // UI
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(userList) { user ->
            UserItemDonor(user = user) { selectedUser ->
                // Navigate to ChatScreen with selectedUser details
                val intent = Intent(context, messengerTalkList::class.java).apply {
                   putExtra("userDocumentId", selectedUser.documentId)
                   putExtra("Account_Type", "DonorAccount")
                    putExtra("riderEmail", selectedUser.user_email) //rider email
                    putExtra("userFirstName", selectedUser.user_firstName)
                    putExtra("userLastName", selectedUser.user_firstName)
                    putExtra("donorProfileImg", selectedUser.profileImage) 
                    putExtra("fcmToken", selectedUser.fcmToken)
                }
                
                context.startActivity(intent)
            }
        }
    }
}



// Single User Item Composable
@Composable
fun UserItemDonor(user: UserRider, onUserClicked: (UserRider) -> Unit) {

Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
                        .clickable(onClick = { onUserClicked(user)  })
    ) {
    
    
    Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ) {
                AsyncImage(
                    model = user.profileImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clip(RoundedCornerShape(18.dp))
                        .background(color = Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(start = 14.dp)
                ) {
                    Text(
                        text = user.user_firstName + " " + user.user_lastName,
                        fontSize = 14.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    
                    Text(
                text = "Tap to chat",
                color = Color.Gray,
                fontSize = 10.sp
            )
                    
                }
            }
    }

}

@Composable
fun UserItemRider(user: UserDonor, onUserClicked: (UserDonor) -> Unit) {

Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp)
                        .clickable(onClick = { onUserClicked(user)  })
    ) {
    
    
    Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(20.dp)
            ) {
                AsyncImage(
                    model = user.profileImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clip(RoundedCornerShape(18.dp))
                        .background(color = Color.Gray),
                    contentScale = ContentScale.Crop
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(start = 14.dp)
                ) {
                    Text(
                        text = user.donor_org,
                        fontSize = 14.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    
                    Text(
                text = "Tap to chat",
                color = Color.Gray,
                fontSize = 10.sp
            )
                    
                }
            }
    }
}


@Composable
fun TopBarSettings(onMenuClick: () -> Unit, isScrolled: Boolean) {
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

        Spacer(modifier = Modifier.width(78.dp))

        Text(
            text = "My Chats",
            color = Color(0xFF8070F6),
            modifier = Modifier.padding(bottom = 2.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
