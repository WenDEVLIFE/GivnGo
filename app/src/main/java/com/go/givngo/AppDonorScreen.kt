package com.go.givngo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.go.givngo.bottomBar.AnimatedBottomBar
import com.go.givngo.bottomBar.components.BottomBarItem
import com.go.givngo.bottomBar.model.IndicatorDirection
import com.go.givngo.bottomBar.model.IndicatorStyle
import com.go.givngo.bottomBar.model.ItemStyle
import com.go.givngo.ui.modifer.drawColoredShadow
import kotlinx.coroutines.launch

@Composable
fun MyAppDonor(userFinishBasic: String) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val navigationItems = listOf(
        MainNavigation.Home,
        MainNavigation.Points,
        MainNavigation.MyDonations
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
                                    itemStyle = ItemStyle.STYLE6WITHPOINTS
                                )
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            androidx.navigation.compose.NavHost(
                navController = navController,
                startDestination = MainNavigation.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(MainNavigation.Home.route) { HomeScreenDonor() }
                composable(MainNavigation.Points.route) { donationPoints() }
                composable(MainNavigation.MyDonations.route) { myDonations() }
            }
        }
    }
}

