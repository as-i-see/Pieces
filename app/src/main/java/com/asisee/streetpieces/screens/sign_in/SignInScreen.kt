package com.asisee.streetpieces.screens.sign_in

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.BasicButton
import com.asisee.streetpieces.common.composable.BasicTextButton
import com.asisee.streetpieces.common.composable.BasicToolbar
import com.asisee.streetpieces.common.composable.CenteredColumn
import com.asisee.streetpieces.common.composable.EmailField
import com.asisee.streetpieces.common.composable.ErrorIndicator
import com.asisee.streetpieces.common.composable.PasswordField
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.common.ext.basicButton
import com.asisee.streetpieces.common.ext.fieldModifier
import com.asisee.streetpieces.common.ext.textButton
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.common.snackbar.SnackbarMessage
import com.asisee.streetpieces.screens.main.MainScreen
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText

class SignInScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<SignInScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is SignInScreenSideEffect.NavigateToFeedScreen -> {
                    navigator.replaceAll(MainScreen())
                }
                is SignInScreenSideEffect.DisplayMessage -> {
                    SnackbarManager.showMessage(SnackbarMessage.StringSnackbar(sideEffect.message))
                }

            }
        }
        View(
            state,
            screenModel::onEmailChange,
            screenModel::onPasswordChange,
            screenModel::onSignInClick,
            screenModel::onForgotPasswordClick,
        )

    }

    @Composable
    fun View(
        state: SignInScreenState,
        onEmailChange: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onSignInClick: () -> Unit,
        onForgotPasswordClick: () -> Unit,
    ) {
        when(state) {
            is SignInScreenState.Loading -> {
                ProgressIndicator()
            }
            is SignInScreenState.SignIn -> {
                BasicToolbar(AppText.login_details)

                CenteredColumn {

                    EmailField(state.email, onEmailChange, Modifier.fieldModifier())

                    PasswordField(state.password, onPasswordChange, Modifier.fieldModifier())

                    BasicButton(AppText.sign_in, Modifier.basicButton(), onSignInClick)

                    BasicTextButton(AppText.forgot_password, Modifier.textButton(), onForgotPasswordClick)
                }
            }
        }

    }
}