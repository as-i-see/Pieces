package com.asisee.streetpieces.screens.sign_in

sealed interface SignInScreenSideEffect {
    data class DisplayMessage(val message: String) : SignInScreenSideEffect

    data object NavigateToFeedScreen : SignInScreenSideEffect
}