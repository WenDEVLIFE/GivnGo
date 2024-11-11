package com.go.givngo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CardPoints() {
    val newUser = "Start Earning Points Today!"
    val bottomInstructions = "Start by posting a donation or claim points on my points section below."

    val oldUser = "Keep Going, points can get higher and higher!"
    val newBottonInstruction = "You can use the points to claim free vouchers by heading to voucher market."

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
            .padding(start = 20.dp, end = 20.dp)
            .background(
                color = Color(0xFFF6F5FF),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = "My Points",
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(start = 8.dp),
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (userPoints != 0) oldUser else newUser,
                modifier = Modifier
                    .width(160.dp)
                    .padding(start = 8.dp),
                fontSize = 16.sp,
                color = Color(0xFF7469CB),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = if (userPoints != 0) newBottonInstruction else bottomInstructions,
                fontSize = 10.sp,
                modifier = Modifier
                    .width(160.dp)
                    .padding(start = 8.dp),
                color = Color(0xFF7469CB),
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp)
        ) {
            if (userPoints != 0) {
                Image(
                    painter = painterResource(id = R.drawable.ic_glowcircle_active),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_glowcircle),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .graphicsLayer { this.alpha = 0.25f },
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = "$userPoints",
                fontSize = 38.sp,
                color = Color(0xFF8070F6),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
