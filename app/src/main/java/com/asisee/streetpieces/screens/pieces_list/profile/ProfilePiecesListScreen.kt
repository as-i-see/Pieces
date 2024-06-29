package com.asisee.streetpieces.screens.pieces_list.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.asisee.streetpieces.common.composable.PiecesList
import com.asisee.streetpieces.common.composable.ProfilePiecesList
import com.asisee.streetpieces.model.UserPieces
import com.asisee.streetpieces.screens.pieces_list.PiecesListSideEffect
import com.asisee.streetpieces.screens.user_profile.UserProfileScreen
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
class ProfilePiecesListScreen(private val userPieces: UserPieces, private val clickedPieceIndex: Int = 0) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<ProfilePiecesListScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when(sideEffect) {
                is PiecesListSideEffect.PopBack -> {
                    navigator.pop()
                }
                is PiecesListSideEffect.NavigateToUserProfile -> {
                    navigator.push(UserProfileScreen(sideEffect.userData))
                }
            }
        }
        ProfilePiecesList(
            userPieces = userPieces,
            shownPieceIndex = clickedPieceIndex,
            popBack = screenModel::popBack,
            toUserProfile = screenModel::toUserProfile
        )
    }
}