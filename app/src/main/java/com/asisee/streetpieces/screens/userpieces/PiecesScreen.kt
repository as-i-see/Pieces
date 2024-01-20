package com.asisee.streetpieces.screens.userpieces

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.OWN_USER_ID
import com.asisee.streetpieces.PROFILE_SCREEN
import com.asisee.streetpieces.R
import com.asisee.streetpieces.USER_ID
import com.asisee.streetpieces.common.composable.PieceView
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.composable.SpacerML
import com.asisee.streetpieces.common.composable.SpacerS
import com.asisee.streetpieces.common.ext.epochSecondsToPieceDate
import com.asisee.streetpieces.common.ext.spacerS
import com.asisee.streetpieces.common.ext.spacerXS
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.PieceLocation
import com.asisee.streetpieces.model.UserData
import kotlinx.coroutines.Dispatchers
import com.asisee.streetpieces.R.string as AppText
import com.asisee.streetpieces.R.drawable as AppIcon

@Composable
fun UserPiecesScreen(popUp: () -> (Unit), open: (String) -> Unit, viewModel: UserPiecesViewModel = hiltViewModel()) {
    val pieces by viewModel.pieces.collectAsState(initial = emptyList(), context = Dispatchers.IO)
    val userData by viewModel.userData.collectAsState(initial = UserData(), context = Dispatchers.IO)
    val lazyListState = rememberLazyListState()
    Column(
        Modifier.fillMaxWidth()
    ) {
        SpacerS()
        Row(
            Modifier.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = AppIcon.arrowbackward),
                stringResource(id = AppText.arrow_backward),
                Modifier
                    .padding(top = 4.dp)
                    .width(36.dp)
                    .clickable {
                        viewModel.popUp(popUp)
                    }
            )
            Text(
                text = userData.username,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 28.dp)
            )
        }
        SpacerML()
        LazyColumn(modifier = Modifier.fillMaxWidth(), state = viewModel.lazyListState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(pieces) { piece ->
                PieceView(piece = piece, userData = userData, toProfile = { open("$PROFILE_SCREEN?$USER_ID={${userData.userId}}") })
            }
        }
        SpacerM()
    }
}

@Preview
@Composable
fun TopRowPreview() {
    Row(
        Modifier.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = AppIcon.arrowbackward),
            stringResource(id = AppText.arrow_backward),
            Modifier
                .padding(top = 4.dp)
                .width(36.dp)
        )
        Text(
            text = stringResource(id = AppText.explore),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 28.dp),
        )
    }
}

//@Preview
@Composable
fun PieceViewPreview() {
    val userData = UserData(
        username = "CAC.QTS",
        photoUri = "https://instasize.com/_next/image?url=https%3A%2F%2Fres.cloudinary.com%2Fmunkee%2Fimage%2Fupload%2Fw_1000%2Cc_fill%2Car_1%3A1%2Cg_auto%2Cr_max%2Fv1682630058%2Finstasize-website%2Flearn%2Fblonde-girl-sitting-down-pink-jacket.webp&w=828&q=75"
    )
    val piece = Piece(
        title = "Best kusok ever",
        dateTimeInEpochSeconds = 1699698285,
        photoUri = "https://graffitibible.com/wp-content/uploads/2022/11/Graffiti-piece-on-a-wall-final-picture.jpg",
        location = PieceLocation(50.450100, 30.523399)
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userData.photoUri)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.uploaded_avatar),
                placeholder = painterResource(id = AppIcon.ic_sign_in),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(30.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
            )
            Text(modifier = Modifier.padding(start = 8.dp), text = userData.username, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.spacerS())
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(piece.photoUri)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.piece),
            placeholder = painterResource(id = AppIcon.ic_sign_in),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.spacerS())
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = userData.username, fontWeight = FontWeight.SemiBold)
            Text(modifier = Modifier.padding(start = 8.dp), text = piece.title)
        }
        Spacer(modifier = Modifier.spacerXS())
        Text(modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.Light, fontSize = 10.sp, text = epochSecondsToPieceDate(piece.dateTimeInEpochSeconds))
    }
}

