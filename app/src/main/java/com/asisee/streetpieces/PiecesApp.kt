package com.asisee.streetpieces

import android.Manifest
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.asisee.streetpieces.R.string as AppText
import com.asisee.streetpieces.common.composable.PermissionDialog
import com.asisee.streetpieces.common.composable.RationaleDialog
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.screens.camera.CameraScreen
import com.asisee.streetpieces.screens.create_piece.CreatePieceScreen
import com.asisee.streetpieces.screens.login.LoginScreen
import com.asisee.streetpieces.screens.permission.RequestCameraPermissionDialog
import com.asisee.streetpieces.screens.permission.RequestLocationPermissionDialog
import com.asisee.streetpieces.screens.piece.PieceScreen
import com.asisee.streetpieces.screens.profile.ProfileScreen
import com.asisee.streetpieces.screens.searchfeed.SearchFeedScreen
import com.asisee.streetpieces.screens.settings.SettingsScreen
import com.asisee.streetpieces.screens.sign_up.SignUpScreen
import com.asisee.streetpieces.screens.splash.SplashScreen
import com.asisee.streetpieces.screens.userpieces.UserPiecesScreen
import com.asisee.streetpieces.theme.PiecesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val CURRENT_YEAR = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

@Composable
@ExperimentalMaterialApi
fun PiecesApp() {
    PiecesTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermissionDialog()
        }

        Surface(color = MaterialTheme.colors.background) {
            val appState = rememberAppState()

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colors.onPrimary)
                        })
                },
                scaffoldState = appState.scaffoldState,
                bottomBar = {
                    if (appState.shouldShowBottomBar())
                        BottomBar(navController = appState.navController)
                }) { innerPaddingModifier ->
                    NavHost(
                        navController = appState.navController,
                        startDestination = SPLASH_SCREEN,
                        modifier = Modifier.padding(innerPaddingModifier)) {
                            piecesGraph(appState)
                        }
                }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale)
            RationaleDialog(
                AppText.notification_permission_title, AppText.notification_permission_settings)
        else
            PermissionDialog(
                AppText.notification_permission_title, AppText.request_notification_permission) {
                    permissionState.launchPermissionRequest()
                }
    }
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
        PiecesAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@ExperimentalMaterialApi
fun NavGraphBuilder.piecesGraph(appState: PiecesAppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
            open = { route -> appState.navigate(route) })
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) })
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(SIGN_UP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(TAKE_PHOTO_SCREEN) {
        RequestCameraPermissionDialog()
        CameraScreen(openScreen = { route -> appState.navigate(route) })
    }

    composable(
        route = "$CREATE_PIECE_SCREEN$PHOTO_URI_ARG",
        arguments = listOf(navArgument(PHOTO_URI) { nullable = false })) {
            RequestLocationPermissionDialog()
            CreatePieceScreen(
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
        }

    composable(
        route = "$PROFILE_SCREEN$USER_ID_ARG",
        arguments = listOf(navArgument(USER_ID) { nullable = false })) {
            ProfileScreen(
                openScreen = { route -> appState.navigate(route) },
                openAndPopUpCurrent = { route -> appState.navigateAndPopUpCurrent(route) })
        }

    composable(SEARCH_FEED_SCREEN) {
        SearchFeedScreen(openScreen = { route -> appState.navigate(route) })
    }

    composable(
        route = "$PIECE_SCREEN$PIECE_ID_USER_ID_ARG",
        arguments =
            listOf(
                navArgument(PIECE_ID) { nullable = false },
                navArgument(USER_ID) { nullable = false })) {
            PieceScreen(popUp = appState::popUp, open = appState::navigate)
        }

    composable(
        route = "$USER_PIECES_SCREEN$PIECE_ID_USER_ID_ARG",
        arguments =
            listOf(
                navArgument(PIECE_ID) { nullable = false },
                navArgument(USER_ID) { nullable = false })) {
            UserPiecesScreen(popUp = appState::popUp, open = appState::navigate)
        }
}
