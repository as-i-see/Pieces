package com.asisee.streetpieces.screens.searchfeed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.common.composable.SpacerS
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.screens.post.PostScreen
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Home
import compose.icons.evaicons.outline.Search
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText


data object SearchFeedTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(AppText.search)
            val icon = rememberVectorPainter(EvaIcons.Outline.Search)
            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<SearchFeedScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is SearchFeedScreenSideEffect.NavigateToPost -> {
                    navigator.push(PostScreen(sideEffect.postData))
                }
            }
        }
        View(
            state = state,
            toPost = screenModel::toPost
        )
    }

    @Composable
    fun View(
        state: SearchFeedScreenState,
        toPost: (PostData) -> Unit
    ) {
        when (state) {
            SearchFeedScreenState.Loading -> {
                ProgressIndicator()
            }
            is SearchFeedScreenState.SearchFeed -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SpacerS()
                    TextField(
                        value = "search",
                        onValueChange = {},
                        singleLine = true,
                        placeholder = { Text(stringResource(id = AppText.search)) },
                        leadingIcon = {
                            Icon(
                                imageVector = EvaIcons.Outline.Search,
                                contentDescription = stringResource(AppText.search_icon)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                    SpacerS()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(items = state.posts, key = { post -> post.id }) { post ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(post.pictureUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(AppText.piece),
                                placeholder = painterResource(id = R.drawable.photo_placeholder),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(RectangleShape)
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                                    .clickable(
                                        onClickLabel = stringResource(id = AppText.piece_onclick_label),
                                        role = Role.Image
                                    ) {
                                        toPost(post)
                                    },
                            )
                        }
                    }
                }
            }
        }

    }
}