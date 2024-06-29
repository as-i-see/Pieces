package com.asisee.streetpieces.screens.own_profile

import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData

sealed interface OwnProfileScreenSideEffect {
    data object NavigateBack: OwnProfileScreenSideEffect

    data object NavigateToSettings: OwnProfileScreenSideEffect
    data class NavigateToPiece(val userData: UserData, val postData: PostData): OwnProfileScreenSideEffect
}