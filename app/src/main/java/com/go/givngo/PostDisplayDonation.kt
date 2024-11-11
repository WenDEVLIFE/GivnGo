package com.go.givngo

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.go.givngo.Model.ImageLoadState

@Composable
fun PostDisplayModel(
    userDonationPost: String,
    imageUrl: Uri?,
    onClick: () -> Unit
) {
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
            .padding(4.dp)
            .clickable { onClick() } // Make clickable to show the dialog
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // AsyncImage at the top
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(180.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(18.dp))
                ,
                contentScale = ContentScale.Crop,
                onSuccess = {
                    imageLoadState = ImageLoadState.Success
                },
                onError = {
                    imageLoadState = ImageLoadState.Error
                },
                onLoading = {
                    imageLoadState = ImageLoadState.Loading
                }
            )
            // Text aligned at the bottom
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = userDonationPost,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                                .fillMaxWidth(),
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }
                    ImageLoadState.Loading -> {
                        // Loading state UI if needed
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = userDonationPost,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth(),
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}
