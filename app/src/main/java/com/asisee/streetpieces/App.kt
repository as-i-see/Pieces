package com.asisee.streetpieces

import android.Manifest
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.asisee.streetpieces.common.composable.PermissionDialog
import com.asisee.streetpieces.common.composable.RationaleDialog
import com.asisee.streetpieces.common.composable.TabNavigationItem
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.screens.feed.FeedTab
import com.asisee.streetpieces.screens.own_profile.OwnProfileTab
import com.asisee.streetpieces.screens.searchfeed.SearchFeedTab
import com.asisee.streetpieces.screens.splash.SplashScreen
import com.asisee.streetpieces.theme.PiecesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.KoinAndroidContext
import com.asisee.streetpieces.R.string as AppText

val CURRENT_YEAR = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

@Composable
@ExperimentalMaterialApi
fun App(context: Context) {
    PiecesTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermissionDialog()
        }
        KoinAndroidContext {
            Surface {
                val appState = rememberAppState()
                Navigator(SplashScreen()) { navigator ->
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(
                                hostState = appState.snackbarHostState,
                                modifier = Modifier.padding(8.dp),
                                snackbar = { snackbarData ->
                                    Snackbar(snackbarData)
                                }
                            )
                        },
//                        bottomBar = {
//                            BottomNavigation {
//                                TabNavigationItem(FeedTab)
//                                TabNavigationItem(SearchFeedTab)
//                                TabNavigationItem(OwnProfileTab)
//                            }
//                        }
                    ) { innerPaddingModifier ->
                        Box(
                            modifier = Modifier.padding(innerPaddingModifier)
                        ) {
                            CurrentScreen()
                        }
                    }
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
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        PiecesAppState(snackbarHostState, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

// @ExperimentalMaterialApi
// fun NavGraphBuilder.piecesGraph(appState: PiecesAppState) {
//    composable(SPLASH_SCREEN_ROUTE) {
//        SplashScreen(
//            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
//            open = { route -> appState.navigate(route) })
//    }
//
//    composable(SETTINGS_SCREEN) {
//        SettingsScreen(
//            restartApp = { route -> appState.clearAndNavigate(route) },
//            openScreen = { route -> appState.navigate(route) })
//    }
//
//    composable(LOGIN_SCREEN) {
//        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
//    }
//
//    composable(SIGN_UP_SCREEN) {
//        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
//    }
//
//    composable(TAKE_PHOTO_SCREEN) {
//        RequestCameraPermissionDialog()
//        CameraScreen(openScreen = { route -> appState.navigate(route) })
//    }
//
//    composable(
//        route = "$CREATE_PIECE_SCREEN$PHOTO_URI_ARG",
//        arguments = listOf(navArgument(PHOTO_URI) { nullable = false })) {
//            RequestLocationPermissionDialog()
//            CreatePieceScreen(
//                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
//        }
//
//    composable(
//        route = "$PROFILE_SCREEN$USER_ID_ARG",
//        arguments = listOf(navArgument(USER_ID) { nullable = false })) {
//            ProfileScreen(
//                openScreen = { route -> appState.navigate(route) },
//                openAndPopUpCurrent = { route -> appState.navigateAndPopUpCurrent(route) })
//        }
//
//    composable(SEARCH_FEED_SCREEN) {
//        SearchFeedScreen(openScreen = { route -> appState.navigate(route) })
//    }
//
//    composable(
//        route = "$PIECE_SCREEN$PIECE_ID_USER_ID_ARG",
//        arguments =
//            listOf(
//                navArgument(PIECE_ID) { nullable = false },
//                navArgument(USER_ID) { nullable = false })) {
//            PieceScreen(popUp = appState::popUp, open = appState::navigate)
//        }
//
//    composable(
//        route = "$USER_PIECES_SCREEN$PIECE_ID_USER_ID_ARG",
//        arguments =
//            listOf(
//                navArgument(PIECE_ID) { nullable = false },
//                navArgument(USER_ID) { nullable = false })) {
//            UserPiecesScreen(popUp = appState::popUp, open = appState::navigate)
//        }
// }
