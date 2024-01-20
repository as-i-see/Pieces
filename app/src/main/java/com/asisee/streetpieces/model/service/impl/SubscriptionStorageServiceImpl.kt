package com.asisee.streetpieces.model.service.impl

import com.asisee.streetpieces.model.Subscription
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.SubscriptionStorageService
import com.asisee.streetpieces.model.service.trace
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SubscriptionStorageServiceImpl @Inject constructor(private val firestore: FirebaseFirestore, private val auth: AccountService): SubscriptionStorageService {
    override fun subscriptionsOfUserInId(userId: String): Flow<List<String>> =
        firestore.collection(SUBSCRIPTIONS_COLLECTION).whereEqualTo(USER_ID_FIELD, userId).dataObjects<Subscription>().map { it.map { it.subUserId } }

    override fun subscribersOfUserInId(userId: String): Flow<List<String>> =
        firestore.collection(SUBSCRIPTIONS_COLLECTION).whereEqualTo(SUBSCRIPTION_USER_ID_FIELD, userId).dataObjects<Subscription>().map { it.map { it.userId } }

    override suspend fun subscribeCurrentUserTo(userId: String) = trace(SUBSCRIBE_TRACE) {
        firestore.collection(SUBSCRIPTIONS_COLLECTION).add(Subscription(userId = auth.currentUserId, subUserId = userId)).await().id
    }

    override suspend fun deleteSubscription(subscriptionId: String) {
        firestore.collection(SUBSCRIPTIONS_COLLECTION).document(subscriptionId).delete().await()
    }

    override suspend fun numberOfSubscribers(userId: String) =
        firestore.collection(SUBSCRIPTIONS_COLLECTION).whereEqualTo(SUBSCRIPTION_USER_ID_FIELD, userId).count().get(AggregateSource.SERVER).await().count

    override suspend fun numberOfSubscriptions(userId: String) =
        firestore.collection(SUBSCRIPTIONS_COLLECTION).whereEqualTo(USER_ID_FIELD, userId).count().get(AggregateSource.SERVER).await().count


    private fun subscriptionQuery(userId: String) = firestore.collection(SUBSCRIPTIONS_COLLECTION)
        .where(
            Filter.and(
                Filter.equalTo(USER_ID_FIELD, auth.currentUser),
                Filter.equalTo(SUBSCRIPTION_USER_ID_FIELD, userId)
            )
        )

    override suspend fun subscriptionId(userId: String) = subscriptionQuery(userId).get().await().documents.firstOrNull()?.id


    companion object {
        private const val SUBSCRIPTIONS_COLLECTION = "subscriptions"
        private const val USER_ID_FIELD = "userId"
        private const val SUBSCRIPTION_USER_ID_FIELD = "subUserId"
        private const val SUBSCRIBE_TRACE = "subscribe"
    }
}