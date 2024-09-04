package com.asisee.streetpieces.model.service
import com.asisee.streetpieces.model.Subscription
import com.asisee.streetpieces.model.SubscriptionState
import kotlinx.coroutines.flow.Flow

interface FollowerService {
    fun userSubscriptions(userId: String): Flow<List<Subscription>>

    fun userFollowers(userId: String): Flow<List<Subscription>>

    suspend fun numberOfFollowers(userId: String): Long

    suspend fun numberOfSubscriptions(userId: String): Long

    suspend fun follow(userId: String, otherUserId: String): SubscriptionState.Following

    suspend fun deleteSubscription(subscriptionId: String)

    suspend fun subscriptionState(userId: String, otherUserId: String): SubscriptionState
}
