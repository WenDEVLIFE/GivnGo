package com.go.givngo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.go.givngo.OverscrollEffect.overScroll
import com.go.givngo.OverscrollEffect.rememberPushDownOverscrollEffect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenDonor() {
    val context = LocalContext.current
    val userCurrentSignedInEmail = SharedPreferences.getEmail(context) ?: "Developer@gmail.com"

    var selectedCategory by remember { mutableStateOf("Meals") }
    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()

    val maxPadding = 100.dp
    val paddingTop = derivedStateOf {
        val paddingPx = with(density) { maxPadding.toPx() - scrollState.value }
        paddingPx.coerceIn(0f, with(density) { maxPadding.toPx() })
    }.value

    userProfileAlignment()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = with(density) { paddingTop.toDp() }) // Apply padding directly
            .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
            .overScroll(overscrollEffect = pushDownOverscrollEffect
            )
            .background(color = Color.White)
    ) {
        GreetingSection()
        Spacer(modifier = Modifier.height(16.dp))
        CardPoints()
        Spacer(modifier = Modifier.height(18.dp))
        HorizontalLine()
        Spacer(modifier = Modifier.height(18.dp))
        CategoryHeadline()
        CategoryCardSection(
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                selectedCategory = category
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        currentSelectedCategory(categoryN = selectedCategory)
        Spacer(modifier = Modifier.height(4.dp))
        CategoryButtons(selectedCategory = selectedCategory)
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(color = Color.Transparent)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenRecipient(navController: NavController, setSelectedItem: (Int) -> Unit) {

    var selectedCategory by remember { mutableStateOf("Meals") }
    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    val pushDownOverscrollEffect = rememberPushDownOverscrollEffect()

    val maxPadding = 100.dp
    val paddingTop = derivedStateOf {
        val paddingPx = with(density) { maxPadding.toPx() - scrollState.value }
        paddingPx.coerceIn(0f, with(density) { maxPadding.toPx() })
    }.value

    userProfileAlignment()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = with(density) { paddingTop.toDp() }) // Apply padding directly
            .clip(RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp))
            .overScroll(overscrollEffect = pushDownOverscrollEffect
            )
            .background(color = Color.White)
    ) {
        GreetingSection()
        Spacer(modifier = Modifier.height(20.dp))
        topCategoryRecipient()
        Spacer(modifier = Modifier.height(8.dp))
        CarouselRow()
        Spacer(modifier = Modifier.height(18.dp))
        HorizontalLine()
        Spacer(modifier = Modifier.height(18.dp))
        BottomHeadline()
        Spacer(modifier = Modifier.height(18.dp))
        RecipientBrowseOptions(navController, setSelectedItem)
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .background(color = Color.Transparent)
        )
    }
}
