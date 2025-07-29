package com.domain.you.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.domain.you.data.models.HealingModalities
import com.domain.you.ui.components.AlchemistCard
import com.domain.you.ui.components.BottomNavBar
import com.domain.you.ui.components.SearchBar

// Dummy data for now - will be replaced with Firebase data later
data class DummyAlchemist(
    val id: String,
    val name: String,
    val title: String,
    val imageUrl: String,
    val rating: Float,
    val reviewCount: Int,
    val hourlyRate: String,
    val modalities: List<String>,
    val isOnline: Boolean = false,
    val distance: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeekerHomeScreen(navController: NavController) {
    var selectedModality by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Dummy data - will be replaced with real data
    val featuredAlchemists = remember {
        listOf(
            DummyAlchemist(
                "1", "Sarah Johnson", "Reiki Master & Energy Healer",
                "https://randomuser.me/api/portraits/women/1.jpg",
                4.9f, 127, "$80-120", listOf("Reiki", "Energy Healing", "Meditation"),
                isOnline = true
            ),
            DummyAlchemist(
                "2", "Michael Chen", "Holistic Life Coach",
                "https://randomuser.me/api/portraits/men/2.jpg",
                4.8f, 89, "$100-150", listOf("Life Coaching", "Breathwork", "Meditation")
            ),
            DummyAlchemist(
                "3", "Luna Martinez", "Sound Healer & Yoga Teacher",
                "https://randomuser.me/api/portraits/women/3.jpg",
                5.0f, 203, "$70-100", listOf("Sound Healing", "Yoga Therapy", "Meditation"),
                isOnline = true
            )
        )
    }
    
    val nearbyAlchemists = remember {
        listOf(
            DummyAlchemist(
                "4", "David Thompson", "Massage Therapist",
                "https://randomuser.me/api/portraits/men/4.jpg",
                4.7f, 64, "$90", listOf("Massage Therapy", "Reflexology"),
                distance = "0.8 mi"
            ),
            DummyAlchemist(
                "5", "Emma Wilson", "Acupuncturist",
                "https://randomuser.me/api/portraits/women/5.jpg",
                4.9f, 112, "$120", listOf("Acupuncture", "Traditional Chinese Medicine"),
                distance = "1.2 mi"
            ),
            DummyAlchemist(
                "6", "James Brown", "Herbalist & Nutritionist",
                "https://randomuser.me/api/portraits/men/6.jpg",
                4.6f, 38, "$80", listOf("Herbalism", "Nutritional Counseling"),
                distance = "2.1 mi"
            )
        )
    }
    
    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = "seeker_home"
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(bottom = 16.dp)
                ) {
                    // Greeting and Notifications
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Welcome back!",
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Find your healer",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        IconButton(
                            onClick = { /* TODO: Navigate to notifications */ }
                        ) {
                            Badge(
                                
                            ) {
                                Icon(
                                    Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    
                    // Search Bar
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { 
                            // TODO: Navigate to search screen with query
                            navController.navigate("search?query=$it")
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            
            // Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(HealingModalities.list.take(8)) { modality ->
                        FilterChip(
                            selected = selectedModality == modality,
                            onClick = {
                                selectedModality = if (selectedModality == modality) null else modality
                            },
                            label = { Text(modality) },
                            leadingIcon = if (selectedModality == modality) {
                                {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            } else null
                        )
                    }
                }
            }
            
            // Featured Alchemists Section
            item {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Featured Healers",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(onClick = { /* TODO: See all featured */ }) {
                            Text("See all")
                        }
                    }
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(featuredAlchemists) { alchemist ->
                            FeaturedAlchemistCard(
                                alchemist = alchemist,
                                onClick = {
                                    navController.navigate("alchemist_detail/${alchemist.id}")
                                }
                            )
                        }
                    }
                }
            }
            
            // Nearby Alchemists Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nearby Healers",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { /* TODO: See all nearby */ }) {
                        Text("See all")
                    }
                }
            }
            
            // Nearby Alchemists List
            items(nearbyAlchemists) { alchemist ->
                AlchemistCard(
                    alchemist = alchemist,
                    onClick = {
                        navController.navigate("alchemist_detail/${alchemist.id}")
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedAlchemistCard(
    alchemist: DummyAlchemist,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .width(280.dp)
            .height(320.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Image with Online Badge
            Box {
                AsyncImage(
                    model = alchemist.imageUrl,
                    contentDescription = alchemist.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                
                if (alchemist.isOnline) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Green.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = "Online Now",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = alchemist.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                
                Text(
                    text = alchemist.title,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFB800)
                    )
                    Text(
                        text = alchemist.rating.toString(),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "(${alchemist.reviewCount})",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = alchemist.hourlyRate,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Modality chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    alchemist.modalities.take(2).forEach { modality ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = modality,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    if (alchemist.modalities.size > 2) {
                        Text(
                            text = "+${alchemist.modalities.size - 2}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}
