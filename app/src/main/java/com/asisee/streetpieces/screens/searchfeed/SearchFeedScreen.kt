package com.asisee.streetpieces.screens.searchfeed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.ext.spacerS
import com.asisee.streetpieces.screens.destinations.PieceScreenDestination
import com.asisee.streetpieces.theme.PlaceholderGray
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import com.asisee.streetpieces.R.drawable as AppIcon
import com.asisee.streetpieces.R.string as AppText

@Destination
@Composable
fun SearchFeedScreen(
    navigator: DestinationsNavigator,
    viewModel: SearchFeedViewModel = hiltViewModel()
) {
    val latestPieces by
        viewModel.pieces.collectAsState(initial = emptyList(), context = Dispatchers.IO)
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.spacerS())
            TextField(
                value = "search",
                onValueChange = {},
                singleLine = true,
                placeholder = { Text(stringResource(id = AppText.search)) },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = AppIcon.ic_search),
                        contentDescription = stringResource(id = AppText.search_icon))
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(0.9f),
                colors =
                    TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        placeholderColor = PlaceholderGray,
                        backgroundColor = Color.Gray,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent))
            Spacer(modifier = Modifier.spacerS())
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    items(items = latestPieces, key = { piece -> piece.id }) { piece ->
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
                                    .clickable(
                                        onClickLabel =
                                            stringResource(id = AppText.piece_onclick_label),
                                        role = Role.Image) {
                                            navigator.navigate(
                                                PieceScreenDestination(piece.id, piece.userId))
                                        },
                        )
                    }
                }
        }
}

@Composable
@Preview
fun SearchFeedScreenPreview() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.spacerS())
            TextField(
                value = "search",
                onValueChange = {},
                singleLine = true,
                placeholder = { Text(stringResource(id = AppText.search)) },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = AppIcon.ic_search),
                        contentDescription = stringResource(id = AppText.search_icon))
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(0.9f),
                colors =
                    TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        placeholderColor = PlaceholderGray,
                        backgroundColor = Color.Gray,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent))
            Spacer(modifier = Modifier.spacerS())
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
