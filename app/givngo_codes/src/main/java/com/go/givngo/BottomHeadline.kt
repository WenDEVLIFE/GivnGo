package com.go.givngo

import androidx.compose.foundation.layout.Column
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
fun BottomHeadline() {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Get Started by browsing donations!",
            fontSize = 18.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
                .padding(start = 25.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BottomHeadlineRider() {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Begin your delivering journey!",
            fontSize = 18.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
                .padding(start = 25.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}



