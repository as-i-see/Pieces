package com.asisee.streetpieces.screens.create_piece

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.BasicField
import com.asisee.streetpieces.common.composable.NavBackActionToolbar
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.composable.SpacerS
import com.asisee.streetpieces.common.exceptions.LocationResultException
import com.asisee.streetpieces.common.ext.row
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Checkmark
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText
class CreatePieceScreen(val photoUri: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<CreatePieceScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when(sideEffect) {
                CreatePieceSideEffect.NavigateFurther -> {

                }
                CreatePieceSideEffect.UploadError -> {

                }
                CreatePieceSideEffect.MissingTitleError -> {

                }
                CreatePieceSideEffect.LocationStillLoadingError -> {

                }
                CreatePieceSideEffect.RequestLocationPermission -> {

                }
                CreatePieceSideEffect.NavBack -> {

                }
            }
        }
        View(
            state = state,
            onNavBack = screenModel::navBack,
            onDoneClick = screenModel::onDoneClick,
            onTitleChange = screenModel::onTitleChange,
            onFetchLocationClick = screenModel::fetchLocation,
            onRequestLocationPermissionClick = screenModel::requestLocationPermission
        )
    }

    @Composable
    fun View(
        state: CreatePieceScreenState,
        onNavBack: () -> Unit,
        onDoneClick: () -> Unit,
        onTitleChange: (String) -> Unit,
        onFetchLocationClick: () -> Unit,
        onRequestLocationPermissionClick: () -> Unit,
    ) {
        when (state) {
            is CreatePieceScreenState.Loading -> {
                ProgressIndicator()
            }
            is CreatePieceScreenState.CreatePiece -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                )
                {
                    NavBackActionToolbar(navigateBack = onNavBack, title = stringResource(id = AppText.new_post)) {
                        IconButton(onClick = onDoneClick) {
                            Icon(imageVector = EvaIcons.Fill.Checkmark, contentDescription = stringResource(id = AppText.upload))
                        }
                    }
                    SpacerM()
                    Row(
                        Modifier.row(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    )
                    {
                        BasicField(
                            R.string.title,
                            state.postData.title,
                            { newValue -> onTitleChange(newValue) },
                            Modifier.fillMaxWidth(0.7f))
                        AsyncImage(
                            model =
                            ImageRequest.Builder(LocalContext.current)
                                .data(state.postData.pictureUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = stringResource(R.string.preview),
                            placeholder = painterResource(id = R.drawable.photo_placeholder),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .clip(RectangleShape)
                                .width(100.dp)
                                .height(100.dp),
                        )
                    }
                    SpacerS()
                    Divider()
                    SpacerM()
                    LocationHandler(
                        locationState = state.pieceLocation,
                        onFetchLocationClick = onFetchLocationClick,
                        onRequestLocationPermissionClick = onRequestLocationPermissionClick,
                    )
                }
            }
        }
    }

    @Composable
    fun LocationHandler(
        locationState: CreatePieceScreenState.PieceLocationState,
        onFetchLocationClick: () -> Unit,
        onRequestLocationPermissionClick: () -> Unit,
    ) {
        Row(
            modifier = Modifier.row(),
            horizontalArrangement = Arrangement.Center,
        ) {
            when (locationState) {
                is CreatePieceScreenState.PieceLocationState.Loading -> {
                    ProgressIndicator()
                }
                is CreatePieceScreenState.PieceLocationState.Used -> {
                    Text(text = locationState.pieceLocation.toString())
                }
                is CreatePieceScreenState.PieceLocationState.NotUsed -> {
                    Button(
                        onClick = { onFetchLocationClick() },
                    ) {
                        Text(text = stringResource(id = AppText.set_location))
                    }
                }
                is CreatePieceScreenState.PieceLocationState.Error -> {
                    when (locationState.exception) {
                        LocationResultException.PermissionRequestFlow -> {
                            Button(
                                onClick = { onRequestLocationPermissionClick() },
                            ) {
                                Text(text = stringResource(id = AppText.grant_location_permission))
                            }
                        }
                        LocationResultException.LocationFetchException -> {
                            Button(
                                onClick = { onFetchLocationClick() },
                            ) {
                                Text(text = stringResource(id = AppText.error_try_again))
                            }
                        }
                    }
                }
            }
        }
    }
}


