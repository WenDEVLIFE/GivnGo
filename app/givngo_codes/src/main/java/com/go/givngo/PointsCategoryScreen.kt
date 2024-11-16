package com.go.givngo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.go.givngo.Model.ImageLoadState
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun pointsCategoryHouseholds(
    pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



    val context = LocalContext.current


    var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()


    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }

    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
                                            val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                                    //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0



                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun pointsCategoryStationery(
    pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



    val context = LocalContext.current


    var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()


    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }

    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
                                            val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                                    //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0



                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun pointsCategoryBooks(
    pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



    val context = LocalContext.current


    var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()


    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }

    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
                                            val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                                    //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0



                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun pointsCategoryFurnitures(
    pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



    val context = LocalContext.current


    var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()


    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }

    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
                                            val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                                    //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0



                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun pointsCategoryClothings(
    pointsCardsTitles: String,
    imageUrl: String?,
    eachPointsCard: Int,
    initialNewPointsEachCategory: Int?
) {



    val context = LocalContext.current


    var latestNewPoints by remember { mutableStateOf(0) }

    latestNewPoints = listOf(
        initialNewPointsEachCategory ?: 0
    ).sum()


    var newPointsEachCategory by remember { mutableStateOf(initialNewPointsEachCategory) }

    val fontChange = if (newPointsEachCategory != 0) FontWeight.Bold else FontWeight.Normal
    val textColor = if (newPointsEachCategory != 0) Color(0xFF8070F6) else Color(0xFF312C3F)
    var imageLoadState by remember { mutableStateOf<ImageLoadState?>(null) }

    Row(
        modifier = Modifier.padding(start = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(170.dp)
                .background(
                    color = Color(0xFFE9DCFF),
                    shape = RoundedCornerShape(25.dp)
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        color = Color(0xFFE9DCFF),
                        shape = RoundedCornerShape(25.dp)
                    ),
                contentScale = ContentScale.Crop,
                onSuccess = { imageLoadState = ImageLoadState.Success },
                onError = { imageLoadState = ImageLoadState.Error },
                onLoading = { imageLoadState = ImageLoadState.Loading }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0f),
                                Color.Black.copy(alpha = 0.8f)
                            )
                        )
                    ),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                when (imageLoadState) {
                    ImageLoadState.Success -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    ImageLoadState.Error, null -> {
                        Text(
                            text = pointsCardsTitles,
                            fontSize = 13.sp,
                            color = Color(0xFF8070F6),
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                        )
                    }
                    else -> {}
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Display the category points text
            Text(
                text = "$pointsCardsTitles Points: $eachPointsCard",
                fontSize = 12.sp,
                color = textColor,
                fontWeight = fontChange,
                textAlign = TextAlign.Center
            )

            if (initialNewPointsEachCategory != 0) {
                Text(
                    text = "Your New Points: " + latestNewPoints,
                    fontSize = 12.sp,
                    color = textColor,
                    fontWeight = fontChange,
                    textAlign = TextAlign.Center
                )

                // Button to claim points
                Button(
                    onClick = {
                        val firestore = FirebaseFirestore.getInstance()
                        val userAccountType = SharedPreferences.getBasicType(context) ?: "BasicAccounts"
                        val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

                        // Step 1: Delete specific category points in "Unclaimed_Points"
                        firestore.collection("GivnGoAccounts")
                            .document(userAccountType)
                            .collection("Donor")
                            .document(userCurrentSignedInEmail)
                            .collection("MyPoints")
                            .document("Post")
                            .collection("Unclaimed_Points")
                            .whereEqualTo("donation_category", pointsCardsTitles)
                            .get()
                            .addOnSuccessListener { documents ->
                                for (document in documents) {
                                    document.reference.delete()
                                }

                                // Step 2: Update donor's account points in Firestore
                                firestore.collection("GivnGoAccounts")
                                    .document(userAccountType)
                                    .collection("Donor")
                                    .document(userCurrentSignedInEmail)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            val currentPoints = (doc.getLong("donor_account_points") ?: 0).toInt()
                                            val updatedPoints = currentPoints + (initialNewPointsEachCategory ?: 0)

                                            // Update the points field
                                            firestore.collection("GivnGoAccounts")
                                                .document(userAccountType)
                                                .collection("Donor")
                                                .document(userCurrentSignedInEmail)
                                                .update("donor_account_points", updatedPoints)
                                                .addOnSuccessListener {
                                                    //Add a way to refresh the selected route
                                                    newPointsEachCategory = null
                                                    newPointsEachCategory = 0



                                                }
                                        }
                                    }
                            }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFEBE8FF)
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(2.dp)
                        .width(140.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Claim Points!",
                            fontSize = 12.sp,
                            color = Color(0xFF8070F6),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}