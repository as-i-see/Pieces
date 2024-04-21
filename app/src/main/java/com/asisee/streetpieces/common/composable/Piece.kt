package com.asisee.streetpieces.common.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.ext.epochSecondsToPieceDate
import com.asisee.streetpieces.common.ext.spacerS
import com.asisee.streetpieces.common.ext.spacerXS
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.UserData

@Composable
fun Piece(piece: Piece, userData: UserData, toProfile: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier.fillMaxWidth().padding(start = 16.dp).clickable {
                    toProfile()
                },
            verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model =
                        ImageRequest.Builder(LocalContext.current)
                            .data(userData.photoUri)
                            .crossfade(true)
                            .build(),
                    contentDescription = stringResource(R.string.uploaded_avatar),
                    placeholder = painterResource(id = R.drawable.ic_sign_in),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(30.dp).aspectRatio(1f).clip(CircleShape),
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = userData.username,
                    fontWeight = FontWeight.SemiBold)
            }
        Spacer(modifier = Modifier.spacerS())
        AsyncImage(
            model =
                ImageRequest.Builder(LocalContext.current)
                    .data(piece.photoUri)
                    .crossfade(true)
                    .build(),
            contentDescription = stringResource(R.string.piece),
            placeholder = painterResource(id = R.drawable.ic_sign_in),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.spacerS())
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
                Text(text = userData.username, fontWeight = FontWeight.SemiBold)
                Text(modifier = Modifier.padding(start = 8.dp), text = piece.title)
            }
        Spacer(modifier = Modifier.spacerXS())
        Text(
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = FontWeight.Light,
            fontSize = 10.sp,
            text = epochSecondsToPieceDate(piece.dateTimeInEpochSeconds))
    }
}
