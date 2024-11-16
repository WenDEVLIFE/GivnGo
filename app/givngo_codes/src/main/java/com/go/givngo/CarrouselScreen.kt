package com.go.givngo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun CarouselRow() {
    Box(
        modifier = Modifier.fillMaxWidth(), // Fill the width of the screen
        contentAlignment = Alignment.CenterStart // Align content to start but centered horizontally
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 25.dp)
        ) {
            CarouselExplores(
                title = "13 Ways to Donate",
                width = 230.dp,
                drawableResId = R.drawable.ic_homeheart,
                websiteUrl = "https://www.usatoday.com/story/money/personalfinance/budget-and-spending/2017/10/27/13-ways-give-charity-without-breaking-your-budget/792720001/",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYh5tt4OajOwQouFN0hBn-J66QrREp-_Gm43NKG-MKiovxJZ0Q8CeLlIQ&s=10"
            )
            CarouselExplores(
                title = "Fight Hunger",
                width = 200.dp,
                drawableResId = R.drawable.ic_homeheart,
                websiteUrl = "https://muslimi.com/food-donation-how-giving-surplus-food-can-help-fight-hunger-and-reduce-waste/",
                imageUrl = "https://muslimi.com/wp-content/uploads/2023/04/da.jpg"
            )
        }
    }
}

@Composable
fun CarouselExplores(
    title: String,
    drawableResId: Int,
    width: Dp,
    websiteUrl: String,
    imageUrl: String // Using a URL for async image loading
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Box with async image as background
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .clickable { /* add intent to open based on the selected title */ }
                .height(210.dp)
                .width(width)
                .padding(6.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            // Async image loading
            AsyncImage(
                model = imageUrl, // Load image from URL
                contentDescription = title,
                modifier = Modifier
                    .matchParentSize()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )

            // Overlaying the second image and text at the bottom start
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, start = 16.dp) // Padding at bottom and start
            ) {
                // Second image (text icon)
                Image(
                    painter = painterResource(id = drawableResId), // Replace with your icon resource
                    contentDescription = "Text icon",
                    modifier = Modifier
                        .size(20.dp),
                    contentScale = ContentScale.Crop
                )

                // Title text
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color(0xFF8070F6),
                    modifier = Modifier
                        .padding(start = 4.dp),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CarouselRowRider() {
    Box(
        modifier = Modifier.fillMaxWidth(), // Fill the width of the screen
        contentAlignment = Alignment.CenterStart // Align content to start but centered horizontally
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 25.dp)
        ) {
           CarouselExplores(
                title = "13 Ways to Donate",
                width = 230.dp,
                drawableResId = R.drawable.ic_homeheart,
                websiteUrl = "https://www.usatoday.com/story/money/personalfinance/budget-and-spending/2017/10/27/13-ways-give-charity-without-breaking-your-budget/792720001/",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTYh5tt4OajOwQouFN0hBn-J66QrREp-_Gm43NKG-MKiovxJZ0Q8CeLlIQ&s=10"
            )
            CarouselExplores(
                title = "Benefits of a donor",
                width = 200.dp,
                drawableResId = R.drawable.ic_homeheart,
                websiteUrl = "https://muslimi.com/food-donation-how-giving-surplus-food-can-help-fight-hunger-and-reduce-waste/",
                imageUrl = "https://muslimi.com/wp-content/uploads/2023/04/da.jpg"
            )
        }
    }
}

