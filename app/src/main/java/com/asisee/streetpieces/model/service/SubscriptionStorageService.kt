package com.asisee.streetpieces.model.service

import kotlinx.coroutines.flow.Flow

interface SubscriptionStorageService {
    fun subscriptionsOfUserInId(userId: String): Flow<List<String>>

    fun subscribersOfUserInId(userId: String): Flow<List<String>>

    suspend fun numberOfSubscribers(userId: String): Long

    suspend fun numberOfSubscriptions(userId: String): Long

    suspend fun subscribeCurrentUserTo(userId: String): String

    suspend fun deleteSubscription(subscriptionId: String)

    suspend fun subscriptionId(userId: String): String?
}
