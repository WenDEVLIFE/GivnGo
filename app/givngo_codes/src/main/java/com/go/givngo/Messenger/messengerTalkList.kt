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
import com.google.firebase.messaging.FirebaseMessaging

import com.google.firebase.messaging.RemoteMessage

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.filled.Send

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.widget.Toast
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.google.firebase.firestore.ListenerRegistration

import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.Query

class messengerTalkList  : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    
        super.onCreate(savedInstanceState)
        
        val documentId = intent.getStringExtra("userDocumentId") ?: ""
        val accountType = intent.getStringExtra("Account_Type") ?: ""
        val riderEmail = intent.getStringExtra("riderEmail") ?: ""
        val riderFName = intent.getStringExtra("userFirstName") ?: ""
        val riderLName = intent.getStringExtra("userLastName") ?: ""
        val riderFCMToken = intent.getStringExtra("fcmToken") ?: ""
        
        val donorEmail = intent.getStringExtra("donorEmail") ?: ""
        val donorPImg = intent.getStringExtra("donorProfileImg") ?: ""
        val orgNameDonor = intent.getStringExtra("userOrgName") ?: ""
        val donorFCMToken = intent.getStringExtra("fcmToken") ?: ""
        
        
        setContent {
            MyComposeApplicationTheme {
            when (accountType)  {
            
            "DonorAccount" -> {
            
           MyAppConversationToRider(
                documentId,riderEmail,riderFName,riderLName,donorPImg,riderFCMToken)
           
            }
            
            "RiderAccount" -> {
            
            MyAppConversationToDonor(
                documentId,donorEmail,orgNameDonor,donorPImg,donorFCMToken)
            
            }
            
            }
                
            }
        }
        
        
    }
}





data class ChatMessage(
var message: String,
var timestamp: String,
var sender_email: String,
var chat_type: String,
var request_image: String,
var donation_name: String
) 

@OptIn(ExperimentalUnitApi::class)
@Composable
fun MyAppConversationToRider(
    docId: String,
    riderEmail: String,
    userFN: String,
    userLN: String,
    donorProfileImage: String,
    donorFCMToken: String
) {
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as messengerTalkList)
    
    Toast.makeText(context, "Current Document: $docId", Toast.LENGTH_SHORT).show()

    val emailDonor = SharedPreferences.getEmail(context) ?: "developer@gmail.com"
    val nameDonor = SharedPreferences.getOrgName(context) ?: "Developer"
    var messages = remember { mutableStateListOf<ChatMessage>() }

    val db = FirebaseFirestore.getInstance()
    val chatCollection = db.collection("GivnGoChat")
        .document(emailDonor)
        .collection("Chat_To_Rider")
        .document(docId)
        .collection("Chats")

    // Fetch and format timestamp
    fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy, hh:mm a z", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date(timestamp))
    }

    // Fetch messages on activity entry
    fun fetchMessagesOnActivityEntry() {
        chatCollection.orderBy("timestamp", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { snapshot ->
                messages.clear()
                snapshot?.forEach { document ->
                    messages.add(
                        ChatMessage(
                            sender_email = document.getString("sender_email") ?: "",
                            message = document.getString("message") ?: "",
                             timestamp = document.getString("timestamp") ?: "",
                                chat_type = document.getString("chat_type") ?: "",
                                request_image = document.getString("request_image") ?: "",
                                donation_name = document.getString("donation_name") ?: ""
                            )
                    )
                }
            }
            .addOnFailureListener { error ->
                Log.e("Firestore", "Error fetching messages: ${error.localizedMessage}")
            }
    }

    // Add realtime listener
    fun addRealtimeListener() {
        chatCollection.orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listener error: ${error.localizedMessage}")
                    return@addSnapshotListener
                }
                snapshot?.let {
                    messages.clear()
                    it.forEach { document ->
                        messages.add(
                            ChatMessage(
                                sender_email = document.getString("sender_email") ?: "",
                                message = document.getString("message") ?: "",
                                 timestamp = document.getString("timestamp") ?: "",
                                chat_type = document.getString("chat_type") ?: "",
                                request_image = document.getString("request_image") ?: "",
                                donation_name = document.getString("donation_name") ?: ""
                            )
                        )
                    }
                }
            }
    }

    // Send a message
    fun sendMessageToFirestore(message: String) {
        val timestamp = System.currentTimeMillis()
        val chatMessage = ChatMessage(
            sender_email = emailDonor,
             message = message,
            timestamp = formatTimestamp(timestamp),
            chat_type = "",
            request_image = "",
            donation_name = ""
            )

        chatCollection.add(chatMessage)
            .addOnSuccessListener {
                Log.d("Firestore", "Message sent successfully")
            }
            .addOnFailureListener { error ->
                Log.e("Firestore", "Error sending message: ${error.localizedMessage}")
            }
    }

    // Lifecycle
    LaunchedEffect(Unit) {
        fetchMessagesOnActivityEntry()
        addRealtimeListener()
    }

    DisposableEffect(Unit) {
        onDispose {
            // Cleanup logic if required
        }
    }

    // UI
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBarTalkListToDonor(
                onMenuClick = { activity.finish() },
                isScrolled = scrollState.value > 0,
                userFN,
                donorProfileImage
            )
        }
    ) { paddingValues ->
        ChatScreen(
            currentEmail = emailDonor,
            messages = messages.sortedByDescending { it.timestamp },
            onSendMessage = { message -> sendMessageToFirestore(message) }
        )
    }
}



