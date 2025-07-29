package com.domain.you.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.domain.you.data.models.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlchemistHomeScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Dashboard", "Calendar", "Services", "Reviews")

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
                            "Luna Starweaver", // This would come from the user data
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
                            Text("5", fontSize = 10.sp)
                        }
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = "Calendar") },
                    label = { Text("Calendar") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Spa, contentDescription = "Services") },
                    label = { Text("Services") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Filled.Star, contentDescription = "Reviews") },
                    label = { Text("Reviews") }
                )
            }
        },
        floatingActionButton = {
            if (selectedTab == 1) { // Calendar tab
                ExtendedFloatingActionButton(
                    onClick = { /* TODO: Add new availability */ },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    text = { Text("Add Availability") }
                )
            } else if (selectedTab == 2) { // Services tab
                ExtendedFloatingActionButton(
                    onClick = { /* TODO: Add new service */ },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    text = { Text("Add Service") }
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> DashboardTab(navController, paddingValues)
            1 -> CalendarTab(navController, paddingValues)
            2 -> ServicesTab(navController, paddingValues)
            3 -> ReviewsTab(navController, paddingValues)
        }
    }
}

@Composable
fun DashboardTab(navController: NavController, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Today's Overview
        item {
            TodayOverviewSection()
        }

        // Earnings Summary
        item {
            EarningsSummarySection()
        }

        // Today's Appointments
        item {
            TodaysAppointmentsSection(navController)
        }

        // Quick Stats
        item {
            QuickStatsSection()
        }

        // Recent Activity
        item {
            RecentActivitySection()
        }

        // Profile Completion
        item {
            ProfileCompletionCard()
        }
    }
}

