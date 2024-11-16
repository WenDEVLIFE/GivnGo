package com.go.givngo

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.go.givngo.donorSide.postDonation
import com.go.givngo.donorSide.postHistory
import com.go.givngo.progressIndicator.DotProgressIndicator

@Composable
fun CategoryCards(
    title: String,
    drawableResId: Int?,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val textColor = if (selectedCategory == title) Color(0xFF8070F6) else Color(0xFF8070F6)
    val boldText = if (selectedCategory == title) FontWeight.Bold else FontWeight.Normal

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // Align contents to center horizontally
        modifier = Modifier
            .padding(top = 20.dp) // Add top padding for the entire component
    ) {
        // Box with image
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clickable { onCategorySelected(title) }
                .size(100.dp)
                .padding(6.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            if (drawableResId != null) {
                Image(
                    painter = painterResource(id = drawableResId), // Load drawable here
                    contentDescription = title,
                    modifier = Modifier
                        .matchParentSize()
                        .padding(2.dp)
                        .clip(RoundedCornerShape(30.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            color = Color(0xFF8070F6),
                            shape = RoundedCornerShape(30.dp)
                        )
                )
            }
        }

        // Text outside the Box, positioned at the bottom
        Text(
            text = title,
            fontSize = 11.sp,
            color = textColor,
            fontWeight = boldText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 3.dp) // Space between image and text
        )
    }
}

@Composable
fun CategoryCardSection(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 25.dp)
        ) {

            CategoryCards("Meals", R.drawable.ic_meals_category, selectedCategory, onCategorySelected)
            CategoryCards("Clothings", R.drawable.ic_clothings_category, selectedCategory, onCategorySelected)
            CategoryCards("Furnitures", R.drawable.ic_electronics_category, selectedCategory, onCategorySelected)
        }
    }

}

@Composable
fun CategoryButtons(selectedCategory: String) {
    val context = LocalContext.current

    var slideOffset by remember { mutableStateOf(1f) }

    val enterTransition = slideInVertically(
        initialOffsetY = { height -> height },
        animationSpec = tween(durationMillis = 300)
    )

    val exitTransition = slideOutVertically(
        targetOffsetY = { height -> height },
        animationSpec = tween(durationMillis = 300)
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            // Handle the result from the launched activity
        }
    )


    if(selectedCategory == "Meals"){

        AnimatedVisibility(
            visible = true,
            enter = enterTransition,
            exit = exitTransition
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically, // Aligns items in the center vertically
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Button(
                    onClick = {

                        val intent = Intent(context, postDonation::class.java)
                        launcher.launch(intent)

                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFF6F5FF)
                    ),
                    modifier = Modifier
                        .height(35.dp)
                        .padding(start = 30.dp, end = 8.dp)
                        .wrapContentWidth(), // Adjusts to wrap the content
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_createpost),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(2.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Post a donation",
                            fontSize = 10.sp,
                            modifier = Modifier
                                .padding(start = 2.dp),
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Button(
                    onClick = {
                        val intent = Intent(context, postHistory::class.java).apply{
                            putExtra("donation_category_meals", "Meals")
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFF6F5FF)
                    ),
                    modifier = Modifier
                        .height(35.dp)
                        .padding(end = 30.dp)
                        .wrapContentWidth(),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_posthistory),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(2.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Donation history",
                            fontSize = 10.sp,
                            modifier = Modifier
                                .padding(start = 2.dp),
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }else if (selectedCategory == "Clothings"){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Button(
                onClick = {

                    val intent = Intent(context, postDonation::class.java)
                    launcher.launch(intent)

                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF6F5FF)
                ),
                modifier = Modifier
                    .height(35.dp)
                    .padding(start = 30.dp, end = 8.dp)
                    .wrapContentWidth(),
                shape = RoundedCornerShape(25.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_createpost),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Post a donation",
                        fontSize = 10.sp,
                        modifier = Modifier
                            .padding(start = 2.dp),
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Button(
                onClick = {
                    val intent = Intent(context, postHistory::class.java).apply{
                        putExtra("donation_category_meals", "Clothings")
                    }
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF6F5FF)
                ),
                modifier = Modifier
                    .height(35.dp)
                    .padding(end = 30.dp)
                    .wrapContentWidth(),

                shape = RoundedCornerShape(25.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_posthistory),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Donation history",
                        fontSize = 10.sp,
                        modifier = Modifier
                            .padding(start = 2.dp),
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }else if ( selectedCategory == "Furnitures"){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Button(
                onClick = {

                    val intent = Intent(context, postDonation::class.java)
                    launcher.launch(intent)

                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF6F5FF)
                ),
                modifier = Modifier
                    .height(35.dp)
                    .padding(start = 30.dp, end = 8.dp)
                    .wrapContentWidth(), // wrap the content
                shape = RoundedCornerShape(25.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_createpost),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Post a donation",
                        fontSize = 10.sp,
                        modifier = Modifier
                            .padding(start = 2.dp),
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Button(
                onClick = {
                    val intent = Intent(context, postHistory::class.java).apply{
                        putExtra("donation_category_meals", "Furnitures")
                    }
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF6F5FF)
                ),
                modifier = Modifier
                    .height(35.dp)
                    .padding(end = 30.dp)
                    .wrapContentWidth(),
                shape = RoundedCornerShape(25.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_posthistory),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Donation history",
                        fontSize = 10.sp,
                        modifier = Modifier
                            .padding(start = 2.dp),
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }else{
        Spacer(modifier = Modifier.height(22.dp))
        DotProgressIndicator(modifier = Modifier.size(60.dp, 30.dp))
    }

}
