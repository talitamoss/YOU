package com.domain.you.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.domain.you.data.models.*
import com.domain.you.viewmodels.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val currentUser by authViewModel.currentUser.collectAsState()
    
    // For demo purposes, we'll use mock data
    val isMockAlchemist = remember { mutableStateOf(true) } // Toggle this to test both views
    val mockUser = if (isMockAlchemist.value) mockAlchemistUser else mockSeekerUser
    val userToDisplay = currentUser ?: mockUser

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Navigate to settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Header
            item {
                ProfileHeaderSection(
                    user = userToDisplay,
                    isAlchemist = userToDisplay.userType == UserType.ALCHEMIST,
                    onEditProfile = { /* TODO: Navigate to edit */ },
                    onChangePhoto = { /* TODO: Handle photo change */ }
                )
            }

            // Quick Stats (different for each user type)
            if (userToDisplay.userType == UserType.ALCHEMIST) {
                item {
                    AlchemistStatsSection()
                }
            } else {
                item {
                    SeekerStatsSection()
                }
            }

            // Profile Sections based on user type
            if (userToDisplay.userType == UserType.ALCHEMIST) {
                // Alchemist-specific sections
                item {
                    AboutSection(
                        aboutText = mockAlchemistData.aboutMe,
                        onEdit = { /* TODO: Edit about */ }
                    )
                }

                item {
                    ModalitiesSection(
                        modalities = mockAlchemistData.modalities,
                        onEdit = { /* TODO: Edit modalities */ }
                    )
                }

                item {
                    CertificationsSection(
                        certifications = mockAlchemistData.certifications,
                        onAdd = { /* TODO: Add certification */ }
                    )
                }

                item {
                    LanguagesSection(
                        languages = mockAlchemistData.languages,
                        onEdit = { /* TODO: Edit languages */ }
                    )
                }
            } else {
                // Seeker-specific sections
                item {
                    InterestsSection(
                        interests = mockSeekerData.interests,
                        onEdit = { /* TODO: Edit interests */ }
                    )
                }

                item {
                    PreferredModalitiesSection(
                        modalities = mockSeekerData.preferredModalities,
                        onEdit = { /* TODO: Edit preferences */ }
                    )
                }

                item {
                    SavedAlchemistsSection(
                        savedCount = mockSeekerData.savedAlchemists.size,
                        onViewAll = { /* TODO: Navigate to saved */ }
                    )
                }
            }

            // Common sections for both
            item {
                LocationSection(
                    location = if (userToDisplay.userType == UserType.ALCHEMIST) 
                        mockAlchemistData.location else mockSeekerData.location,
                    onEdit = { /* TODO: Edit location */ }
                )
            }

            item {
                AccountSection(
                    email = userToDisplay.email,
                    isEmailVerified = userToDisplay.isEmailVerified,
                    onVerifyEmail = { /* TODO: Verify email */ },
                    onChangePassword = { /* TODO: Change password */ },
                    onSignOut = { authViewModel.signOut() }
                )
            }

            // Danger Zone
            item {
                DangerZoneSection(
                    onDeleteAccount = { /* TODO: Delete account */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileHeaderSection(
    user: User,
    isAlchemist: Boolean,
    onEditProfile: () -> Unit,
    onChangePhoto: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image with edit overlay
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = user.profileImageUrl ?: "https://picsum.photos/200/200?random=${user.uid}",
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(
                            width = 3.dp,
                            color = if (isAlchemist) 
                                MaterialTheme.colorScheme.secondary 
                            else 
                                MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )
                
                IconButton(
                    onClick = onChangePhoto,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Change photo",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name and user type
            Text(
                text = user.displayName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isAlchemist) 
                        MaterialTheme.colorScheme.secondaryContainer 
                    else 
                        MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (isAlchemist) Icons.Default.Spa else Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isAlchemist) "Alchemist" else "Seeker",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                if (isAlchemist && mockAlchemistData.isVerified) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile completion indicator
            if (!user.isProfileComplete) {
                LinearProgressIndicator(
                    progress = 0.7f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Profile 70% complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            Button(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile")
            }
        }
    }
}

@Composable
fun AlchemistStatsSection() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(alchemistStats) { stat ->
            StatCard(stat)
        }
    }
}

@Composable
fun SeekerStatsSection() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(seekerStats) { stat ->
            StatCard(stat)
        }
    }
}

