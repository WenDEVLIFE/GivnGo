package com.go.givngo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun topCategoryRecipient() {

    Column(
        modifier = Modifier.fillMaxWidth(),  // Ensures the column takes the full width
        horizontalAlignment = Alignment.Start // Aligns the content to the start
    ) {
        Text(
            text = "Discover your support!",
            fontSize = 20.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
                .padding(start = 25.dp) // Padding from the start for centering purpose
                .fillMaxWidth(),  // Ensures the text takes the full width
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start // Align text to the start within the text box
        )
    }
}
