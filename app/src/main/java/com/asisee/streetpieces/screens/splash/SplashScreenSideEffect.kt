package com.asisee.streetpieces.screens.splash

sealed interface SplashScreenSideEffect {

    data object NavigateToFeedScreen: SplashScreenSideEffect

    data object NavigateToSignInScreen : SplashScreenSideEffect

    data object NavigateToSignUpScreen : SplashScreenSideEffect

    data object DisplayError: SplashScreenSideEffect

}