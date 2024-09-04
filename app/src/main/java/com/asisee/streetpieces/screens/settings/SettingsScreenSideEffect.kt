package com.asisee.streetpieces.screens.settings

sealed interface SettingsScreenSideEffect {
    data object PopBack: SettingsScreenSideEffect

    data object RestartApp: SettingsScreenSideEffect

    data object SignOutError: SettingsScreenSideEffect

    data object DeleteAccountError: SettingsScreenSideEffect

    data object OpenSignInScreen : SettingsScreenSideEffect

    data object OpenSignUpScreen: SettingsScreenSideEffect
}