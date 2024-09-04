package com.asisee.streetpieces.screens.user_profile

import com.asisee.streetpieces.model.UserPosts

sealed interface UserProfileScreenSideEffect {
    data object NavigateBack: UserProfileScreenSideEffect
    data class NavigateToPiece(val userPosts: UserPosts, val clickedPieceIndex: Int): UserProfileScreenSideEffect
}