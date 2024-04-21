package com.asisee.streetpieces.screens.userpieces

sealed interface UserPiecesSideEffect {
    data object PopBack
    data object NavigateToUserProfile
}