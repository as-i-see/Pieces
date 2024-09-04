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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.NavBackActionToolbar
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.composable.SpacerS
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.UserPosts
import com.asisee.streetpieces.screens.posts_list.profile.ProfilePostsListScreen
import com.asisee.streetpieces.screens.settings.SettingsScreen
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.outline.Settings
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

data class OwnProfileScreen(val userData: UserData) : Screen {
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
                    navigator.push(SettingsScreen())
                }
                is OwnProfileScreenSideEffect.NavigateToPiece -> {
                    navigator.push(ProfilePostsListScreen(sideEffect.userPosts, sideEffect.clickedPostIndex))
                }
                is OwnProfileScreenSideEffect.NavigateToPostCreation -> {

                }
            }
        }
        View(
            state = state,
            navigateBack = screenModel::navigateBack,
            navigateToSettings = screenModel::navigateToSettings,
            navigateToPiece = screenModel::navigateToPiece,
        )
    }

    @Composable
    fun View(
        state: OwnProfileScreenState,
        navigateBack: () -> Unit,
        navigateToSettings: () -> Unit,
        navigateToPiece: (UserPosts, Int) -> Unit
    ) {
        Column(Modifier.fillMaxSize()) {
            NavBackActionToolbar(navigateBack = navigateBack, title = state.userData.username) {
                IconButton(onClick = navigateToSettings) {
                    Icon(
                        imageVector = EvaIcons.Outline.Settings,
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            }
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
                            .fillMaxWidth(0.25f)
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
                        Text(
                            text = "Posts",
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.numberOfFollowers.toString(),
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp)
                        Text(
                            text = "Followers",
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.numberOfFollowees.toString(),
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp)
                        Text(
                            text = "Following",
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            SpacerS()
            if (userData.name.isNotEmpty()) {
                Text(
                    text = userData.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp))
            }
            if (userData.bio.isNotEmpty()) {
                Text(
                    text = userData.bio,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp))
            }
            SpacerM()
            Divider()
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)) {
                itemsIndexed(items = state.posts, key = { index, piece -> piece.id }) { index, piece ->
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
                                navigateToPiece(UserPosts(state.userData, state.posts), index)
                            },
                    )
                }
            }
        }
    }
}