@OptIn(ExperimentalUnitApi::class)
@Composable
fun MyAppConversationToDonor(
    docId: String,
    riderEmail: String,
    orgDonorName: String,
    donorProfileImage: String,
    riderFCMtoken: String
) {
    val scrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val activity = (context as messengerTalkList)
    
    Toast.makeText(context, "Current Document: $docId", Toast.LENGTH_SHORT).show()
    
    val emailDonor = SharedPreferences.getEmail(context) ?: "developer@gmail.com"
    val nameDonor = SharedPreferences.getOrgName(context) ?: "Developer"
    var messages = remember { mutableStateListOf<ChatMessage>() }

    val db = FirebaseFirestore.getInstance()
    val chatCollection = db.collection("GivnGoChat")
        .document(riderEmail)
        .collection("Chat_To_Rider")
        .document(docId)
        .collection("Chats")

    // Fetch and format timestamp
    fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy, hh:mm a z", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date(timestamp))
    }

    // Fetch messages on activity entry
    fun fetchMessagesOnActivityEntry() {
        chatCollection.orderBy("timestamp", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { snapshot ->
                messages.clear()
                snapshot?.forEach { document ->
                    messages.add(
                        ChatMessage(
                            sender_email = document.getString("sender_email") ?: "",
                            message = document.getString("message") ?: "",
                             timestamp = document.getString("timestamp") ?: "",
                                chat_type = document.getString("chat_type") ?: "",
                                request_image = document.getString("request_image") ?: "",
                                donation_name = document.getString("donation_name") ?: ""
                            )
                    )
                }
            }
            .addOnFailureListener { error ->
                Log.e("Firestore", "Error fetching messages: ${error.localizedMessage}")
            }
    }

    // Add realtime listener
    fun addRealtimeListener() {
        chatCollection.orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listener error: ${error.localizedMessage}")
                    return@addSnapshotListener
                }
                snapshot?.let {
                    messages.clear()
                    it.forEach { document ->
                        messages.add(
                            ChatMessage(
                                sender_email = document.getString("sender_email") ?: "",
                                message = document.getString("message") ?: "",
                                timestamp = document.getString("timestamp") ?: "",
                                chat_type = document.getString("chat_type") ?: "",
                                request_image = document.getString("request_image") ?: "",
                                donation_name = document.getString("donation_name") ?: ""
                            )
                        )
                    }
                }
            }
    }

    // Send a message
    fun sendMessageToFirestore(message: String) {
        val timestamp = System.currentTimeMillis()
        val chatMessage = ChatMessage(
            sender_email = emailDonor,
            message = message,
            timestamp = formatTimestamp(timestamp),
            chat_type = "",
            request_image = "",
            donation_name = ""
        )

        chatCollection.add(chatMessage)
            .addOnSuccessListener {
                Log.d("Firestore", "Message sent successfully")
            }
            .addOnFailureListener { error ->
                Log.e("Firestore", "Error sending message: ${error.localizedMessage}")
            }
    }

    // Lifecycle
    LaunchedEffect(Unit) {
        fetchMessagesOnActivityEntry()
        addRealtimeListener()
    }

    DisposableEffect(Unit) {
        onDispose {
            // Cleanup logic if required
        }
    }

    // UI
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBarTalkListToDonor(
                onMenuClick = { activity.finish() },
                isScrolled = scrollState.value > 0,
                orgDonorName,
                donorProfileImage
            )
        }
    ) { paddingValues ->
        ChatScreen(
            currentEmail = emailDonor,
            messages = messages.sortedByDescending { it.timestamp },
            onSendMessage = { message -> sendMessageToFirestore(message) }
        )
    }
}



