package com.asisee.streetpieces

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.asisee.streetpieces.common.ext.routeWithoutParameters
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.common.snackbar.SnackbarMessage.Companion.toMessage
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
  private val bottomBarRoutes = listOf(
    BottomBarDestination.SearchFeed, BottomBarDestination.Profile
  ).map { it.route }

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

  fun navigateAndPopUp(route: String, popUp: String) {
    navController.navigate(route) {
      launchSingleTop = true
      popUpTo(popUp) { inclusive = true }
    }
  }

  fun navigateAndPopUpCurrent(route: String) {
    navController.navigate(route) {
      launchSingleTop = true
      popUp()
    }
  }

  fun clearAndNavigate(route: String) {
    navController.navigate(route) {
      launchSingleTop = true
      popUpTo(0) { inclusive = true }
    }
  }

  @Composable fun shouldShowBottomBar() : Boolean =
    navController.currentBackStackEntryAsState().value?.destination?.route?.routeWithoutParameters() in bottomBarRoutes.map { it.routeWithoutParameters() }
}
