package com.asisee.streetpieces.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.asisee.streetpieces.common.composable.BasicButton
import com.asisee.streetpieces.common.composable.BasicTextButton
import com.asisee.streetpieces.common.ext.basicButton
import com.asisee.streetpieces.common.ext.textButton
import com.asisee.streetpieces.screens.destinations.LoginScreenDestination
import com.asisee.streetpieces.screens.destinations.SearchFeedScreenDestination
import com.asisee.streetpieces.screens.destinations.SignUpScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import com.asisee.streetpieces.R.string as AppText

private const val SPLASH_TIMEOUT = 1000L

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(navigator: DestinationsNavigator, viewModel: SplashViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState
    Column(
        modifier =
            Modifier.fillMaxWidth()
                .fillMaxHeight()
                .background(color = MaterialTheme.colors.background)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            if (uiState.showError) {
                Text(text = stringResource(AppText.generic_error))
                BasicButton(AppText.try_again, Modifier.basicButton()) {
                    viewModel.onAppStart { navigator.navigate(SearchFeedScreenDestination) }
                }
            } else if (uiState.showProgress) {
                CircularProgressIndicator(color = MaterialTheme.colors.onBackground)
            } else {
                BasicButton(AppText.sign_in, Modifier.basicButton()) {
                    navigator.navigate(LoginScreenDestination)
                }
                BasicButton(AppText.create_account, Modifier.basicButton()) {
                    navigator.navigate(SignUpScreenDestination)
                }
                BasicTextButton(AppText.continue_anonymously, Modifier.textButton()) {
                    viewModel.toMainScreen { navigator.navigate(SearchFeedScreenDestination) }
                }
            }
        }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        viewModel.onAppStart { navigator.navigate(SearchFeedScreenDestination) }
    }
}
