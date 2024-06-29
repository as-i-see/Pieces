package com.asisee.streetpieces.model.service

import com.asisee.streetpieces.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val userIsAnonymous: Boolean
    val currentUser: User?
    val currentUserFlow: Flow<User>

    suspend fun authenticate(email: String, password: String)

    suspend fun sendRecoveryEmail(email: String)

    suspend fun createAnonymousAccount()

    suspend fun linkAccount(email: String, password: String)

    suspend fun deleteAccount()

    suspend fun signOut()
}
