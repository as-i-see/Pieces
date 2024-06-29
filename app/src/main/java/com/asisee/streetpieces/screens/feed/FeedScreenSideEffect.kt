package com.asisee.streetpieces.screens.feed

import com.asisee.streetpieces.model.UserData

sealed interface FeedScreenSideEffect {
    data class NavigateToUserProfile(val userData: UserData) : FeedScreenSideEffect
}