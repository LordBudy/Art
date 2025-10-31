package com.example.artphotoframe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.artphotoframe.core.presentation.screens.FavoriteScreen
import com.example.artphotoframe.core.presentation.screens.PictureScreen
import com.example.artphotoframe.core.presentation.screens.SearchScreen
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtPhotoFrameTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        val currentBackStack by navController.currentBackStackEntryAsState()
                        val currentRoute = currentBackStack?.destination?.route

                        val isDetail = currentRoute?.startsWith("picture_screen") == true

                        if (!isDetail) {
                            val tabs = listOf(
                                "search_screen" to "Поиск",
                                "favorite_screen" to "Избранное"
                            )

                            NavigationBar {
                                tabs.forEach { (route, label) ->
                                    NavigationBarItem(
                                        selected = currentRoute == route,
                                        onClick = {
                                            navController.navigate(route) {
                                                launchSingleTop = true
                                                popUpTo(navController.graph.startDestinationRoute!!) {
                                                    saveState = true
                                                }
                                                restoreState = true
                                            }
                                        },
                                        icon = {}, // сюда потом можно будет воткнуть Icon(...)
                                        label = { Text(label) }
                                    )
                                }
                            }
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "search_screen",
                        modifier = Modifier.Companion.padding(padding)
                    ) {
                        composable("search_screen") {
                            SearchScreen(navController)
                        }

                        composable("favorite_screen") {
                            FavoriteScreen(navController)
                        }



                        composable(
                            route = "picture_screen/{pictureId}",
                            arguments = listOf(
                                navArgument("pictureId") { type = NavType.Companion.IntType }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("pictureId")
                            id?.let {
                                PictureScreen(
                                    pictureId = it,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}