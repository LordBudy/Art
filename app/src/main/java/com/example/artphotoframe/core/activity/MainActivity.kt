package com.example.artphotoframe.core.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.artphotoframe.core.presentation.screens.PictureScreen
import com.example.artphotoframe.core.presentation.screens.SearchScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "search_screen"
            ) {
                composable("search_screen") {
                    SearchScreen(
                        navController = navController
                    )
                }

                composable(
                    route = "picture_screen/{pictureId}",
                    arguments = listOf(
                        navArgument("pictureId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val pictureId = backStackEntry.arguments?.getInt("pictureId")
                    pictureId?.let { id ->
                        PictureScreen(
                            pictureId = id,
                            navController
                        )
                    }
                }
            }
        }
    }
}