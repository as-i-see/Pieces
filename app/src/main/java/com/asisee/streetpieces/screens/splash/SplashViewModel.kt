package com.asisee.streetpieces.screens.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.LOGIN_SCREEN
import com.asisee.streetpieces.SEARCH_FEED_SCREEN
import com.asisee.streetpieces.SIGN_UP_SCREEN
import com.asisee.streetpieces.SPLASH_SCREEN
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.ConfigurationService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.screens.LogViewModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : LogViewModel(logService) {
  val uiState = mutableStateOf(SplashUiState(showError = false, showProgress = true))
  private val userFlow = accountService.currentUserFlow
  init {
    launchCatching { configurationService.fetchConfiguration() }
  }

  fun onAppStart(openAndPopUp: (String, String) -> Unit) = viewModelScope.launch {
    uiState.value = uiState.value.copy(showError = false)
    if (accountService.hasUser && !accountService.currentUser!!.isAnonymous) {
      openAndPopUp(SEARCH_FEED_SCREEN, SPLASH_SCREEN)
    }
    uiState.value = uiState.value.copy(showProgress = false)
  }

  fun toLoginScreen(open: (String) -> Unit) = open(LOGIN_SCREEN)

  fun toSignUpScreen(open: (String) -> Unit) = open(SIGN_UP_SCREEN)

  fun toMainScreen(openAndPopUp: (String, String) -> Unit) = viewModelScope.launch {
    if (!accountService.hasUser)
      createAnonymousAccount()
    openAndPopUp(SEARCH_FEED_SCREEN, SPLASH_SCREEN)
  }

  private fun createAnonymousAccount() {
    launchCatching(snackbar = false) {
      try {
        accountService.createAnonymousAccount()
      } catch (ex: FirebaseAuthException) {
        uiState.value = uiState.value.copy(showError = true)
        throw ex
      }
    }
  }
}
