package com.asisee.streetpieces.screens.posts_list

import com.asisee.streetpieces.model.UserData

sealed interface PostsListSideEffect {
    data object PopBack : PostsListSideEffect
    data class NavigateToUserProfile(val userData: UserData) : PostsListSideEffect
    data class NavigateToOwnProfile(val userData: UserData) : PostsListSideEffect
}