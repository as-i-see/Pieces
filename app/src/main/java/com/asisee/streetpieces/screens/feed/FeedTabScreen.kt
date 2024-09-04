package com.asisee.streetpieces.screens.feed

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.asisee.streetpieces.common.composable.CenteredColumn
import com.asisee.streetpieces.common.composable.FeedPostsList
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.screens.user_profile.UserProfileScreen
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText

class FeedTabScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<FeedScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is FeedScreenSideEffect.NavigateToUserProfile -> {
                    navigator.push(UserProfileScreen(sideEffect.userData))
                }
            }
        }
        View(
            state,
            screenModel::navigateToUserProfile
        )
    }
    @Composable
    fun View(
        state: FeedScreenState,
        toProfile: (UserData) -> Unit,
    ) {
        when(state) {
            is FeedScreenState.Loading -> {
                ProgressIndicator()
            }
            is FeedScreenState.Feed -> {
                if (state.posts.isNotEmpty())
                    FeedPostsList(posts = state.posts, toUserProfile = toProfile)
                else CenteredColumn {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        text = stringResource(id = AppText.empty_feed),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}