package com.asisee.streetpieces

import androidx.compose.foundation.Image
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.asisee.streetpieces.model.service.AccountService

@Composable
fun BottomBar(navController: NavHostController, modifier: Modifier = Modifier) {
    val destinations = listOf(
        BottomBarDestination.SearchFeed, BottomBarDestination.Profile
    )
    NavigationBar(tonalElevation = 4.dp) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        destinations.forEach { screen ->
            NavigationBarItem(
                label = {
                    Text(text = stringResource(id = screen.title))
                },
                icon = {
                    Image(painter = painterResource(id = screen.icon), contentDescription = stringResource(id = screen.title))
                },
                selected = currentRoute == screen.route || currentRoute?.endsWith(OWN_USER_ID) == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
//                        launchSingleTop = true
                        restoreState = true

                    }
                }
            )
        }
    }
}

fun String.withoutParams() = substringBefore('?')