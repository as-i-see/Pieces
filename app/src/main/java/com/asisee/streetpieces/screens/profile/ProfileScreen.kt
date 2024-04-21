package com.asisee.streetpieces.screens.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.ActionToolbar
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.composable.SpacerS
import com.asisee.streetpieces.common.ext.spacerM
import com.asisee.streetpieces.common.ext.spacerS
import com.asisee.streetpieces.common.ext.toolbarActions
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.screens.destinations.SettingsScreenDestination
import com.asisee.streetpieces.screens.destinations.SplashScreenDestination
import com.asisee.streetpieces.screens.destinations.UserPiecesScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import com.asisee.streetpieces.R.string as AppText

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Destination(navArgsDelegate = ProfileScreenNavArgs::class)
@Composable
fun ProfileScreen(navigator: DestinationsNavigator, viewModel: ProfileViewModel = hiltViewModel()) {
    val userData by
        viewModel.userData.collectAsState(initial = UserData(), context = Dispatchers.IO)
    val pieces by viewModel.pieces.collectAsState(initial = emptyList(), context = Dispatchers.IO)
    val subscriptionId by viewModel.subscriptionId
    val nSubscribers by viewModel.nSubscribers
    val nSubscriptions by viewModel.nSubscriptions
    LaunchedEffect(key1 = true) {
        viewModel.checkAnonymousUser { navigator.popBackStack(SplashScreenDestination, true) }
    }
    androidx.compose.material3.Scaffold(floatingActionButton = {}) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            ActionToolbar(
                title = AppText.pieces,
                modifier = Modifier.toolbarActions(),
                endActionIcon = R.drawable.ic_settings,
                endAction = { navigator.navigate(SettingsScreenDestination) })
            Spacer(modifier = Modifier.spacerM())
            Text(
                text = userData.username,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 16.dp))
            Spacer(modifier = Modifier.spacerS())
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                    if (userData.photoUri.isNotEmpty()) {
                        AsyncImage(
                            model =
                                ImageRequest.Builder(LocalContext.current)
                                    .data(userData.photoUri)
                                    .crossfade(true)
                                    .build(),
                            contentDescription = stringResource(AppText.upload_avatar),
                            placeholder = painterResource(id = R.drawable.ic_sign_in),
                            contentScale = ContentScale.Crop,
                            modifier =
                                Modifier.padding(start = 16.dp)
                                    .fillMaxWidth(0.3f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape),
                        )
                    } else {
                        Image(
                            painterResource(id = R.drawable.ic_avatar),
                            stringResource(AppText.uploaded_avatar),
                            modifier =
                                Modifier.padding(start = 16.dp)
                                    .fillMaxWidth(0.3f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape))
                    }
                    Row(
                        Modifier.fillMaxWidth().padding(start = 32.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = pieces.size.toString(),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp)
                                Text(text = "Posts")
                            }
                            nSubscribers?.let { nSubscribers ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = nSubscribers.toString(),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp)
                                    Text(text = "Followers")
                                }
                            }
                            nSubscriptions?.let { nSubscriptions ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = nSubscriptions.toString(),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp)
                                    Text(text = "Following")
                                }
                            }
                        }
                }

            Spacer(modifier = Modifier.spacerS())
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
            SpacerS()
            if (subscriptionId == null) {
                Button(
                    onClick = { viewModel.follow() },
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(2.dp, 8.dp, 0.dp, 4.dp, 4.dp),
                    modifier = Modifier.padding(start = 16.dp).height(30.dp).width(100.dp)) {
                        Text(
                            text = "Follow", fontWeight = FontWeight.Bold, letterSpacing = (0.3).sp)
                    }
            } else {
                Button(
                    onClick = { viewModel.unfollow() },
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(2.dp, 8.dp, 0.dp, 4.dp, 4.dp),
                    modifier = Modifier.padding(start = 16.dp).height(30.dp).width(100.dp)) {
                        Text(
                            text = "Unfollow",
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (0.3).sp)
                    }
            }
            SpacerM()
            Divider()
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    items(items = pieces, key = { piece -> piece.id }) { piece ->
                        AsyncImage(
                            model =
                                ImageRequest.Builder(LocalContext.current)
                                    .data(piece.photoUri)
                                    .crossfade(true)
                                    .build(),
                            contentDescription = stringResource(AppText.piece),
                            placeholder = painterResource(id = R.drawable.photo_placeholder),
                            contentScale = ContentScale.Crop,
                            modifier =
                                Modifier.clip(RectangleShape)
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                                    .clickable {
                                        navigator.navigate(
                                            UserPiecesScreenDestination(userData.userId, piece.id))
                                    },
                        )
                    }
                }
        }
    }
}

// @Preview
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreenPreview(modifier: Modifier = Modifier) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = modifier.padding(16.dp)) {
                    Icon(Icons.Filled.Add, stringResource(id = AppText.cd_add))
                }
        }) {
            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                ActionToolbar(
                    title = AppText.pieces,
                    modifier = Modifier.toolbarActions(),
                    endActionIcon = R.drawable.ic_settings,
                    endAction = {})
                Spacer(modifier = Modifier.spacerM())
                Text(
                    text = "XOXO PERV",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painterResource(id = R.drawable.photo_placeholder),
                            "",
                            modifier =
                                Modifier.clip(CircleShape)
                                    .fillMaxWidth(0.3f)
                                    .aspectRatio(1f)
                                    .padding(start = 16.dp))
                        Row(
                            Modifier.fillMaxWidth().padding(start = 32.dp, end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "12",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp)
                                    Text(text = "Posts")
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "120",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp)
                                    Text(text = "Followers")
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "69",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp)
                                    Text(text = "Following")
                                }
                            }
                    }

                Text(
                    text = "Yevhenii Karlov",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 16.dp))
                Text(
                    text = "Sex Giant",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                SpacerS()
                Button(
                    onClick = {},
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(2.dp, 8.dp, 0.dp, 4.dp, 4.dp),
                    modifier = Modifier.padding(start = 16.dp).height(30.dp).width(100.dp)) {
                        Text(
                            text = "Follow", fontWeight = FontWeight.Bold, letterSpacing = (0.3).sp)
                    }
                SpacerM()
                Divider()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(6) {
                            item { Image(painterResource(id = R.drawable.photo_placeholder), "") }
                        }
                    }
            }
        }
}