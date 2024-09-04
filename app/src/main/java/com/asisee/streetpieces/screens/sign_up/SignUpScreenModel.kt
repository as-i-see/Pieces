package com.asisee.streetpieces.screens.sign_up

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var username by mutableStateOf("")
        private set
    var name by mutableStateOf("")
        private set
    var bio by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var repeatPassword by mutableStateOf("")
        private set

    fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
    }

    fun onRepeatPasswordChange(newValue: String) {
        repeatPassword = newValue
    }

    fun onUsernameChange(newValue: String) {
        username = newValue
    }

    fun onNameChange(newValue: String) {
        name = newValue
    }

    fun onBioChange(newValue: String) {
        bio = newValue
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
            if (!email.isValidEmail()) {
                postMessageSideEffect(AppText.email_error)
            } else if (!password.isValidPassword()) {
                postMessageSideEffect(AppText.password_error)
            } else if (password != repeatPassword) {
                postMessageSideEffect(AppText.password_match_error)
            } else if (!username.isValidUsername()) {
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
                    accountService.linkAccount(email, password)
                    val uploadedProfilePictureUrl =
                        if (state.profilePictureUrl.isNotEmpty())
                            photoStorageService.uploadAvatar(state.profilePictureUrl)
                        else ""
                    userDataService.save(
                        UserData(
                            name = name,
                            username = username,
                            bio = bio,
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