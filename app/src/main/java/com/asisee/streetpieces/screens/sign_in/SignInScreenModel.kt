package com.asisee.streetpieces.screens.sign_in

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.ext.isValidEmail
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.screens.own_profile.OwnProfileScreenState
import com.asisee.streetpieces.screens.sign_up.SignUpScreenSideEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
    }

    fun onSignInClick() = intent {
        (state as? SignInScreenState.SignIn)?.let { state ->
            if (!email.isValidEmail()) {
                postMessageSideEffect(AppText.email_error)
            }
            else if (password.isBlank()) {
                postMessageSideEffect(AppText.empty_password_error)
            } else {
                reduce {
                    SignInScreenState.Loading
                }
                try {
                    accountService.authenticate(email, password)
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
            if (!email.isValidEmail()) {
                postMessageSideEffect(AppText.email_error)
            } else {
                reduce {
                    SignInScreenState.Loading
                }
                try {
                    accountService.sendRecoveryEmail(email)
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