@Composable
fun TodayOverviewSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "3",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Sessions Today",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "$420",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Today's Earnings",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "4.9",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Average Rating",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun EarningsSummarySection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Earnings Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /* TODO: View detailed earnings */ }) {
                    Text("View Details")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            // Weekly earnings chart placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // This would be replaced with an actual chart
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Weekly Earnings Chart",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$2,450",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "This Week",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$9,820",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "This Month",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "+15%",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        "vs Last Month",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun TodaysAppointmentsSection(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Today's Appointments",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = { /* TODO: Navigate to calendar */ }) {
                Text("View All")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        
        mockTodayAppointments.forEach { appointment ->
            AppointmentCard(appointment)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AppointmentCard(appointment: AlchemistAppointment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time slot
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(60.dp)
            ) {
                Text(
                    appointment.time,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    appointment.duration,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .padding(horizontal = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            
            // Client info
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = appointment.clientImage,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        appointment.clientName,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        appointment.serviceName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Action buttons
            Row {
                if (appointment.status == "upcoming") {
                    IconButton(onClick = { /* TODO: Message client */ }) {
                        Icon(
                            Icons.Default.Message,
                            contentDescription = "Message",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { /* TODO: Start session */ }) {
                        Icon(
                            Icons.Default.VideoCall,
                            contentDescription = "Start",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (appointment.status) {
                            "in_progress" -> MaterialTheme.colorScheme.primaryContainer
                            "completed" -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ) {
                        Text(
                            appointment.status.replace("_", " ").capitalize(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickStatsSection() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mockQuickStats) { stat ->
            QuickStatCard(stat)
        }
    }
}

@Composable
fun QuickStatCard(stat: QuickStat) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = stat.backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                stat.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = stat.iconColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stat.value,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                stat.label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (stat.trend != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (stat.trend > 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (stat.trend > 0) Color(0xFF4CAF50) else Color(0xFFE91E63)
                    )
                    Text(
                        "${if (stat.trend > 0) "+" else ""}${stat.trend}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (stat.trend > 0) Color(0xFF4CAF50) else Color(0xFFE91E63)
                    )
                }
            }
        }
    }
}

@Composable
fun RecentActivitySection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Recent Activity",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                mockRecentActivities.forEach { activity ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            activity.icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = activity.iconColor
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                activity.title,
                                fontSize = 14.sp
                            )
                            Text(
                                activity.time,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (activity != mockRecentActivities.last()) {
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCompletionCard() {
    val completionPercentage = 85
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Profile Completion",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Complete your profile to attract more clients",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
                Text(
                    "$completionPercentage%",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = completionPercentage / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { /* TODO: Navigate to profile */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Complete Profile")
            }
        }
    }
}

@Composable
fun CalendarTab(navController: NavController, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text(
            "Your Calendar",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        // Calendar view placeholder
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Calendar View",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "Manage your availability and appointments",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Availability settings
        Text(
            "Weekly Availability",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        mockWeeklyAvailability.forEach { availability ->
            AvailabilityRow(availability)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AvailabilityRow(availability: WeeklyAvailability) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (availability.isAvailable) 
                MaterialTheme.colorScheme.surfaceVariant 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                availability.day,
                modifier = Modifier.width(100.dp),
                fontWeight = FontWeight.Medium
            )
            if (availability.isAvailable) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "${availability.startTime} - ${availability.endTime}",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    "Not Available",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = availability.isAvailable,
                onCheckedChange = { /* TODO: Update availability */ }
            )
        }
    }
}

@Composable
fun ServicesTab(navController: NavController, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Your Services",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${mockServices.size} active",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        items(mockServices) { service ->
            ServiceCard(service)
        }
    }
}

@Composable
fun ServiceCard(service: Service) {
    Card(
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        service.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        service.modality,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row {
                    IconButton(onClick = { /* TODO: Edit service */ }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { /* TODO: Toggle active */ }) {
                        Icon(
                            if (service.isOnline) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle",
                            tint = if (service.isOnline) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                service.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${service.duration} min",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (service.maxParticipants > 1) "Group (${service.maxParticipants})" else "1-on-1",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    "$${"%.0f".format(service.price)}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                if (service.isOnline) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Online", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Videocam,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        modifier = Modifier.height(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                if (service.isInPerson) {
                    AssistChip(
                        onClick = { },
                        label = { Text("In-Person", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        modifier = Modifier.height(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewsTab(navController: NavController, paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Rating Overview
        item {
            RatingOverviewCard()
        }
        
        // Reviews List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Client Reviews",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /* TODO: Filter reviews */ }) {
                    Text("Filter")
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        
        items(mockAlchemistReviews) { review ->
            ReviewDetailCard(review)
        }
    }
}

@Composable
fun RatingOverviewCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "4.9",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = if (index < 5) Color(0xFFFFB800) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                "Based on 127 reviews",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Rating breakdown
            listOf(5 to 95, 4 to 25, 3 to 5, 2 to 2, 1 to 0).forEach { (stars, count) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "$stars",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(20.dp)
                    )
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFB800)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    LinearProgressIndicator(
                        progress = count / 127f,
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        count.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.width(30.dp),
                        textAlign = TextAlign.End
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun ReviewDetailCard(review: Review) {
    Card(shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = "https://picsum.photos/50/50?random=${review.reviewerId}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            review.reviewerName,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                                .format(review.timestamp.toDate()),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                if (index < review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (index < review.rating) Color(0xFFFFB800) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (review.isVerifiedBooking) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Verified,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        "Verified",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                review.comment,
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Response section
            if (review.id == "1") { // Mock response for demo
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Reply,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Your Response",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Thank you so much for your kind words! It was wonderful working with you.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { /* TODO: Add response */ }) {
                    Icon(
                        Icons.Default.Reply,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Respond")
                }
            }
        }
    }
}

// Data classes and mock data
data class AlchemistAppointment(
    val id: String,
    val clientName: String,
    val clientImage: String,
    val serviceName: String,
    val time: String,
    val duration: String,
    val status: String
)

data class QuickStat(
    val label: String,
    val value: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: Color,
    val backgroundColor: Color,
    val trend: Int? = null
)

data class RecentActivity(
    val title: String,
    val time: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: Color
)

data class WeeklyAvailability(
    val day: String,
    val isAvailable: Boolean,
    val startTime: String = "9:00 AM",
    val endTime: String = "5:00 PM"
)

// Mock Data
val mockTodayAppointments = listOf(
    AlchemistAppointment(
        id = "1",
        clientName = "Sarah Johnson",
        clientImage = "https://picsum.photos/100/100?random=1",
        serviceName = "Reiki Healing Session",
        time = "9:00 AM",
        duration = "60 min",
        status = "completed"
    ),
    AlchemistAppointment(
        id = "2",
        clientName = "Michael Chen",
        clientImage = "https://picsum.photos/100/100?random=2",
        serviceName = "Crystal Healing",
        time = "11:00 AM",
        duration = "45 min",
        status = "in_progress"
    ),
    AlchemistAppointment(
        id = "3",
        clientName = "Emma Williams",
        clientImage = "https://picsum.photos/100/100?random=3",
        serviceName = "Meditation Guidance",
        time = "2:00 PM",
        duration = "30 min",
        status = "upcoming"
    )
)

val mockQuickStats = listOf(
    QuickStat(
        label = "Total Clients",
        value = "234",
        icon = Icons.Default.People,
        iconColor = Color(0xFF6750A4), // Primary color
        backgroundColor = Color(0xFFEADDFF), // Primary container
        trend = 12
    ),
    QuickStat(
        label = "Sessions This Month",
        value = "48",
        icon = Icons.Default.CalendarToday,
        iconColor = Color(0xFF4CAF50),
        backgroundColor = Color(0xFFE8F5E9),
        trend = 8
    ),
    QuickStat(
        label = "Repeat Clients",
        value = "78%",
        icon = Icons.Default.Repeat,
        iconColor = Color(0xFF2196F3),
        backgroundColor = Color(0xFFE3F2FD),
        trend = 5
    ),
    QuickStat(
        label = "Response Time",
        value = "2.5h",
        icon = Icons.Default.Speed,
        iconColor = Color(0xFFFF9800),
        backgroundColor = Color(0xFFFFF3E0),
        trend = -15
    )
)

val mockRecentActivities = listOf(
    RecentActivity(
        title = "New booking from Emma Williams",
        time = "2 hours ago",
        icon = Icons.Default.BookmarkAdd,
        iconColor = Color(0xFF6750A4) // Primary color
    ),
    RecentActivity(
        title = "Michael Chen left a 5-star review",
        time = "5 hours ago",
        icon = Icons.Default.Star,
        iconColor = Color(0xFFFFB800)
    ),
    RecentActivity(
        title = "Profile views increased by 25%",
        time = "Yesterday",
        icon = Icons.Default.TrendingUp,
        iconColor = Color(0xFF4CAF50)
    )
)

val mockWeeklyAvailability = listOf(
    WeeklyAvailability("Monday", true, "9:00 AM", "6:00 PM"),
    WeeklyAvailability("Tuesday", true, "9:00 AM", "6:00 PM"),
    WeeklyAvailability("Wednesday", true, "10:00 AM", "5:00 PM"),
    WeeklyAvailability("Thursday", true, "9:00 AM", "6:00 PM"),
    WeeklyAvailability("Friday", true, "9:00 AM", "4:00 PM"),
    WeeklyAvailability("Saturday", false),
    WeeklyAvailability("Sunday", false)
)

val mockServices = listOf(
    Service(
        id = "1",
        alchemistId = "1",
        title = "Reiki Healing Session",
        description = "Experience deep relaxation and energy balancing through traditional Reiki techniques. Perfect for stress relief and emotional healing.",
        modality = "Reiki",
        duration = 60,
        price = 85.0,
        isOnline = true,
        isInPerson = true,
        maxParticipants = 1
    ),
    Service(
        id = "2",
        alchemistId = "1",
        title = "Crystal Healing Journey",
        description = "Harness the power of crystals to align your chakras and promote healing. Each session is customized to your specific needs.",
        modality = "Crystal Healing",
        duration = 45,
        price = 75.0,
        isOnline = false,
        isInPerson = true,
        maxParticipants = 1
    ),
    Service(
        id = "3",
        alchemistId = "1",
        title = "Group Meditation Circle",
        description = "Join our weekly meditation circle for guided mindfulness and community connection. Suitable for all experience levels.",
        modality = "Meditation",
        duration = 90,
        price = 30.0,
        isOnline = true,
        isInPerson = false,
        maxParticipants = 10
    )
)

val mockAlchemistReviews = listOf(
    Review(
        id = "1",
        reviewerId = "client1",
        reviewerName = "Sarah Johnson",
        alchemistId = "1",
        serviceId = "1",
        rating = 5f,
        comment = "Luna's Reiki session was absolutely transformative. I came in feeling stressed and overwhelmed, and left feeling completely renewed. Her gentle approach and intuitive understanding of energy work is remarkable.",
        isVerifiedBooking = true
    ),
    Review(
        id = "2",
        reviewerId = "client2",
        reviewerName = "Michael Chen",
        alchemistId = "1",
        serviceId = "2",
        rating = 5f,
        comment = "The crystal healing session exceeded my expectations. Luna has deep knowledge of crystals and their properties. I felt the energy shifts during the session and have been sleeping better ever since.",
        isVerifiedBooking = true
    ),
    Review(
        id = "3",
        reviewerId = "client3",
        reviewerName = "Emma Williams",
        alchemistId = "1",
        serviceId = "3",
        rating = 4f,
        comment = "Great meditation circle! Luna creates a welcoming space for both beginners and experienced practitioners. The only reason for 4 stars is that the online format had some technical issues at the start.",
        isVerifiedBooking = true
    )
)
