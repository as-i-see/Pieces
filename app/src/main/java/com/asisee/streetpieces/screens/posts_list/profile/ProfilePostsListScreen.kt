package com.asisee.streetpieces.screens.posts_list.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.asisee.streetpieces.common.composable.ProfilePiecesList
import com.asisee.streetpieces.model.UserPosts
import com.asisee.streetpieces.screens.own_profile.OwnProfileScreen
import com.asisee.streetpieces.screens.posts_list.PostsListSideEffect
import com.asisee.streetpieces.screens.user_profile.UserProfileScreen
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
class ProfilePostsListScreen(private val userPosts: UserPosts, private val clickedPieceIndex: Int = 0) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<ProfilePostsListScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when(sideEffect) {
                is PostsListSideEffect.PopBack -> {
                    navigator.pop()
                }
                is PostsListSideEffect.NavigateToUserProfile -> {
                    navigator.push(UserProfileScreen(sideEffect.userData))
                }
                is PostsListSideEffect.NavigateToOwnProfile -> {
                    navigator.push(OwnProfileScreen(sideEffect.userData))
                }
            }
        }
        ProfilePiecesList(
            userPosts = userPosts,
            shownPieceIndex = clickedPieceIndex,
            popBack = screenModel::popBack,
            toUserProfile = screenModel::toUserProfile
        )
    }
}