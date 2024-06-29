package com.asisee.streetpieces.model

sealed interface SubscriptionState {
    data object NotFollowing: SubscriptionState

    data class Following(val subscriptionId: String) : SubscriptionState
}