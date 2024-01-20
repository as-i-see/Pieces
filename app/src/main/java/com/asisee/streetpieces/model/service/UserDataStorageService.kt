package com.asisee.streetpieces.model.service

import com.asisee.streetpieces.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataStorageService {
    fun userData(userId: String) : Flow<UserData>
    suspend fun getUserData(userId: String) : UserData
    suspend fun save(userData: UserData): String
    suspend fun update(userData: UserData)
    suspend fun delete(userId: String)
}