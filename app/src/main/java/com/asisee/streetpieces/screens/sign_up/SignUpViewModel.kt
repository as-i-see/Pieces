package com.asisee.streetpieces.screens.sign_up

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.asisee.streetpieces.common.ext.MAX_USERNAME_LENGTH
import com.asisee.streetpieces.common.ext.isValidBio
import com.asisee.streetpieces.common.ext.isValidEmail
import com.asisee.streetpieces.common.ext.isValidName
import com.asisee.streetpieces.common.ext.isValidPassword
import com.asisee.streetpieces.common.ext.isValidUsername
import com.asisee.streetpieces.common.ext.passwordMatches
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PhotoStorageService
import com.asisee.streetpieces.model.service.UserDataStorageService
import com.asisee.streetpieces.screens.LogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.asisee.streetpieces.R.string as AppText

@HiltViewModel
class SignUpViewModel
@Inject
constructor(
    private val accountService: AccountService,
    private val userDataStorageService: UserDataStorageService,
    private val photoStorageService: PhotoStorageService,
    logService: LogService
) : LogViewModel(logService) {
    var uiState = mutableStateOf(SignUpUiState())
        private set

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onUsernameChange(newValue: String) {
        if (newValue.length > MAX_USERNAME_LENGTH)
            SnackbarManager.showMessage(AppText.username_error)
        else uiState.value = uiState.value.copy(username = newValue)
    }

    fun onNameChange(newValue: String) {
        if (!newValue.isValidName()) SnackbarManager.showMessage(AppText.name_error)
        else uiState.value = uiState.value.copy(name = newValue)
    }

    fun onPhotoUriChosen(uri: Uri?) =
        uri?.let { chosenPhotoUri -> uiState.value = uiState.value.copy(photoUri = chosenPhotoUri) }

    fun onBioChange(newValue: String) {
        if (!newValue.isValidBio()) SnackbarManager.showMessage(AppText.bio_error)
        else uiState.value = uiState.value.copy(bio = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(toMainScreen: () -> Unit) {
        uiState.value.run {
            if (!email.isValidEmail()) {
                SnackbarManager.showMessage(AppText.email_error)
                return
            }
            if (!password.isValidPassword()) {
                SnackbarManager.showMessage(AppText.password_error)
                return
            }

            if (!password.passwordMatches(uiState.value.repeatPassword)) {
                SnackbarManager.showMessage(AppText.password_match_error)
                return
            }

            if (!username.isValidUsername()) {
                SnackbarManager.showMessage(AppText.username_error)
                return
            }

            launchCatching {
                if (!accountService.hasUser) accountService.createAnonymousAccount()
                accountService.linkAccount(email, password)
                val remotePhotoUri =
                    if (photoUri != Uri.EMPTY) photoStorageService.uploadAvatar(photoUri) else ""
                userDataStorageService.save(
                    UserData(
                        name = name, username = username, bio = bio, photoUri = remotePhotoUri))
                toMainScreen()
            }
        }
    }
}