@Composable
fun ChatScreen(
    currentEmail: String,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding() // Adjust for keyboard
            .navigationBarsPadding() 
    ) {
        // Chat Messages
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true // Display bottom-to-top
        ) {
            items(messages) { message ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp) // Add space between messages
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (message.sender_email == currentEmail) {
                            Arrangement.End
                        } else {
                            Arrangement.Start
                        }
                    ) {
                        if (message.sender_email == currentEmail) {
                            SenderMessage(message.message ?: "", message.chat_type ?: "", message.request_image ?: "", message.donation_name ?: "")
                        } else {
                            RecipientMessage(message.message ?: "",message.chat_type ?: "", message.request_image ?: "", message.donation_name ?: "")
                        }
                    }
                }
            }
        }

        // Input Field and Send Button
    Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp, bottom = 4.dp), // Added space at the bottom of the row
    verticalAlignment = Alignment.CenterVertically
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(end = 8.dp)
            .height(40.dp) // Set a smaller height for the box
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 8.dp) // Padding inside the box
    ) {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Type a message") }, // Replaces the label
            singleLine = true,
            textStyle = TextStyle(textAlign = TextAlign.Start), // Align text to the left
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent, // Transparent background
                unfocusedIndicatorColor = Color.Transparent, // No underline
                focusedIndicatorColor = Color.Transparent, // No underline
                textColor = Color.Black
            )
        )
    }

    IconButton(onClick = {
        if (messageText.isNotBlank()) {
            onSendMessage(messageText)
            messageText = ""
        }
    }) {
        Icon(Icons.Default.Send, contentDescription = "Send")
    }
}


    
    }
}


@Composable
fun SenderMessage(message: String, chattype: String, thumbnail: String, packageName: String){
    // Display message from the sender (right side)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.End // Align to the right
    ) {
    
    if(chattype == "Request"){
    Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFF4F3FE), // Blue for sender
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(8.dp)
        ) {
        
        val thumbnail =  Uri.parse(thumbnail).toString() 
Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding( bottom = 4.dp)
    ) {
    
        
        AsyncImage(
                    model = thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color.Gray),
                    contentScale = ContentScale.Crop
                )
                
            Text(
                text = "You made a delivery request of donation $packageName",
                color = Color.Black,
                modifier = Modifier.width(200.dp).padding(top = 4.dp, bottom = 8.dp),
                fontSize = 12.sp,
                style = MaterialTheme.typography.body1
            )
            
            }
        }
    }else{
    Box(
            modifier = Modifier
                .background(
                    color = Color(0xFF0078FF), // Blue for sender
                    shape = RoundedCornerShape(25.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message,
                color = Color.White,
                style = MaterialTheme.typography.body1
            )
        }
    }
    
        
    }
}

@Composable
fun RecipientMessage(message: String,chattype: String, thumbnail: String, packageName: String){
    // Display message from the recipient (left side)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.Start // Align to the left
    ) {
    
        if(chattype == "Request"){
        
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFF4F3FE), // Blue for sender
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(8.dp)
        ) {
        
        val thumbnail =  Uri.parse(thumbnail).toString() 
Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding( bottom = 4.dp)
    ) {
    
        
        AsyncImage(
                    model = thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color.Gray),
                    contentScale = ContentScale.Crop
                )
                
            Text(
                text = "Delivery Request with Donation Name: $packageName",
                color = Color.Black,
                modifier = Modifier.width(200.dp).padding(top = 4.dp, bottom = 8.dp),
                fontSize = 12.sp,
                style = MaterialTheme.typography.body1
            )
            
            }
        }
    }else{
    Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFE0E0E0), // Light gray for recipient
                    shape = RoundedCornerShape(25.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message,
                color = Color.Black,
                style = MaterialTheme.typography.body1
            )
        }
    
    }
        
    }
}



@Composable
fun TopBarTalkListToDonor(onMenuClick: () -> Unit, isScrolled: Boolean, userOrgName: String, userDonorImg: String) {
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
        
        Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = userDonorImg,
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(color = Color.Gray),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(6.dp))
                
                Text(
                        text = userOrgName,
                        fontSize = 12.sp,
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    
                }
    }
}

@Composable
fun TopBarTalkListToRider(onMenuClick: () -> Unit, isScrolled: Boolean) {
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

        
    }
}
