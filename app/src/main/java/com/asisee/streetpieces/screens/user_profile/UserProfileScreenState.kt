package com.asisee.streetpieces.screens.user_profile

import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.SubscriptionState
import com.asisee.streetpieces.model.UserData

sealed interface UserProfileScreenState {
    data object Loading : UserProfileScreenState
    data class UserProfile (
        val userData: UserData,
        val numberOfFollowers: Long = 0,
        val numberOfFollowees: Long = 0,
        val subscriptionState: SubscriptionState,
        val pieces: List<PostData> = emptyList()
    ) : UserProfileScreenState
}