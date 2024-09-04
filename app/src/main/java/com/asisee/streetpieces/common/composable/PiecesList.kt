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
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.UserPost
import com.asisee.streetpieces.model.UserPosts

@Composable
fun FeedPostsList(
    posts: List<UserPost>,
    toUserProfile: (UserData) -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        SpacerS()
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(posts) { userPost ->
                Post(
                    postData = userPost.postData,
                    userData = userPost.userData,
                    toProfile = toUserProfile
                )
                SpacerS()
            }
        }
    }
}

@Composable
fun ProfilePiecesList(
    userPosts: UserPosts,
    shownPieceIndex: Int,
    popBack: () -> Unit,
    toUserProfile: (UserData) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(shownPieceIndex) {
        lazyListState.scrollToItem(shownPieceIndex)
    }
    Column(Modifier.fillMaxWidth()) {
        NavBackActionToolbar(navigateBack = popBack, title = userPosts.userData.username) {}
        SpacerML()
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(userPosts.piecesData) { piece ->
                Post(
                    postData = piece,
                    userData = userPosts.userData,
                    toProfile = toUserProfile
                )
            }
        }
        SpacerM()
    }
}