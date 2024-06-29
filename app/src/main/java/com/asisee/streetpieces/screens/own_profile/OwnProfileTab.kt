package com.asisee.streetpieces.screens.own_profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.composable.SpacerS
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.Settings
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText

data object OwnProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(AppText.profile)
            val icon = rememberVectorPainter(EvaIcons.Outline.Person)
            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<OwnProfileScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when(sideEffect) {
                is OwnProfileScreenSideEffect.NavigateBack -> {
                    navigator.pop()
                }
                is OwnProfileScreenSideEffect.NavigateToSettings -> {
//                    navigator.push(...)
                }
                is OwnProfileScreenSideEffect.NavigateToPiece -> {
//                    navigator.push()
                }
            }
        }
        View(
            state = state,
            navigateToSettings = screenModel::navigateToSettings,
            navigateToPiece = screenModel::navigateToPiece,
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun View(
        state: OwnProfileScreenState,
        navigateToSettings: () -> Unit,
        navigateToPiece: (UserData, PostData) -> Unit
    ) {
        when (state) {
            is OwnProfileScreenState.Loading -> {
                ProgressIndicator()
            }
            is OwnProfileScreenState.OwnProfile -> {
                Column(Modifier.fillMaxSize()) {
                    TopAppBar(
                        title = {
                            Text (
                                text = state.userData.username,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                            )
                        },
                        actions = {
                            IconButton(onClick = navigateToSettings) {
                                Icon(
                                    imageVector = EvaIcons.Outline.Settings,
                                    contentDescription = stringResource(R.string.settings)
                                )
                            }
                        })
                    SpacerM()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.userData.profilePictureUrl.isNotEmpty()) {
                            AsyncImage(
                                model =
                                ImageRequest.Builder(LocalContext.current)
                                    .data(state.userData.profilePictureUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.upload_avatar),
                                placeholder = painterResource(id = R.drawable.ic_sign_in),
                                contentScale = ContentScale.Crop,
                                modifier =
                                Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxWidth(0.3f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),
                            )
                        } else {
                            Icon(
                                imageVector = EvaIcons.Fill.Person,
                                contentDescription = stringResource(R.string.uploaded_avatar),
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxWidth(0.3f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp, end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = state.posts.size.toString(),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp)
                                Text(text = "Posts")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = state.numberOfFollowers.toString(),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp)
                                Text(text = "Followers")
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = state.numberOfFollowees.toString(),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp)
                                Text(text = "Following")
                            }
                        }
                    }
                    SpacerS()
                    if (state.userData.name.isNotEmpty()) {
                        Text(
                            text = state.userData.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 16.dp))
                    }
                    if (state.userData.bio.isNotEmpty()) {
                        Text(
                            text = state.userData.bio,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                    }
                    SpacerM()
                    Divider()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        items(items = state.posts, key = { piece -> piece.id }) { piece ->
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(piece.pictureUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.piece),
                                placeholder = painterResource(id = R.drawable.photo_placeholder_svg),
                                contentScale = ContentScale.Crop,
                                modifier =
                                Modifier
                                    .clip(RectangleShape)
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                                    .clickable {
                                        navigateToPiece(state.userData, piece)
                                    },
                            )
                        }
                    }
                }
            }
        }
    }
}