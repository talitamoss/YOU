package com.domain.you.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.domain.you.data.models.Alchemist
import com.domain.you.data.models.Location
import com.domain.you.ui.theme.YOUTheme

@Composable
fun AlchemistCard(
    alchemist: Alchemist,
    alchemistName: String,
    alchemistImageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image section with verified badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = alchemistImageUrl,
                    contentDescription = "$alchemistName profile image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 100f
                            )
                        )
                )
                
                // Verified badge
                if (alchemist.isVerified) {
                    Surface(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.TopEnd),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = "Verified",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Verified",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
                
                // Name and location overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        alchemistName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    alchemist.location?.let { location ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${location.city}, ${location.state}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
            
            // Content section
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Specialties/Modalities
                if (alchemist.modalities.isNotEmpty()) {
                    Text(
                        alchemist.modalities.take(3).joinToString(" • "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // About preview
                if (alchemist.aboutMe.isNotEmpty()) {
                    Text(
                        alchemist.aboutMe,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFFFFB800)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${alchemist.rating}",
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            " (${alchemist.reviewCount})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Price
                    alchemist.hourlyRate?.let { rate ->
                        Text(
                            "$${"%.0f".format(rate)}/hr",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (alchemist.yearsOfExperience > 0) {
                        AssistChip(
                            onClick = { },
                            label = { 
                                Text(
                                    "${alchemist.yearsOfExperience}+ years",
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.height(32.dp)
                        )
                    }
                    
                    if (alchemist.languages.size > 1) {
                        AssistChip(
                            onClick = { },
                            label = { 
                                Text(
                                    "${alchemist.languages.size} languages",
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.height(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompactAlchemistCard(
    alchemist: Alchemist,
    alchemistName: String,
    alchemistImageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = alchemistImageUrl,
                contentDescription = "$alchemistName profile image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        alchemistName,
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
                    alchemist.modalities.take(2).joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                    alchemist.location?.let { location ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            location.city,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                alchemist.hourlyRate?.let { rate ->
                    Text(
                        "$${"%.0f".format(rate)}",
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
}

@Preview(showBackground = true)
@Composable
fun AlchemistCardPreview() {
    YOUTheme {
        AlchemistCard(
            alchemist = Alchemist(
                userId = "1",
                modalities = listOf("Reiki", "Crystal Healing", "Meditation"),
                hourlyRate = 85.0,
                location = Location(city = "Melbourne", state = "VIC", country = "Australia"),
                rating = 4.9f,
                reviewCount = 127,
                yearsOfExperience = 8,
                languages = listOf("English", "Spanish"),
                aboutMe = "Experienced energy healer specializing in Reiki and crystal therapy. Passionate about helping others find balance and peace.",
                isVerified = true
            ),
            alchemistName = "Luna Starweaver",
            alchemistImageUrl = "https://picsum.photos/400/300",
            onClick = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CompactAlchemistCardPreview() {
    YOUTheme {
        CompactAlchemistCard(
            alchemist = Alchemist(
                userId = "1",
                modalities = listOf("Reiki", "Crystal Healing"),
                hourlyRate = 85.0,
                location = Location(city = "Melbourne", state = "VIC"),
                rating = 4.9f,
                reviewCount = 127,
                isVerified = true
            ),
            alchemistName = "Luna Starweaver",
            alchemistImageUrl = "https://picsum.photos/200/200",
            onClick = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}
