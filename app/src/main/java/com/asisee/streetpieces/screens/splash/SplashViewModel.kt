package com.asisee.streetpieces.screens.splash

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.ConfigurationService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.screens.LogViewModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject
constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : LogViewModel(logService) {
    val uiState = mutableStateOf(SplashUiState(showError = false, showProgress = true))
    private val userFlow = accountService.currentUserFlow

    init {
        launchCatching { configurationService.fetchConfiguration() }
    }

    fun onAppStart(toMainScreen: () -> Unit) =
        viewModelScope.launch {
            uiState.value = uiState.value.copy(showError = false)
            if (accountService.hasUser && !accountService.currentUser!!.isAnonymous) {
                toMainScreen()
            }
            uiState.value = uiState.value.copy(showProgress = false)
        }

    fun toMainScreen(toMainScreen: () -> Unit) =
        viewModelScope.launch {
            if (!accountService.hasUser) createAnonymousAccount()
            toMainScreen()
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
