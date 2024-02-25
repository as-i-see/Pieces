/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.asisee.streetpieces.screens.settings

import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.screens.LogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    logService: LogService,
    private val accountService: AccountService,
) : LogViewModel(logService) {
    val uiState = accountService.currentUserFlow.map { SettingsUiState(it.isAnonymous) }

    fun onSignOutClick(restartApp: () -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp()
        }
    }

    fun onDeleteMyAccountClick(restartApp: () -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            restartApp()
        }
    }
}
