package com.asisee.streetpieces.screens.post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.asisee.streetpieces.common.composable.Post
import com.asisee.streetpieces.common.composable.ProfilePiecesList
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.UserPieces
import com.asisee.streetpieces.screens.pieces_list.PiecesListSideEffect
import com.asisee.streetpieces.screens.user_profile.UserProfileScreen
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
class PostScreen(private val postData: PostData) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<PostScreenModel>(parameters = { parametersOf(postData) })
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when(sideEffect) {
                is PostScreenSideEffect.PopBack -> {
                    navigator.pop()
                }
                is PostScreenSideEffect.NavigateToUserProfile -> {
                    navigator.push(UserProfileScreen(sideEffect.userData))
                }
            }
        }
        View(
            state = state,
            toProfile = screenModel::toUserProfile
        )
    }
    @Composable
    private fun View(
        state: PostScreenState,
        toProfile: (UserData) -> Unit
    ) {
        when(state) {
            PostScreenState.Loading -> {
                ProgressIndicator()
            }
            is PostScreenState.Post -> {
                Post(
                    postData = state.post,
                    userData = state.author,
                    toProfile = toProfile
                )
            }
        }
    }
}