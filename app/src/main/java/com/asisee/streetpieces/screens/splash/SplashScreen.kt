package com.asisee.streetpieces.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.asisee.streetpieces.common.composable.BasicButton
import com.asisee.streetpieces.common.composable.BasicTextButton
import com.asisee.streetpieces.common.composable.CenteredColumn
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.common.ext.basicButton
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.screens.feed.FeedTab
import com.asisee.streetpieces.screens.main.MainScreen
import com.asisee.streetpieces.screens.sign_in.SignInScreen
import com.asisee.streetpieces.screens.sign_up.SignUpScreen
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText

private const val SPLASH_TIMEOUT = 1000L

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<SplashScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is SplashScreenSideEffect.NavigateToSignInScreen -> {
                    navigator.push(SignInScreen())
                }
                is SplashScreenSideEffect.NavigateToSignUpScreen -> {
                    navigator.push(SignUpScreen())
                }
                is SplashScreenSideEffect.NavigateToFeedScreen -> {
                    navigator.replaceAll(MainScreen())
                }
                is SplashScreenSideEffect.DisplayError -> {
                    SnackbarManager.showMessage(AppText.error)
                }
            }
        }
        View(state,
            screenModel::navigateToSignInScreen,
            screenModel::navigateToSignUpScreen,
            screenModel::continueAnonymously,
        )
    }

    @Composable
    fun View(
        state: SplashScreenState,
        onSignInClick: () -> Unit,
        onSignUpClick: () -> Unit,
        onContinueAnonymouslyClick: () -> Unit,
    ) {
        when(state) {
            is SplashScreenState.Loading -> {
                ProgressIndicator()
            }
            is SplashScreenState.LoginOptions -> {
                CenteredColumn {
                    BasicButton(AppText.sign_in, Modifier.basicButton(), onSignInClick)
                    BasicButton(AppText.create_account, Modifier.basicButton(), onSignUpClick)
                    BasicTextButton(AppText.continue_anonymously, Modifier.basicButton(), onContinueAnonymouslyClick)
                }
            }
        }

    }
}