@Composable
fun StatCard(stat: ProfileStat) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = stat.backgroundColor
        ),
        modifier = Modifier.width(120.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                stat.icon,
                contentDescription = null,
                tint = stat.iconColor,
                modifier = Modifier.size(24.dp)
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
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AboutSection(aboutText: String, onEdit: () -> Unit) {
    ProfileSection(
        title = "About",
        onAction = onEdit,
        actionIcon = Icons.Default.Edit
    ) {
        Text(
            text = aboutText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ModalitiesSection(modalities: List<String>, onEdit: () -> Unit) {
    ProfileSection(
        title = "Modalities",
        onAction = onEdit,
        actionIcon = Icons.Default.Edit
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            modalities.forEach { modality ->
                AssistChip(
                    onClick = { },
                    label = { Text(modality) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Spa,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun CertificationsSection(certifications: List<Certification>, onAdd: () -> Unit) {
    ProfileSection(
        title = "Certifications",
        onAction = onAdd,
        actionIcon = Icons.Default.Add
    ) {
        if (certifications.isEmpty()) {
            Text(
                "No certifications added yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                certifications.forEach { cert ->
                    CertificationCard(cert)
                }
            }
        }
    }
}

@Composable
fun CertificationCard(certification: Certification) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.School,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    certification.name,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    certification.issuingOrganization,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                certification.dateObtained?.let { date ->
                    Text(
                        "Obtained: ${java.text.SimpleDateFormat("MMM yyyy").format(date.toDate())}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (certification.isVerified) {
                Icon(
                    Icons.Default.Verified,
                    contentDescription = "Verified",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun LanguagesSection(languages: List<String>, onEdit: () -> Unit) {
    ProfileSection(
        title = "Languages",
        onAction = onEdit,
        actionIcon = Icons.Default.Edit
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            languages.forEach { language ->
                AssistChip(
                    onClick = { },
                    label = { Text(language) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun InterestsSection(interests: List<String>, onEdit: () -> Unit) {
    ProfileSection(
        title = "My Interests",
        onAction = onEdit,
        actionIcon = Icons.Default.Edit
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            interests.forEach { interest ->
                AssistChip(
                    onClick = { },
                    label = { Text(interest) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun PreferredModalitiesSection(modalities: List<String>, onEdit: () -> Unit) {
    ProfileSection(
        title = "Preferred Healing Modalities",
        onAction = onEdit,
        actionIcon = Icons.Default.Edit
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            modalities.forEach { modality ->
                AssistChip(
                    onClick = { },
                    label = { Text(modality) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}

@Composable
fun SavedAlchemistsSection(savedCount: Int, onViewAll: () -> Unit) {
    ProfileSection(
        title = "Saved Alchemists",
        onAction = onViewAll,
        actionText = "View All"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "$savedCount",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Saved alchemists",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun LocationSection(location: Location?, onEdit: () -> Unit) {
    ProfileSection(
        title = "Location",
        onAction = onEdit,
        actionIcon = Icons.Default.Edit
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                location?.let { "${it.city}, ${it.state}, ${it.country}" } ?: "Not set",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AccountSection(
    email: String,
    isEmailVerified: Boolean,
    onVerifyEmail: () -> Unit,
    onChangePassword: () -> Unit,
    onSignOut: () -> Unit
) {
    ProfileSection(title = "Account") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Email with verification status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Email",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(email)
                }
                if (!isEmailVerified) {
                    TextButton(onClick = onVerifyEmail) {
                        Text("Verify", fontSize = 12.sp)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Verified",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            Divider()

            // Change Password
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChangePassword() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Change Password")
                }
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider()

            // Sign Out
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSignOut() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Logout,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Sign Out",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun DangerZoneSection(onDeleteAccount: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    ProfileSection(
        title = "Danger Zone",
        titleColor = MaterialTheme.colorScheme.error
    ) {
        OutlinedButton(
            onClick = { showDeleteDialog = true },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Delete Account")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account?") },
            text = { 
                Text("This action cannot be undone. All your data will be permanently deleted.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteAccount()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileSection(
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onAction: (() -> Unit)? = null,
    actionIcon: ImageVector? = null,
    actionText: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
                onAction?.let { action ->
                    if (actionIcon != null) {
                        IconButton(onClick = action) {
                            Icon(
                                actionIcon,
                                contentDescription = actionText,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else if (actionText != null) {
                        TextButton(onClick = action) {
                            Text(actionText)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

// FlowRow composable for chips layout
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val rows = mutableListOf<List<androidx.compose.ui.layout.Placeable>>()
        var currentRow = mutableListOf<androidx.compose.ui.layout.Placeable>()
        var currentRowWidth = 0
        val spacing = 8.dp.roundToPx()

        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints.copy(minWidth = 0))
            if (currentRowWidth + placeable.width > constraints.maxWidth && currentRow.isNotEmpty()) {
                rows.add(currentRow.toList())
                currentRow = mutableListOf(placeable)
                currentRowWidth = placeable.width + spacing
            } else {
                currentRow.add(placeable)
                currentRowWidth += placeable.width + spacing
            }
        }
        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }

        val height = rows.sumOf { row ->
            row.maxOfOrNull { it.height } ?: 0
        } + (rows.size - 1) * spacing

        layout(constraints.maxWidth, height) {
            var y = 0
            rows.forEach { row ->
                var x = 0
                row.forEach { placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width + spacing
                }
                y += (row.maxOfOrNull { it.height } ?: 0) + spacing
            }
        }
    }
}

// Data classes
data class ProfileStat(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val iconColor: Color,
    val backgroundColor: Color
)

// Mock Data
val mockAlchemistUser = User(
    uid = "1",
    email = "luna@healingspace.com",
    displayName = "Luna Starweaver",
    userType = UserType.ALCHEMIST,
    profileImageUrl = null,
    bio = "Energy healer and spiritual guide",
    isEmailVerified = true,
    isProfileComplete = false
)

val mockSeekerUser = User(
    uid = "2",
    email = "sarah@email.com",
    displayName = "Sarah Johnson",
    userType = UserType.SEEKER,
    profileImageUrl = null,
    bio = "Seeking balance and wellness",
    isEmailVerified = false,
    isProfileComplete = false
)

val mockAlchemistData = Alchemist(
    userId = "1",
    modalities = listOf("Reiki", "Crystal Healing", "Meditation", "Sound Healing"),
    certifications = listOf(
        Certification(
            name = "Reiki Master",
            issuingOrganization = "International Reiki Organization",
            isVerified = true
        ),
        Certification(
            name = "Crystal Healing Practitioner",
            issuingOrganization = "Crystal Academy",
            isVerified = false
        )
    ),
    hourlyRate = 85.0,
    location = Location(
        city = "Melbourne",
        state = "VIC",
        country = "Australia"
    ),
    rating = 4.9f,
    reviewCount = 127,
    yearsOfExperience = 8,
    languages = listOf("English", "Spanish", "French"),
    aboutMe = "I'm a certified Reiki Master and Crystal Healing practitioner with over 8 years of experience. My journey began when I discovered the transformative power of energy healing in my own life. I specialize in helping clients release emotional blockages, find inner peace, and reconnect with their authentic selves. Each session is tailored to your unique needs and energy.",
    isVerified = true
)

val mockSeekerData = Seeker(
    userId = "2",
    interests = listOf("Meditation", "Mindfulness", "Stress Relief", "Personal Growth", "Energy Work"),
    preferredModalities = listOf("Reiki", "Meditation", "Yoga Therapy"),
    location = Location(
        city = "Melbourne",
        state = "VIC",
        country = "Australia"
    ),
    savedAlchemists = listOf("1", "3", "5"),
    bookingHistory = listOf("booking1", "booking2")
)

val alchemistStats = listOf(
    ProfileStat(
        label = "Total Sessions",
        value = "342",
        icon = Icons.Default.CalendarToday,
        iconColor = Color(0xFF6750A4), // Primary color
        backgroundColor = Color(0xFFEADDFF) // Primary container
    ),
    ProfileStat(
        label = "Rating",
        value = "4.9",
        icon = Icons.Default.Star,
        iconColor = Color(0xFFFFB800),
        backgroundColor = Color(0xFFFFF8E1)
    ),
    ProfileStat(
        label = "Active Clients",
        value = "48",
        icon = Icons.Default.People,
        iconColor = Color(0xFF4CAF50),
        backgroundColor = Color(0xFFE8F5E9)
    )
)

val seekerStats = listOf(
    ProfileStat(
        label = "Sessions",
        value = "12",
        icon = Icons.Default.Spa,
        iconColor = Color(0xFF6750A4), // Primary color
        backgroundColor = Color(0xFFEADDFF) // Primary container
    ),
    ProfileStat(
        label = "Saved",
        value = "3",
        icon = Icons.Default.Favorite,
        iconColor = Color(0xFFE91E63),
        backgroundColor = Color(0xFFFCE4EC)
    ),
    ProfileStat(
        label = "Reviews",
        value = "8",
        icon = Icons.Default.RateReview,
        iconColor = Color(0xFF2196F3),
        backgroundColor = Color(0xFFE3F2FD)
    )
)
