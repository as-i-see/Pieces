package com.asisee.streetpieces

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.common.snackbar.SnackbarMessage.Companion.toMessage
import com.asisee.streetpieces.screens.destinations.CameraScreenDestination
import com.asisee.streetpieces.screens.destinations.LoginScreenDestination
import com.asisee.streetpieces.screens.destinations.SignUpScreenDestination
import com.asisee.streetpieces.screens.destinations.SplashScreenDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
class PiecesAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                scaffoldState.snackbarHostState.showSnackbar(text)
            }
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    @Composable
    fun shouldHideBottomBar(): Boolean =
        navController.currentDestination() in
            listOf(
                SplashScreenDestination,
                SignUpScreenDestination,
                LoginScreenDestination,
                CameraScreenDestination)
}
