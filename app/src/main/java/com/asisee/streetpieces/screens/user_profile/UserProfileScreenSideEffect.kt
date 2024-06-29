package com.asisee.streetpieces.screens.user_profile

import com.asisee.streetpieces.model.UserPieces

sealed interface UserProfileScreenSideEffect {
    data object NavigateBack: UserProfileScreenSideEffect
    data class NavigateToPiece(val userPieces: UserPieces, val clickedPieceIndex: Int): UserProfileScreenSideEffect
}