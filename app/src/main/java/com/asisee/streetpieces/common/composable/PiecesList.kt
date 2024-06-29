package com.asisee.streetpieces.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asisee.streetpieces.model.Post
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.UserPieces

@Composable
fun PiecesList(
    toolbarTitle: String,
    posts: List<Post>,
    shownPieceIndex: Int,
    popBack: () -> Unit,
    toUserProfile: (UserData) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(shownPieceIndex) {
        lazyListState.scrollToItem(shownPieceIndex)
    }
    Column(Modifier.fillMaxWidth()) {
        NavBackActionToolbar(navigateBack = popBack, title = toolbarTitle) {}
        SpacerML()
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(posts) { piece ->
                Post(
                    postData = piece.data,
                    userData = piece.author,
                    toProfile = toUserProfile
                )
            }
        }
        SpacerM()
    }
}

@Composable
fun ProfilePiecesList(
    userPieces: UserPieces,
    shownPieceIndex: Int,
    popBack: () -> Unit,
    toUserProfile: (UserData) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(shownPieceIndex) {
        lazyListState.scrollToItem(shownPieceIndex)
    }
    Column(Modifier.fillMaxWidth()) {
        NavBackActionToolbar(navigateBack = popBack, title = userPieces.userData.username) {}
        SpacerML()
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(userPieces.piecesData) { piece ->
                Post(
                    postData = piece,
                    userData = userPieces.userData,
                    toProfile = toUserProfile
                )
            }
        }
        SpacerM()
    }
}