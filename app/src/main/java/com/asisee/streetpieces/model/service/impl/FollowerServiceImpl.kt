package com.asisee.streetpieces.model.service.impl

import com.asisee.streetpieces.model.Subscription
import com.asisee.streetpieces.model.SubscriptionState
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.FollowerService
import com.asisee.streetpieces.model.service.trace
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class FollowerServiceImpl(
    private val firestore: FirebaseFirestore, private val auth: AccountService
) : FollowerService {
    override fun userSubscriptions(userId: String): Flow<List<Subscription>> =
        firestore
            .collection(SUBSCRIPTIONS_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, userId)
            .dataObjects()

    override fun userFollowers(userId: String): Flow<List<Subscription>> =
        firestore
            .collection(SUBSCRIPTIONS_COLLECTION)
            .whereEqualTo(SUBSCRIPTION_USER_ID_FIELD, userId)
            .dataObjects()

    override suspend fun follow(userId: String, otherUserId: String) =
        trace(SUBSCRIBE_TRACE) {
            firestore
                .collection(SUBSCRIPTIONS_COLLECTION)
                .add(Subscription(userId = userId, subUserId = otherUserId))
                .await()
                .id.let {
                    SubscriptionState.Following(it)
                }
        }

    override suspend fun deleteSubscription(subscriptionId: String) {
        firestore.collection(SUBSCRIPTIONS_COLLECTION).document(subscriptionId).delete().await()
    }

    override suspend fun numberOfFollowers(userId: String) =
        firestore
            .collection(SUBSCRIPTIONS_COLLECTION)
            .whereEqualTo(SUBSCRIPTION_USER_ID_FIELD, userId)
            .count()
            .get(AggregateSource.SERVER)
            .await()
            .count

    override suspend fun numberOfSubscriptions(userId: String) =
        firestore
            .collection(SUBSCRIPTIONS_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, userId)
            .count()
            .get(AggregateSource.SERVER)
            .await()
            .count

    private fun subscriptionQuery(userId: String, otherUserId: String) =
        firestore
            .collection(SUBSCRIPTIONS_COLLECTION)
            .where(
                Filter.and(
                    Filter.equalTo(USER_ID_FIELD, userId),
                    Filter.equalTo(SUBSCRIPTION_USER_ID_FIELD, otherUserId)))

    override suspend fun subscriptionState(userId: String, otherUserId: String) =
        subscriptionQuery(userId, otherUserId).get().await().documents.firstOrNull()?.id.let {
            if (it == null) {
                SubscriptionState.NotFollowing
            } else SubscriptionState.Following(it)
        }

    companion object {
        private const val SUBSCRIPTIONS_COLLECTION = "subscriptions"
        private const val USER_ID_FIELD = "userId"
        private const val SUBSCRIPTION_USER_ID_FIELD = "subUserId"
        private const val SUBSCRIBE_TRACE = "subscribe"
    }
}
