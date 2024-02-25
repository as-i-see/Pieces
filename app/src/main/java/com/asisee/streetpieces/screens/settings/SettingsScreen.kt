package com.asisee.streetpieces.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.asisee.streetpieces.common.composable.BasicToolbar
import com.asisee.streetpieces.common.composable.DangerousCardEditor
import com.asisee.streetpieces.common.composable.DialogCancelButton
import com.asisee.streetpieces.common.composable.DialogConfirmButton
import com.asisee.streetpieces.common.composable.RegularCardEditor
import com.asisee.streetpieces.common.ext.card
import com.asisee.streetpieces.common.ext.spacerM
import com.asisee.streetpieces.screens.destinations.LoginScreenDestination
import com.asisee.streetpieces.screens.destinations.SignUpScreenDestination
import com.asisee.streetpieces.screens.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.asisee.streetpieces.R.drawable as AppIcon
import com.asisee.streetpieces.R.string as AppText

@ExperimentalMaterialApi
@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState(initial = SettingsUiState(false))

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
            BasicToolbar(AppText.settings)

            Spacer(modifier = Modifier.spacerM())
            if (uiState.value.isAnonymousAccount) {
                RegularCardEditor(AppText.sign_in, AppIcon.ic_sign_in, "", Modifier.card()) {
                    navigator.navigate(LoginScreenDestination)
                }

                RegularCardEditor(
                    AppText.create_account, AppIcon.ic_create_account, "", Modifier.card()) {
                        navigator.navigate(SignUpScreenDestination)
                    }
            } else {
                SignOutCard {
                    viewModel.onSignOutClick { navigator.clearBackStack(SplashScreenDestination) }
                }
                DeleteMyAccountCard {
                    viewModel.onDeleteMyAccountClick {
                        navigator.clearBackStack(SplashScreenDestination)
                    }
                }
            }
        }
}

@ExperimentalMaterialApi
@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(AppText.sign_out, AppIcon.ic_exit, "", Modifier.card()) {
        showWarningDialog = true
    }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.sign_out_title)) },
            text = { Text(stringResource(AppText.sign_out_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.sign_out) {
                    signOut()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false })
    }
}

@ExperimentalMaterialApi
@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    DangerousCardEditor(
        AppText.delete_my_account, AppIcon.ic_delete_my_account, "", Modifier.card()) {
            showWarningDialog = true
        }

    if (showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.delete_account_title)) },
            text = { Text(stringResource(AppText.delete_account_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.delete_my_account) {
                    deleteMyAccount()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false })
    }
}
