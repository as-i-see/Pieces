package com.asisee.streetpieces.screens.sign_up

import com.asisee.streetpieces.screens.sign_in.SignInScreenSideEffect

sealed interface SignUpScreenSideEffect {
    data class DisplayMessage(val message: String) : SignUpScreenSideEffect

    data object NavigateToFeedScreen : SignUpScreenSideEffect
}