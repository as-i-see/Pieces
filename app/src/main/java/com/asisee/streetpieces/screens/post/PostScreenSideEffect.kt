package com.asisee.streetpieces.screens.post

import com.asisee.streetpieces.model.UserData

sealed interface PostScreenSideEffect {
    data object PopBack : PostScreenSideEffect
    data class NavigateToUserProfile(val userData: UserData) : PostScreenSideEffect
    data class NavigateToOwnProfile(val userData: UserData) : PostScreenSideEffect
}