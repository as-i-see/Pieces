package com.asisee.streetpieces.screens.login

import androidx.compose.runtime.mutableStateOf
import com.asisee.streetpieces.LOGIN_SCREEN
import com.asisee.streetpieces.SEARCH_FEED_SCREEN
import com.asisee.streetpieces.R.string as AppText
import com.asisee.streetpieces.common.ext.isValidEmail
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.screens.LogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : LogViewModel(logService) {
  var uiState = mutableStateOf(LoginUiState())
    private set

  private val email
    get() = uiState.value.email
  private val password
    get() = uiState.value.password

  fun onEmailChange(newValue: String) {
    uiState.value = uiState.value.copy(email = newValue)
  }

  fun onPasswordChange(newValue: String) {
    uiState.value = uiState.value.copy(password = newValue)
  }

  fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    if (password.isBlank()) {
      SnackbarManager.showMessage(AppText.empty_password_error)
      return
    }

    launchCatching {
      accountService.authenticate(email, password)
      openAndPopUp(SEARCH_FEED_SCREEN, LOGIN_SCREEN)
    }
  }

  fun onForgotPasswordClick() {
    if (!email.isValidEmail()) {
      SnackbarManager.showMessage(AppText.email_error)
      return
    }

    launchCatching {
      accountService.sendRecoveryEmail(email)
      SnackbarManager.showMessage(AppText.recovery_email_sent)
    }
  }
}
