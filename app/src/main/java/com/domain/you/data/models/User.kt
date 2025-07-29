package com.domain.you.data.models

import com.google.firebase.Timestamp

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val userType: UserType = UserType.SEEKER,
    val profileImageUrl: String? = null,
    val bio: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val isEmailVerified: Boolean = false,
    val isProfileComplete: Boolean = false
)

enum class UserType {
    SEEKER,
    ALCHEMIST
}

data class Alchemist(
    val userId: String = "",
    val modalities: List<String> = emptyList(),
    val certifications: List<Certification> = emptyList(),
    val hourlyRate: Double? = null,
    val location: Location? = null,
    val availability: List<Availability> = emptyList(),
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val yearsOfExperience: Int = 0,
    val specializations: List<String> = emptyList(),
    val languages: List<String> = listOf("English"),
    val aboutMe: String = "",
    val isVerified: Boolean = false
)

data class Seeker(
    val userId: String = "",
    val interests: List<String> = emptyList(),
    val preferredModalities: List<String> = emptyList(),
    val location: Location? = null,
    val savedAlchemists: List<String> = emptyList(),
    val bookingHistory: List<String> = emptyList()
)

data class Certification(
    val name: String = "",
    val issuingOrganization: String = "",
    val dateObtained: Timestamp? = null,
    val imageUrl: String? = null,
    val isVerified: Boolean = false
)

data class Location(
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val postalCode: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)

data class Availability(
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val startTime: String = "", // Format: "09:00"
    val endTime: String = "" // Format: "17:00"
)

enum class DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

data class Service(
    val id: String = "",
    val alchemistId: String = "",
    val title: String = "",
    val description: String = "",
    val modality: String = "",
    val duration: Int = 60, // in minutes
    val price: Double = 0.0,
    val isOnline: Boolean = true,
    val isInPerson: Boolean = false,
    val maxParticipants: Int = 1
)

data class Review(
    val id: String = "",
    val reviewerId: String = "",
    val reviewerName: String = "",
    val alchemistId: String = "",
    val serviceId: String? = null,
    val rating: Float = 0f,
    val comment: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val isVerifiedBooking: Boolean = false
)

// Healing Modalities List
object HealingModalities {
    val list = listOf(
        "Reiki",
        "Acupuncture",
        "Massage Therapy",
        "Yoga Therapy",
        "Meditation",
        "Sound Healing",
        "Crystal Healing",
        "Aromatherapy",
        "Hypnotherapy",
        "Life Coaching",
        "Nutritional Counseling",
        "Herbalism",
        "Energy Healing",
        "Breathwork",
        "Art Therapy",
        "Dance Therapy",
        "Tai Chi",
        "Qigong",
        "Reflexology",
        "Ayurveda",
        "Traditional Chinese Medicine",
        "Homeopathy",
        "Naturopathy",
        "Shamanic Healing",
        "Chakra Balancing"
    ).sorted()
}