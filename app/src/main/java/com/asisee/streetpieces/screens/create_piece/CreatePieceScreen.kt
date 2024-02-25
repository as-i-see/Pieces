package com.asisee.streetpieces.screens.create_piece

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.common.composable.ActionToolbar
import com.asisee.streetpieces.common.composable.BasicField
import com.asisee.streetpieces.common.ext.alertDialog
import com.asisee.streetpieces.common.ext.permissionButton
import com.asisee.streetpieces.common.ext.row
import com.asisee.streetpieces.common.ext.spacerM
import com.asisee.streetpieces.common.ext.spacerXXL
import com.asisee.streetpieces.screens.destinations.SearchFeedScreenDestination
import com.asisee.streetpieces.theme.BrightOrange
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.asisee.streetpieces.R.drawable as AppIcon
import com.asisee.streetpieces.R.string as AppText

@Destination(navArgsDelegate = CreatePieceScreenNavArgs::class)
@Composable
fun CreatePieceScreen(
    navigator: DestinationsNavigator,
    viewModel: CreatePieceViewModel = hiltViewModel()
) {
    val piece by viewModel.piece
    var showWarningDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().verticalScroll(rememberScrollState())) {
            if (showWarningDialog) {
                AlertDialog(
                    modifier = Modifier.alertDialog(),
                    title = { Text(stringResource(id = AppText.missing_details_title)) },
                    text = { Text(stringResource(id = AppText.missing_details_text)) },
                    confirmButton = {
                        TextButton(
                            onClick = { showWarningDialog = false },
                            modifier = Modifier.permissionButton(),
                            colors =
                                ButtonDefaults.buttonColors(
                                    backgroundColor = BrightOrange, contentColor = Color.White)) {
                                Text(text = stringResource(AppText.ok))
                            }
                    },
                    onDismissRequest = { showWarningDialog = false })
            }

            ActionToolbar(
                title = AppText.edit_piece, endActionIcon = AppIcon.ic_check, modifier = Modifier) {
                    if (piece.title.isBlank() || piece.location.isNotSet()) {
                        showWarningDialog = true
                    } else viewModel.onDoneClick { navigator.navigate(SearchFeedScreenDestination) }
                }

            Spacer(modifier = Modifier.spacerM())

            Row(
                Modifier.row(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround) {
                    BasicField(
                        AppText.title,
                        piece.title,
                        { viewModel.onTitleChange(it) },
                        Modifier.fillMaxWidth(0.7f))
                    AsyncImage(
                        model =
                            ImageRequest.Builder(LocalContext.current)
                                .data(piece.photoUri)
                                .crossfade(true)
                                .build(),
                        contentDescription = stringResource(AppText.preview),
                        placeholder = painterResource(id = AppIcon.photo_placeholder),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.clip(RectangleShape).width(100.dp).height(100.dp),
                    )
                }

            Spacer(modifier = Modifier.spacerXXL())

            if (piece.location.isNotSet())
                Text(
                    text = stringResource(AppText.missing_location_title),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize())
            else {
                LaunchedEffect(true) { viewModel.onLocationFetched() }
                Text(
                    text = piece.location.text(),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize())
            }

            LaunchedEffect(viewModel.locationFetchTick) { viewModel.updateLocation() }
        }
}
