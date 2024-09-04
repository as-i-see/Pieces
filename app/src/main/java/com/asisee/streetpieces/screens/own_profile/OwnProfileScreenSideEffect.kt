package com.asisee.streetpieces.screens.own_profile

import com.asisee.streetpieces.model.UserPosts

sealed interface OwnProfileScreenSideEffect {

    data object NavigateBack: OwnProfileScreenSideEffect

    data object NavigateToSettings: OwnProfileScreenSideEffect

    data class NavigateToPiece(val userPosts: UserPosts, val clickedPostIndex: Int): OwnProfileScreenSideEffect

    data object NavigateToPostCreation: OwnProfileScreenSideEffect
}