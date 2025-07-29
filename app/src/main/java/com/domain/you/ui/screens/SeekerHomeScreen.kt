package com.domain.you.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.domain.you.data.models.*
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeekerHomeScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Discover", "Bookings", "Saved")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Welcome back",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            "Sarah", // This would come from the user data
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error,
                            modifier = Modifier.offset(8.dp, (-8).dp)
                        ) {
                            Text("3", fontSize = 10.sp)
                        }
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("search") },
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    label = { Text("Search") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "Bookings") },
                    label = { Text("Bookings") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Saved") },
                    label = { Text("Saved") }
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> DiscoverTab(navController, paddingValues)
            1 -> BookingsTab(navController, paddingValues)
            2 -> SavedTab(navController, paddingValues)
        }
    }
}

@Composable
fun DiscoverTab(navController: NavController, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Search Bar
        item {
            SearchBarSection(navController)
        }

        // Quick Categories
        item {
            QuickCategoriesSection(navController)
        }

        // Upcoming Session
        item {
            val upcomingSession = mockUpcomingSession()
            if (upcomingSession != null) {
                UpcomingSessionCard(upcomingSession, navController)
            }
        }

        // Featured Alchemists
        item {
            FeaturedAlchemistsSection(navController)
        }

        // Recommended For You
        item {
            RecommendedSection(navController)
        }

        // Recent Reviews
        item {
            RecentReviewsSection()
        }
    }
}

@Composable
fun SearchBarSection(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { navController.navigate("search") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Search for alchemists or modalities...",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickCategoriesSection(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Quick Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockCategories) { category ->
                CategoryChip(category) {
                    navController.navigate("search?modality=${category.name}")
                }
            }
        }
    }
}

@Composable
fun CategoryChip(category: ModalityCategory, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = category.color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                category.icon,
                contentDescription = category.name,
                tint = category.color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                category.name,
                fontSize = 12.sp,
                color = category.color
            )
        }
    }
}

@Composable
fun UpcomingSessionCard(booking: MockBooking, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Upcoming Session",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    booking.timeUntil,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = booking.alchemistImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        booking.alchemistName,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "${booking.serviceName} • ${booking.time}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { /* TODO: Join session */ }) {
                    Icon(
                        Icons.Default.VideoCall,
                        contentDescription = "Join",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedAlchemistsSection(navController: NavController) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Featured Alchemists",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { navController.navigate("search") }) {
                Text("See All")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockFeaturedAlchemists) { alchemist ->
                FeaturedAlchemistCard(alchemist) {
                    navController.navigate("alchemist_detail/${alchemist.userId}")
                }
            }
        }
    }
}

