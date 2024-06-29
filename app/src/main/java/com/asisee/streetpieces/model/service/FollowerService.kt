package com.asisee.streetpieces.model.service
import com.asisee.streetpieces.model.SubscriptionState
import kotlinx.coroutines.flow.Flow

interface FollowerService {
    fun userFolloweesInId(userId: String): Flow<List<String>>

    fun userFollowersInId(userId: String): Flow<List<String>>

    suspend fun numberOfFollowers(userId: String): Long

    suspend fun numberOfFollowees(userId: String): Long

    suspend fun follow(userId: String, otherUserId: String): SubscriptionState.Following

    suspend fun deleteSubscription(subscriptionId: String)

    suspend fun subscriptionState(userId: String, otherUserId: String): SubscriptionState
}
