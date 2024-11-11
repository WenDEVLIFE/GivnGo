package com.go.givngo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.go.givngo.Model.DonationPost

@Composable
fun CustomDonationDialog(
    donationPost: DonationPost,
    onDismissRequest: () -> Unit,
    onClaimDonation: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDismissRequest()
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, start = 16.dp, end = 10.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    AsyncImage(
                        model = donationPost.image_thumbnail,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .padding(start = 10.dp, end = 40.dp, top = 60.dp)
                            .clip(RoundedCornerShape(23.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = donationPost.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 10.dp, bottom = 12.dp)
                )
                Text(
                    text = donationPost.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    modifier = Modifier.padding(start = 10.dp, bottom = 40.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .height(60.dp)
                            .padding(8.dp)
                            .wrapContentWidth()
                            .clip(RoundedCornerShape(25.dp))
                            .background(color = Color(0xFF8070F6)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Button(
                            onClick = onClaimDonation,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF6F5FF)
                            ),
                            modifier = Modifier
                                .wrapContentHeight()
                                .padding(4.dp)
                                .wrapContentWidth(),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_donationpackage),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(2.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = "Claim donation...",
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
            }
        }
    }
}