package com.asisee.streetpieces.screens.userpieces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.asisee.streetpieces.model.UserData
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers

@Destination(navArgsDelegate = UserPiecesScreenNavArgs::class)
@Composable
fun UserPiecesScreen(
    navigator: DestinationsNavigator,
    viewModel: UserPiecesViewModel = hiltViewModel()
) {
    val pieces by viewModel.pieces.collectAsState(initial = emptyList(), context = Dispatchers.IO)
    val userData by
        viewModel.userData.collectAsState(initial = UserData(), context = Dispatchers.IO)
    UserPieces(
        userData = userData,
        pieces = ,
        popBack = { /*TODO*/ },
        toUserProfile = { /*TODO*/ },
        clickedPieceIndex =
    )
}