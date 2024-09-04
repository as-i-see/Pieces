package com.asisee.streetpieces.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.asisee.streetpieces.common.composable.TabNavigationItem
import com.asisee.streetpieces.screens.feed.FeedTab
import com.asisee.streetpieces.screens.own_profile.OwnProfileTab
import com.asisee.streetpieces.screens.searchfeed.SearchFeedTab

class MainScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(tab = FeedTab) {
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        TabNavigationItem(FeedTab)
                        TabNavigationItem(SearchFeedTab)
                        TabNavigationItem(OwnProfileTab)
                    }

                }
            ) { innerPaddingModifier ->
                Box(
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    CurrentTab()
                }
            }
        }
    }
}