@Composable
fun FeaturedAlchemistCard(alchemist: Alchemist, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                AsyncImage(
                    model = "https://picsum.photos/200/120?random=${alchemist.userId}",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (alchemist.isVerified) {
                    Surface(
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.TopEnd),
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = "Verified",
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                "Verified",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    mockAlchemistNames[alchemist.userId] ?: "Unknown",
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    alchemist.modalities.take(2).joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFB800)
                    )
                    Text(
                        "${alchemist.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        " (${alchemist.reviewCount})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        "$${alchemist.hourlyRate?.toInt() ?: 0}/hr",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedSection(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Recommended For You",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Based on your interests in Meditation & Energy Healing",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        mockRecommendedAlchemists.forEach { alchemist ->
            AlchemistListCard(alchemist) {
                navController.navigate("alchemist_detail/${alchemist.userId}")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AlchemistListCard(alchemist: Alchemist, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://picsum.photos/80/80?random=${alchemist.userId}",
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        mockAlchemistNames[alchemist.userId] ?: "Unknown",
                        fontWeight = FontWeight.Medium
                    )
                    if (alchemist.isVerified) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "Verified",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    alchemist.modalities.joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFFFFB800)
                    )
                    Text(
                        "${alchemist.rating} (${alchemist.reviewCount})",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        alchemist.location?.city ?: "Online",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    "$${alchemist.hourlyRate?.toInt() ?: 0}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "per hour",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RecentReviewsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Recent Reviews",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        mockRecentReviews.forEach { review ->
            ReviewCard(review)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://picsum.photos/40/40?random=${review.reviewerId}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        review.reviewerName,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                    Text(
                        "for ${mockAlchemistNames[review.alchemistId]}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    repeat(5) { index ->
                        Icon(
                            if (index < review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (index < review.rating) Color(0xFFFFB800) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                review.comment,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun BookingsTab(navController: NavController, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(
            "Your Bookings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (mockBookings.isEmpty()) {
            EmptyStateMessage(
                icon = Icons.Default.CalendarMonth,
                title = "No bookings yet",
                message = "Your upcoming sessions will appear here"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mockBookings) { booking ->
                    BookingCard(booking, navController)
                }
            }
        }
    }
}

@Composable
fun BookingCard(booking: MockBooking, navController: NavController) {
    Card(
        onClick = { /* TODO: Navigate to booking detail */ },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = booking.alchemistImage,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    booking.alchemistName,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    booking.serviceName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${booking.date} at ${booking.time}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when (booking.status) {
                    "upcoming" -> MaterialTheme.colorScheme.primaryContainer
                    "completed" -> MaterialTheme.colorScheme.surfaceVariant
                    else -> MaterialTheme.colorScheme.errorContainer
                }
            ) {
                Text(
                    booking.status.capitalize(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = when (booking.status) {
                        "upcoming" -> MaterialTheme.colorScheme.onPrimaryContainer
                        "completed" -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            }
        }
    }
}

@Composable
fun SavedTab(navController: NavController, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(
            "Saved Alchemists",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (mockSavedAlchemists.isEmpty()) {
            EmptyStateMessage(
                icon = Icons.Default.FavoriteBorder,
                title = "No saved alchemists",
                message = "Save your favorite alchemists to quickly book with them again"
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mockSavedAlchemists) { alchemist ->
                    AlchemistListCard(alchemist) {
                        navController.navigate("alchemist_detail/${alchemist.userId}")
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateMessage(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// Mock Data Models and Generators
data class ModalityCategory(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
)

data class MockBooking(
    val id: String,
    val alchemistId: String,
    val alchemistName: String,
    val alchemistImage: String,
    val serviceName: String,
    val date: String,
    val time: String,
    val status: String,
    val timeUntil: String = ""
)

// Mock Data
val mockCategories = listOf(
    ModalityCategory("Meditation", Icons.Default.SelfImprovement, Color(0xFF6750A4)),
    ModalityCategory("Reiki", Icons.Default.Spa, Color(0xFF0B6E4F)),
    ModalityCategory("Yoga", Icons.Default.AccessibilityNew, Color(0xFFE91E63)),
    ModalityCategory("Massage", Icons.Default.PanTool, Color(0xFF2196F3)),
    ModalityCategory("Life Coach", Icons.Default.Psychology, Color(0xFFFF6B6B))
)

val mockAlchemistNames = mapOf(
    "1" to "Luna Starweaver",
    "2" to "Sage Moonlight",
    "3" to "River Harmony",
    "4" to "Phoenix Rising",
    "5" to "Crystal Waters"
)

val mockFeaturedAlchemists = listOf(
    Alchemist(
        userId = "1",
        modalities = listOf("Reiki", "Crystal Healing", "Meditation"),
        hourlyRate = 85.0,
        location = Location(city = "Melbourne", state = "VIC", country = "Australia"),
        rating = 4.9f,
        reviewCount = 127,
        yearsOfExperience = 8,
        isVerified = true
    ),
    Alchemist(
        userId = "2",
        modalities = listOf("Life Coaching", "Breathwork"),
        hourlyRate = 120.0,
        location = Location(city = "Sydney", state = "NSW", country = "Australia"),
        rating = 4.8f,
        reviewCount = 89,
        yearsOfExperience = 5,
        isVerified = true
    ),
    Alchemist(
        userId = "3",
        modalities = listOf("Yoga Therapy", "Meditation", "Sound Healing"),
        hourlyRate = 95.0,
        location = Location(city = "Byron Bay", state = "NSW", country = "Australia"),
        rating = 5.0f,
        reviewCount = 203,
        yearsOfExperience = 12,
        isVerified = false
    )
)

val mockRecommendedAlchemists = listOf(
    Alchemist(
        userId = "4",
        modalities = listOf("Meditation", "Energy Healing"),
        hourlyRate = 75.0,
        location = Location(city = "Melbourne", state = "VIC", country = "Australia"),
        rating = 4.7f,
        reviewCount = 64,
        yearsOfExperience = 4,
        isVerified = true
    ),
    Alchemist(
        userId = "5",
        modalities = listOf("Chakra Balancing", "Meditation"),
        hourlyRate = 90.0,
        location = Location(city = "Perth", state = "WA", country = "Australia"),
        rating = 4.9f,
        reviewCount = 156,
        yearsOfExperience = 7,
        isVerified = false
    )
)

val mockSavedAlchemists = mockFeaturedAlchemists.take(2)

fun mockUpcomingSession(): MockBooking? {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.HOUR, 2)
    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    
    return MockBooking(
        id = "1",
        alchemistId = "1",
        alchemistName = "Luna Starweaver",
        alchemistImage = "https://picsum.photos/200/200?random=1",
        serviceName = "Reiki Healing Session",
        date = dateFormat.format(calendar.time),
        time = timeFormat.format(calendar.time),
        status = "upcoming",
        timeUntil = "in 2 hours"
    )
}

val mockBookings = listOf(
    mockUpcomingSession()!!,
    MockBooking(
        id = "2",
        alchemistId = "2",
        alchemistName = "Sage Moonlight",
        alchemistImage = "https://picsum.photos/200/200?random=2",
        serviceName = "Life Coaching Session",
        date = "Dec 28",
        time = "3:00 PM",
        status = "upcoming"
    ),
    MockBooking(
        id = "3",
        alchemistId = "3",
        alchemistName = "River Harmony",
        alchemistImage = "https://picsum.photos/200/200?random=3",
        serviceName = "Yoga Therapy",
        date = "Dec 20",
        time = "10:00 AM",
        status = "completed"
    )
)

val mockRecentReviews = listOf(
    Review(
        id = "1",
        reviewerId = "user1",
        reviewerName = "Emma Thompson",
        alchemistId = "1",
        rating = 5f,
        comment = "Luna's Reiki session was transformative. I felt a deep sense of peace and clarity afterwards. Highly recommend!",
        timestamp = Timestamp.now()
    ),
    Review(
        id = "2",
        reviewerId = "user2",
        reviewerName = "James Wilson",
        alchemistId = "2",
        rating = 4f,
        comment = "Great life coaching session with Sage. Really helped me gain perspective on my career goals.",
        timestamp = Timestamp.now()
    )
)
