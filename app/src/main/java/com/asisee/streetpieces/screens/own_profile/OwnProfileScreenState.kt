package com.asisee.streetpieces.screens.own_profile

import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData

sealed interface OwnProfileScreenState {
    data object Loading : OwnProfileScreenState
    data class OwnProfile (
        val userData: UserData = UserData(),
        val numberOfFollowers: Long = 0,
        val numberOfFollowees: Long = 0,
        val posts: List<PostData> = emptyList()
    ) : OwnProfileScreenState
}