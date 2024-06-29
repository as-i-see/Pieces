package com.asisee.streetpieces.screens.sign_up

import android.content.Context
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.common.ext.isValidEmail
import com.asisee.streetpieces.common.ext.isValidPassword
import com.asisee.streetpieces.common.ext.isValidUsername
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.PhotoStorageService
import com.asisee.streetpieces.model.service.UserDataService
import kotlinx.coroutines.Dispatchers
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
class SignUpScreenModel(
    private val accountService: AccountService,
    private val userDataService: UserDataService,
    private val photoStorageService: PhotoStorageService,
    private val context: Context
) : ScreenModel, ContainerHost<SignUpScreenState, SignUpScreenSideEffect> {

    override val container = screenModelScope
        .container<SignUpScreenState, SignUpScreenSideEffect>(SignUpScreenState.SignUp())

    fun onEmailChange(newValue: String) = intent {
        runOn(SignUpScreenState.SignUp::class) {
            reduce {
                state.copy(email = newValue)
            }
        }
    }

    fun onPasswordChange(newValue: String) = intent {
        runOn(SignUpScreenState.SignUp::class) {
            reduce {
                state.copy(password = newValue)
            }
        }
    }

    fun onRepeatPasswordChange(newValue: String) = intent {
        runOn(SignUpScreenState.SignUp::class) {
            reduce {
                state.copy(repeatPassword = newValue)
            }
        }
    }

    fun onUsernameChange(newValue: String) = intent {
        runOn(SignUpScreenState.SignUp::class) {
            reduce {
                state.copy(username = newValue)
            }
        }
    }

    fun onNameChange(newValue: String) = intent {
        runOn(SignUpScreenState.SignUp::class) {
            reduce {
                state.copy(name = newValue)
            }
        }
    }

    fun onBioChange(newValue: String) = intent {
        runOn(SignUpScreenState.SignUp::class) {
            reduce {
                state.copy(bio = newValue)
            }
        }
    }

    fun onProfilePictureChoose(pictureUri: String?) = intent {
        pictureUri?.let { uri ->
            runOn(SignUpScreenState.SignUp::class) {
                reduce {
                    state.copy(profilePictureUrl = uri)
                }
            }
        }
    }

    fun onSignUpClick() = intent {
        (state as? SignUpScreenState.SignUp)?.let { state ->
            if (!state.email.isValidEmail()) {
                postMessageSideEffect(AppText.email_error)
            } else if (!state.password.isValidPassword()) {
                postMessageSideEffect(AppText.password_error)
            } else if (state.password != state.repeatPassword) {
                postMessageSideEffect(AppText.password_match_error)
            } else if (!state.username.isValidUsername()) {
                postMessageSideEffect(AppText.username_error)
            } else {
                reduce {
                    SignUpScreenState.Loading
                }
                try {
                    if (!accountService.hasUser) {
                        withContext(Dispatchers.IO) {
                            accountService.createAnonymousAccount()
                        }
                    }
                    accountService.linkAccount(state.email, state.password)
                    val uploadedProfilePictureUrl =
                        if (state.profilePictureUrl.isNotEmpty())
                            photoStorageService.uploadAvatar(state.profilePictureUrl)
                        else ""
                    userDataService.save(
                        UserData(
                            name = state.name,
                            username = state.username,
                            bio = state.bio,
                            profilePictureUrl = uploadedProfilePictureUrl
                        )
                    )
                    postSideEffect(SignUpScreenSideEffect.NavigateToFeedScreen)
                } catch (e: Exception) {
                    postMessageSideEffect(AppText.error)
                    reduce {
                        state
                    }
                }
            }
        }
    }

    private fun postMessageSideEffect(messageRes: Int) = intent {
        postSideEffect(
            SignUpScreenSideEffect.DisplayMessage(
                ContextCompat.getString(context, messageRes)
            )
        )
    }
}