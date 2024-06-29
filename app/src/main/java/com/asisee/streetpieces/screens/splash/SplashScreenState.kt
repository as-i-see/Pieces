package com.asisee.streetpieces.screens.splash

sealed interface SplashScreenState {
    data object Loading : SplashScreenState
    data object LoginOptions : SplashScreenState
}
