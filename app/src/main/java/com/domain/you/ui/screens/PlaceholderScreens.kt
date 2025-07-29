package com.domain.you.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// Removed SeekerHomeScreen as it's now in its own file

// Removed AlchemistHomeScreen as it's now in its own file

// Removed ProfileScreen as it's now in its own file

@Composable
fun SearchScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Search Screen", style = MaterialTheme.typography.headlineMedium)
        // TODO: Implement search functionality
    }
}

@Composable
fun AlchemistDetailScreen(navController: NavController, alchemistId: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Alchemist Detail", style = MaterialTheme.typography.headlineMedium)
        Text("ID: $alchemistId", style = MaterialTheme.typography.bodyLarge)
        // TODO: Implement alchemist detail functionality
    }
}
