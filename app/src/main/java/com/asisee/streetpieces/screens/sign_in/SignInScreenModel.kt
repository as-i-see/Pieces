package com.asisee.streetpieces.screens.sign_in

import android.content.Context
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.ext.isValidEmail
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.screens.own_profile.OwnProfileScreenState
import com.asisee.streetpieces.screens.sign_up.SignUpScreenSideEffect
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.runOn
import com.asisee.streetpieces.R.string as AppText

@Factory
@OptIn(OrbitExperimental::class)
class SignInScreenModel(
    private val accountService: AccountService, private val context: Context
) : ScreenModel, ContainerHost<SignInScreenState, SignInScreenSideEffect> {

    override val container = screenModelScope
        .container<SignInScreenState, SignInScreenSideEffect>(SignInScreenState.SignIn())

    fun onEmailChange(newValue: String) = intent {
        runOn(SignInScreenState.SignIn::class) {
            reduce {
                state.copy(email = newValue)
            }
        }
    }

    fun onPasswordChange(newValue: String) = intent {
        runOn(SignInScreenState.SignIn::class) {
            reduce {
                state.copy(password = newValue)
            }
        }
    }

    fun onSignInClick() = intent {
        (state as? SignInScreenState.SignIn)?.let { state ->
            if (!state.email.isValidEmail()) {
                postMessageSideEffect(AppText.email_error)
            }
            else if (state.password.isBlank()) {
                postMessageSideEffect(AppText.empty_password_error)
            } else {
                reduce {
                    SignInScreenState.Loading
                }
                try {
                    accountService.authenticate(state.email, state.password)
                    postSideEffect(SignInScreenSideEffect.NavigateToFeedScreen)
                } catch (e: Exception) {
                    postMessageSideEffect(AppText.authentication_error)
                    reduce {
                        state
                    }
                }
            }

        }
    }

    fun onForgotPasswordClick() = intent {
        (state as? SignInScreenState.SignIn)?.let { state ->
            if (!state.email.isValidEmail()) {
                postMessageSideEffect(AppText.email_error)
            } else {
                reduce {
                    SignInScreenState.Loading
                }
                try {
                    accountService.sendRecoveryEmail(state.email)
                    postSideEffect(SignInScreenSideEffect.DisplayMessage(
                        ContextCompat.getString(context, AppText.recovery_email_sent))
                    )
                } catch (e: Exception) {
                    postSideEffect(SignInScreenSideEffect.DisplayMessage(
                        ContextCompat.getString(context, AppText.error))
                    )
                }
                reduce {
                    state
                }
            }
        }
    }

    private fun postMessageSideEffect(messageRes: Int) = intent {
        postSideEffect(
            SignInScreenSideEffect.DisplayMessage(
                ContextCompat.getString(context,messageRes)
            )
        )
    }
}