package com.asisee.streetpieces.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.asisee.streetpieces.common.composable.BasicButton
import com.asisee.streetpieces.common.composable.BasicTextButton
import com.asisee.streetpieces.common.composable.BasicToolbar
import com.asisee.streetpieces.common.composable.EmailField
import com.asisee.streetpieces.common.composable.PasswordField
import com.asisee.streetpieces.common.ext.basicButton
import com.asisee.streetpieces.common.ext.fieldModifier
import com.asisee.streetpieces.common.ext.textButton
import com.asisee.streetpieces.screens.destinations.SearchFeedScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.asisee.streetpieces.R.string as AppText

@Destination
@Composable
fun LoginScreen(navigator: DestinationsNavigator, viewModel: LoginViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState

    BasicToolbar(AppText.login_details)

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            EmailField(uiState.email, viewModel::onEmailChange, Modifier.fieldModifier())
            PasswordField(uiState.password, viewModel::onPasswordChange, Modifier.fieldModifier())

            BasicButton(AppText.sign_in, Modifier.basicButton()) {
                viewModel.onSignInClick { navigator.navigate(SearchFeedScreenDestination) }
            }

            BasicTextButton(AppText.forgot_password, Modifier.textButton()) {
                viewModel.onForgotPasswordClick()
            }
        }
}
