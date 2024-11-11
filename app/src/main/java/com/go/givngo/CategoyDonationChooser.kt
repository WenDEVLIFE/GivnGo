package com.go.givngo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun categoryDonationChooser(categoryType: String, selectedCategory: String?, onCategorySelected: (String) -> Unit) {
    val isButtonPressed = selectedCategory == categoryType

    Box(
        modifier = Modifier
            .padding(4.dp)
            .height(28.dp)
            .wrapContentWidth()
            .clickable {
                onCategorySelected(categoryType)
            }
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = if (isButtonPressed) Color(0xFFFBF8FF) else Color(0xFFE9E7FF),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = categoryType,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 3.dp, bottom = 3.dp, start = 24.dp, end = 24.dp),
                color =  if (isButtonPressed) Color(0xFF483D9B) else Color(0xFF8070F5),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }

    if (isButtonPressed) {
        when (categoryType) {
            "Electronics" -> {
                /*    // Handle Electronics pressed action
                    Text(
                        text = "You selected Electronics!",
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    ) */
            }
            "Meals" -> {
                /*  // Handle Meals pressed action
                  Text(
                      text = "You selected Meals!",
                      color = Color.Gray,
                      modifier = Modifier.padding(top = 8.dp)
                  ) */
            }
            "Clothings" -> {
                /*   // Handle Clothings pressed action
                   Text(
                       text = "You selected Clothings!",
                       color = Color.Gray,
                       modifier = Modifier.padding(top = 8.dp)
                   ) */
            }
        }
    }
}