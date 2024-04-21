package com.asisee.streetpieces.screens.userpieces

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.Piece
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.composable.SpacerML
import com.asisee.streetpieces.common.composable.SpacerS
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.UserData

@Composable
fun UserPieces(
    userData: UserData,
    pieces: List<Piece>,
    popBack: () -> Unit,
    toUserProfile: () -> Unit,
    clickedPieceIndex: Int,
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(clickedPieceIndex) {
        lazyListState.scrollToItem(clickedPieceIndex)
    }
    Column(Modifier.fillMaxWidth()) {
        SpacerS()
        Row(Modifier.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(id = R.drawable.arrowbackward),
                stringResource(id = R.string.arrow_backward),
                Modifier
                    .padding(top = 4.dp)
                    .width(36.dp)
                    .clickable { popBack() })
            Text(
                text = userData.username,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 28.dp))
        }
        SpacerML()
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(pieces) { piece ->
                Piece(
                    piece = piece,
                    userData = userData,
                    toProfile = toUserProfile)
            }
        }
        SpacerM()
    }
}