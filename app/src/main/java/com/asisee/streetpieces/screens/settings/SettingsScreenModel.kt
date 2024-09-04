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

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.screens.launchCatching
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription

@Factory
class SettingsScreenModel (
    private val accountService: AccountService
) : ScreenModel, ContainerHost<SettingsScreenState, SettingsScreenSideEffect> {

    override val container =
        screenModelScope.container<SettingsScreenState, SettingsScreenSideEffect>(
            SettingsScreenState()
        ) {
            repeatOnSubscription {
                launch {
                    accountService.currentUserFlow.collect {
                        reduce {
                            SettingsScreenState(it.isAnonymous)
                        }
                    }
                }
            }
        }

    fun signOut() = intent {
        launchCatching(
            scope = screenModelScope,
            sideEffectOnError = SettingsScreenSideEffect.SignOutError
        ) {
            accountService.signOut()
            postSideEffect(SettingsScreenSideEffect.RestartApp)

        }
    }

    fun deleteAccount() = intent {
        launchCatching(
            scope = screenModelScope,
            sideEffectOnError = SettingsScreenSideEffect.DeleteAccountError
        ) {
            accountService.deleteAccount()
            postSideEffect(SettingsScreenSideEffect.RestartApp)
        }
    }

    fun openSignInScreen() = intent {
        postSideEffect(SettingsScreenSideEffect.OpenSignInScreen)
    }

    fun openSignUpScreen() = intent {
        postSideEffect(SettingsScreenSideEffect.OpenSignUpScreen)
    }

    fun popBack() = intent {
        postSideEffect(SettingsScreenSideEffect.PopBack)
    }
}