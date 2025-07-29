package com.domain.you.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.you.data.models.User
import com.domain.you.data.models.UserType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                loadUserData(firebaseUser.uid)
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun signUp(email: String, password: String, displayName: String, userType: UserType) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // Create auth account
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user

                if (firebaseUser != null) {
                    // Create user document in Firestore
                    val user = User(
                        uid = firebaseUser.uid,
                        email = email,
                        displayName = displayName,
                        userType = userType
                    )

                    firestore.collection("users")
                        .document(firebaseUser.uid)
                        .set(user)
                        .await()

                    // Create role-specific document
                    when (userType) {
                        UserType.ALCHEMIST -> {
                            firestore.collection("alchemists")
                                .document(firebaseUser.uid)
                                .set(mapOf("userId" to firebaseUser.uid))
                                .await()
                        }
                        UserType.SEEKER -> {
                            firestore.collection("seekers")
                                .document(firebaseUser.uid)
                                .set(mapOf("userId" to firebaseUser.uid))
                                .await()
                        }
                    }

                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated(user)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.user?.let { firebaseUser ->
                    loadUserData(firebaseUser.uid)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    private suspend fun loadUserData(uid: String) {
        try {
            val document = firestore.collection("users").document(uid).get().await()
            val user = document.toObject(User::class.java)
            if (user != null) {
                _currentUser.value = user
                _authState.value = AuthState.Authenticated(user)
            } else {
                _authState.value = AuthState.Error("User data not found")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Failed to load user data")
        }
    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = null
        _authState.value = AuthState.Unauthenticated
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                // Handle success - maybe show a message
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to send reset email")
            }
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}