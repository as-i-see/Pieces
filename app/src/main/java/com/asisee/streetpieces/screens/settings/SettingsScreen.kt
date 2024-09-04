package com.asisee.streetpieces.screens.settings

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.asisee.streetpieces.common.composable.BasicToolbar
import com.asisee.streetpieces.common.composable.DangerousCardEditor
import com.asisee.streetpieces.common.composable.DialogCancelButton
import com.asisee.streetpieces.common.composable.DialogConfirmButton
import com.asisee.streetpieces.common.composable.NavBackActionToolbar
import com.asisee.streetpieces.common.composable.RegularCardEditor
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.ext.card
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.asisee.streetpieces.screens.sign_in.SignInScreen
import com.asisee.streetpieces.screens.sign_up.SignUpScreen
import com.asisee.streetpieces.screens.splash.SplashScreen
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.LogIn
import compose.icons.evaicons.outline.LogOut
import compose.icons.evaicons.outline.PersonAdd
import compose.icons.evaicons.outline.Trash2
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<SettingsScreenModel>()
        val state by screenModel.collectAsState()
        val signOutErrorString = stringResource(AppText.sign_out_error)
        val deleteAccountErrorString = stringResource(AppText.delete_account_error)
        val activity = LocalContext.current.findActivity()
        screenModel.collectSideEffect { sideEffect ->
            when(sideEffect) {
                SettingsScreenSideEffect.RestartApp -> {
                    activity.finishAffinity()
                }
                SettingsScreenSideEffect.SignOutError -> {
                    SnackbarManager.showMessage(signOutErrorString.toSnackbarMessage())
                }
                SettingsScreenSideEffect.DeleteAccountError -> {
                    SnackbarManager.showMessage(deleteAccountErrorString.toSnackbarMessage())
                }
                SettingsScreenSideEffect.OpenSignInScreen -> {
                    navigator.push(SignInScreen())
                }
                SettingsScreenSideEffect.OpenSignUpScreen -> {
                    navigator.push(SignUpScreen())
                }
                SettingsScreenSideEffect.PopBack -> {
                    navigator.pop()
                }
            }
        }
        View(
            state = state,
            popBack = screenModel::popBack,
            openSignInScreen = screenModel::openSignInScreen,
            openSignUpScreen = screenModel::openSignUpScreen,
            signOut = screenModel::signOut,
            deleteAccount = screenModel::deleteAccount,
        )
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun View(
        state: SettingsScreenState,
        popBack: () -> Unit,
        openSignInScreen: () -> Unit,
        openSignUpScreen: () -> Unit,
        signOut: () -> Unit,
        deleteAccount: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavBackActionToolbar(navigateBack = popBack, title = "Settings", actionIconButton = {})
            SpacerM()
            if (state.isAnonymousAccount) {
                RegularCardEditor(
                    title = AppText.sign_in,
                    icon = EvaIcons.Outline.LogIn,
                    content = "",
                    modifier = Modifier.card(),
                    onEditClick = openSignInScreen
                )
                RegularCardEditor(
                    title = AppText.create_account,
                    icon = EvaIcons.Outline.PersonAdd,
                    content = "",
                    modifier = Modifier.card(),
                    onEditClick = openSignUpScreen
                )
            } else {
                SignOutCard(signOut)
                DeleteMyAccountCard(deleteAccount)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun SignOutCard(signOut: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    RegularCardEditor(
        title = AppText.sign_out,
        icon = EvaIcons.Outline.LogOut,
        content = "",
        modifier = Modifier.card()
    ) {
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
        title = AppText.delete_my_account,
        icon = EvaIcons.Outline.Trash2,
        content = "",
        modifier = Modifier.card()
    ) {
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

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}