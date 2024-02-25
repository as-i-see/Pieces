package com.asisee.streetpieces

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.asisee.streetpieces.screens.NavGraphs
import com.asisee.streetpieces.screens.appCurrentDestinationAsState
import com.asisee.streetpieces.screens.destinations.Destination
import com.asisee.streetpieces.screens.startAppDestination
import com.ramcosta.composedestinations.navigation.navigate

@Composable
fun BottomBar(navController: NavController) {
    val currentDestination: Destination = navController.currentDestination()

    BottomNavigation {
        BottomBarDestination.entries.forEach { destination ->
            BottomNavigationItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.navigate(destination.direction) { launchSingleTop = true }
                },
                icon = {
                    Icon(
                        painterResource(id = destination.icon),
                        contentDescription = stringResource(destination.label))
                },
                label = { Text(stringResource(destination.label)) },
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavController.currentDestination() =
    appCurrentDestinationAsState().value ?: NavGraphs.root.startAppDestination
