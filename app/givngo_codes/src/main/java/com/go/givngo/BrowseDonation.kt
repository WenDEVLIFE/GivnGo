package com.go.givngo

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.go.givngo.recipientSide.ClaimedDonations

@Composable
fun browseDonationHeadline() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            // Handle the result from the launched activity
        }
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start // Align content to start in the column
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), // Ensure the Row fills the available width
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Space out the text and image
        ) {
            // Text aligned to center start
            Text(
                text = "Browse donations",
                fontSize = 22.sp,
                color = Color(0xFF8070F6),
                modifier = Modifier
                    .padding(start = 25.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start // Align text to the start within the text block
            )

            // Image aligned to center end
            Image(
                painter = painterResource(id = R.drawable.ic_donationpackage),
                contentDescription = "Back button",
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 25.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = Color.Gray, bounded = false) // Circular ripple
                    ) {
                        val intent = Intent(context, ClaimedDonations::class.java)
                        launcher.launch(intent)
                    }
            )
        }
    }
}

