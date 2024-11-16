package com.go.givngo

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.go.givngo.bottomBar.AnimatedBottomBar
import com.go.givngo.bottomBar.components.BottomBarItem
import com.go.givngo.bottomBar.model.IndicatorDirection
import com.go.givngo.bottomBar.model.IndicatorStyle
import com.go.givngo.bottomBar.model.ItemStyle
import com.go.givngo.recipientSide.ClaimedDonations
import com.go.givngo.ui.modifer.drawColoredShadow
import kotlinx.coroutines.launch



@Composable
fun MyAppRider(userFinishBasic: String) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val navigationItems = listOf(
        RiderNavigationScreen.Home,
        RiderNavigationScreen.Deliv,
        RiderNavigationScreen.MyRoutes
    )
    

    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val activity = (context as MainActivity)
    
    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.Transparent.toArgb() 
    
WindowCompat.getInsetsController(activity.window, activity.window.decorView)?.isAppearanceLightStatusBars = false
}

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background_theme),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize() 
                .background(Color.Transparent),
            contentScale = ContentScale.Crop
        )
        Box(
        modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .align(Alignment.BottomCenter)
        .background(Color.White)
        )

        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color.Transparent, 
            drawerElevation = 0.dp,
            modifier = Modifier
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .background(color = Color.Transparent),
            topBar = { TopBar{ coroutineScope.launch { scaffoldState.drawerState.open() } } },
            drawerContent = {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .clip(RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)) 
            .background(Color.White, shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
            .padding(end=25.dp)
    ) {
        AppDrawer(
        userFinishBasic,
            onItemClicked = {
                coroutineScope.launch { scaffoldState.drawerState.close() }
            }
        )
    }
},
drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
drawerScrimColor = Color.Black.copy(alpha = 0.12f), 
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp, bottom = 23.dp)
                        .drawColoredShadow(
                            color = Color.Black, // Shadow color
                            alpha = 0.1f, // Shadow alpha
                            borderRadius = 30.dp, // Rounded corners
                            blurRadius = 2.dp, // Blur radius
                            offsetY = 5.dp // Y offset for the shadow
                        )
                        .background(Color(0xFFFAF9FE), shape = RoundedCornerShape(23.dp))
                        .padding(top = 6.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center, // Centers items vertically within the Column
                        horizontalAlignment = Alignment.CenterHorizontally // Centers items horizontally within the Column
                    ) {
                        AnimatedBottomBar(
                            selectedItem = selectedItem,
                            itemSize = navigationItems.size,
                            indicatorDirection = IndicatorDirection.BOTTOM,
                            containerColor = Color.Transparent,
                            indicatorColor = Color(0xFF8070F6),
                            indicatorStyle = IndicatorStyle.DOT,
                            modifier = Modifier
                                .padding(bottom = 8.dp) // Add bottom padding here
                        ) {
                            navigationItems.forEachIndexed { index, navigationItem ->
                                val imageVector = ImageVector.vectorResource(id = navigationItem.iconResId)
                                BottomBarItem(
                                    selected = currentRoute == navigationItem.route,
                                    onClick = {
                                        if (currentRoute != navigationItem.route) {
                                            selectedItem = index
                                            navController.navigate(navigationItem.route) {
                                                navController.graph.startDestinationRoute?.let { route ->
                                                    popUpTo(route) {
                                                        saveState = true
                                                    }
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                   imageVector = imageVector,
                                   modifier = Modifier
                                    .padding(bottom = 8.dp), 
                                    label = navigationItem.title,
                                    containerColor = Color.Transparent,
                                    iconColor = Color(0xFF8070F6),
                                    textColor = Color(0xFF8070F6),
                                    itemStyle = ItemStyle.STYLE2
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
        androidx.navigation.compose.NavHost(
                navController = navController,
                startDestination = RiderNavigationScreen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(RiderNavigationScreen.Home.route) { HomeScreenRider(navController, setSelectedItem = { selectedItem = 0 }) }
        composable(RiderNavigationScreen.Deliv.route) { donationPoints() }
     composable(RiderNavigationScreen.MyRoutes.route) { myDonations() }
            }
        }
    }
}

sealed class RiderNavigationScreen(val route: String, val iconResId: Int, val title: String) {
    object Home : RiderNavigationScreen("Home", R.drawable.ic_homeheart, "Home")
    object Deliv : RiderNavigationScreen("Deliveries", R.drawable.ic_deliveries, "Deliveries")
    object MyRoutes : RiderNavigationScreen("My Routes", R.drawable.ic_deliveries, "My Routes")
}

@Composable
fun RiderBrowseOptions(navController: NavController, setSelectedItem: (Int) -> Unit) {
val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 8.dp),
            horizontalAlignment = Alignment.Start // Align the boxes to the start
        ) {
            Box(
                modifier = Modifier
                    .height(45.dp)
                    .background(color = Color(0xFFF6F5FF), shape = RoundedCornerShape(25.dp))
                    .clickable {
                        setSelectedItem(1)
                        navController.navigate(RiderNavigationScreen.Deliv.route)
                    }
                    .wrapContentWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_boxwithheart),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Browse donations",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 2.dp),
                        color = Color(0xFF8070F6),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space between buttons

            Box(
                modifier = Modifier
                    .height(45.dp)
                    .background(color = Color(0xFFF6F5FF), shape = RoundedCornerShape(25.dp))
                    .clickable {
                    val intent = Intent(context, ClaimedDonations::class.java)
                context.startActivity(intent)
                    }
                    .wrapContentWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_boxwithcheck),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Claimed Donations",
                        fontSize = 14.sp,
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

