package com.clint.composeapp

import com.clint.composeapp.ui.theme.MyComposeApplicationTheme
import com.clint.composeapp.ui.modifer.drawColoredShadow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable

import kotlinx.coroutines.launch

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import androidx.navigation.compose.currentBackStackEntryAsState

import com.clint.composeapp.bottomBar.model.*
import com.clint.composeapp.bottomBar.*
import com.clint.composeapp.bottomBar.components.*

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

// Make sure to import the Image composable
import androidx.compose.foundation.Image // For using Image composable

import androidx.core.view.WindowCompat
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.toArgb

import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        
        
        setContent {
            MyComposeApplicationTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val navigationItems = listOf(
        MainNavigation.Home,
        MainNavigation.Deliveries,
        MainNavigation.MyRoutes
    )

    var selectedItem by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val activity = (context as MainActivity)
    SideEffect {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        activity.window.statusBarColor = Color.Transparent.toArgb() 

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

        // Use Scaffold as the main layout container
        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color.Transparent, 
            modifier = Modifier
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .background(color = Color.Transparent),
            topBar = { TopBar { coroutineScope.launch { scaffoldState.drawerState.open() } } },
            drawerContent = {
                AppDrawer(onItemClicked = {
                    coroutineScope.launch { scaffoldState.drawerState.close() }
                })
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp, bottom = 23.dp)
                        .drawColoredShadow(
                            color = Color.Black, // Shadow color
                            alpha = 0.1f, // Shadow alpha
                            borderRadius = 23.dp, // Rounded corners
                            blurRadius = 7.dp, // Blur radius
                            offsetY = 6.dp // Y offset for the shadow
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
                                .padding(bottom = 8.dp), // Add bottom padding here
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
            NavHost(
                navController = navController,
                startDestination = MainNavigation.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(MainNavigation.Home.route) { DashboardScreen() }
                composable(MainNavigation.Deliveries.route) { DeliveriesScreen() }
                composable(MainNavigation.MyRoutes.route) { MyRoutesScreen() }
            }
        }
    }
}


// Navigation Host
@Composable
fun NavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = MainNavigation.Home.route) {
        composable(MainNavigation.Home.route) { DashboardScreen() }
        composable(MainNavigation.Deliveries.route) { DeliveriesScreen() }
        composable(MainNavigation.MyRoutes.route) { MyRoutesScreen() }
    }
}
sealed class MainNavigation(val route: String, val iconResId: Int, val title: String) {
    object Home : MainNavigation("Home", R.drawable.ic_homeheart, "Home")
    object Deliveries : MainNavigation("Donations", R.drawable.ic_browsedonations, "Donations")
    object MyRoutes : MainNavigation("Deliveries", R.drawable.ic_deliveries, "Deliveries")
}

@Composable
fun NavigationItem(navItem: MainNavigation) {
    Column(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = painterResource(id = navItem.iconResId),
            contentDescription = navItem.title
        )
        Text(text = navItem.title)
    }
}

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp) // Add padding around the entire column
            .background(
                color = Color.White, // Set background color to white
                shape = RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp) // Apply top radius to left and right
            )
    ) {
        GreetingSection()

        
    }
}



@Composable
fun DeliveriesScreen() {
Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 3.dp) // Add padding around the entire column
            .background(
                color = Color.White, // Set background color to white
                shape = RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp) // Apply top radius to left and right
            )
    ) {
    
        Text("This is the Deliveries Screen")
        
    }
    
}

@Composable
fun MyRoutesScreen() {
Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 3.dp) // Add padding around the entire column
            .background(
                color = Color.White, // Set background color to white
                shape = RoundedCornerShape(topStart = 23.dp, topEnd = 23.dp) // Apply top radius to left and right
            )
    ) {
    
        Text("This is the My Routes Screen")
        
    }
    
    
}

@Composable
fun TopBar(onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onMenuClick() },
            modifier = Modifier.padding(end = 1.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_menubar),
                contentDescription = "Menu",
                modifier = Modifier.size(34.dp)
            )
        }

        Text(
            text = "GivnGo",
            color = Color.White,
               modifier = Modifier.padding(bottom = 4.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun GreetingSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Goodafternoon User! ✨",
            fontSize = 20.sp,
            color = Color(0xFF8070F6),
            modifier = Modifier
            .padding(top =23.dp, start = 25.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HelpSection() {
    Column {
        Text(
            text = "Uncover New Ways to Help!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HelpCard(title = "Donations", imageRes = R.drawable.ic_browsedonations)
            HelpCard(title = "Be a Volunteer", imageRes = R.drawable.ic_deliveries)
        }
    }
}

@Composable
fun HelpCard(title: String, imageRes: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = title, fontSize = 16.sp)
    }
}

@Composable
fun DeliveryJourneySection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Begin your delivery journey",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DeliveryButton("Available deliveries")
            DeliveryButton("My Routes")
        }
    }
}

@Composable
fun DeliveryButton(text: String) {
    Button(onClick = { /* Handle button click */ }) {
        Text(text = text)
    }
}



@Composable
fun AppDrawer(onItemClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // User section
        Text(
            text = "Volunteer Account!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF917BFF) // light purple
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_homeheart), // Replace with actual image resource
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Unknown user",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "registered account",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Drawer items
        DrawerItem(text = "Dashboard", isSelected = true) {
            onItemClicked("Dashboard")
        }

        DrawerItem(text = "Available deliveries") {
            onItemClicked("Available deliveries")
        }

        DrawerItem(text = "My Routes") {
            onItemClicked("My Routes")
        }

        DrawerItem(text = "Settings") {
            onItemClicked("Settings")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout section
        TextButton(
            onClick = { onItemClicked("Log out") },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(
                text = "Log out",
                color = Color.Red
            )
        }
    }
}

@Composable
fun DrawerItem(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFFE0E0E0) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF917BFF) else Color.Black // light purple for selected
        )
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyComposeApplicationTheme {
        MyApp()
    }
}


