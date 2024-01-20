package com.asisee.streetpieces.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.asisee.streetpieces.R.string as AppText
import com.asisee.streetpieces.common.composable.BasicButton
import com.asisee.streetpieces.common.composable.BasicTextButton
import com.asisee.streetpieces.common.ext.basicButton
import com.asisee.streetpieces.common.ext.textButton
import kotlinx.coroutines.delay

private const val SPLASH_TIMEOUT = 1000L

@Composable
fun SplashScreen(
  openAndPopUp: (String, String) -> Unit,
  open: (String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SplashViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState
  Column(
    modifier =
    modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .background(color = MaterialTheme.colors.background)
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (uiState.showError) {
      Text(text = stringResource(AppText.generic_error))
      BasicButton(AppText.try_again, Modifier.basicButton()) { viewModel.onAppStart(openAndPopUp) }
    } else if (uiState.showProgress) {
      CircularProgressIndicator(color = MaterialTheme.colors.onBackground)
    } else {
      BasicButton(AppText.sign_in, Modifier.basicButton()) { viewModel.toLoginScreen(open) }
      BasicButton(AppText.create_account, Modifier.basicButton()) { viewModel.toSignUpScreen(open) }
      BasicTextButton(AppText.continue_anonymously, Modifier.textButton()) {
        viewModel.toMainScreen(openAndPopUp)
      }
    }
  }

  LaunchedEffect(true) {
    delay(SPLASH_TIMEOUT)
    viewModel.onAppStart(openAndPopUp)
  }
}
