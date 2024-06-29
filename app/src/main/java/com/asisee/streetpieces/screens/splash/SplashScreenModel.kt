package com.asisee.streetpieces.screens.splash

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.service.AccountService
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

private const val SPLASH_TIMEOUT = 1000L
@Factory
class SplashScreenModel(
    private val accountService: AccountService
) : ScreenModel, ContainerHost<SplashScreenState, SplashScreenSideEffect> {

    override val container = screenModelScope
        .container<SplashScreenState, SplashScreenSideEffect>(SplashScreenState.Loading) {
            withContext(Dispatchers.IO) {
                delay(SPLASH_TIMEOUT)
                logInOrAsk()
            }
        }


    private fun logInOrAsk() = intent {
        if (accountService.hasUser && !accountService.userIsAnonymous) {
            postSideEffect(SplashScreenSideEffect.NavigateToFeedScreen)
        } else {
            reduce {
                SplashScreenState.LoginOptions
            }
        }
    }

    fun continueAnonymously() = intent {
        if (accountService.hasUser)
            postSideEffect(SplashScreenSideEffect.NavigateToFeedScreen)
        else {
            try {
                withContext(Dispatchers.IO) {
                    accountService.createAnonymousAccount()
                }
                postSideEffect(SplashScreenSideEffect.NavigateToFeedScreen)
            } catch (ex: FirebaseAuthException) {
                postSideEffect(SplashScreenSideEffect.DisplayError)
            }
        }
    }

    fun navigateToSignInScreen() = intent {
        postSideEffect(SplashScreenSideEffect.NavigateToSignInScreen)
    }

    fun navigateToSignUpScreen() = intent {
        postSideEffect(SplashScreenSideEffect.NavigateToSignUpScreen)
    }
}