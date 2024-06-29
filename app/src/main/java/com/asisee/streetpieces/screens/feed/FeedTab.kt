package com.asisee.streetpieces.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.asisee.streetpieces.common.composable.Post
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.model.UserData
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Home
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText
data object FeedTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(AppText.feed)
            val icon = rememberVectorPainter(EvaIcons.Outline.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<FeedScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is FeedScreenSideEffect.NavigateToUserProfile -> {
//                    navigator.push()
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
                val lazyListState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = lazyListState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.posts) { piece ->
                        Post(
                            postData = piece.data,
                            userData = piece.author,
                            toProfile = {
                                toProfile(piece.author)
                            },
                        )
                    }
                }
            }
        }
    }
}