package com.domain.you

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.domain.you.ui.screens.*
import com.domain.you.ui.theme.YOUTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YOUTheme {
                HealingConnectApp()
            }
        }
    }
}

@Composable
fun HealingConnectApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(navController)
            }
            composable("onboarding") {
                OnboardingScreen(navController)
            }
            composable("login") {
                LoginScreen(navController)
            }
            composable("signup") {
                SignupScreen(navController)
            }
            composable("seeker_home") {
                SeekerHomeScreen(navController)
            }
            composable("alchemist_home") {
                AlchemistHomeScreen(navController)
            }
            composable("profile") {
                ProfileScreen(navController)
            }
            composable("search") {
                SearchScreen(navController)
            }
            composable("alchemist_detail/{alchemistId}") { backStackEntry ->
                AlchemistDetailScreen(
                    navController = navController,
                    alchemistId = backStackEntry.arguments?.getString("alchemistId") ?: ""
                )
            }
        }
    }
}