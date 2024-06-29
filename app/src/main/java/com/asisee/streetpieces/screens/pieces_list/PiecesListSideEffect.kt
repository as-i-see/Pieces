package com.asisee.streetpieces.screens.pieces_list

import com.asisee.streetpieces.model.UserData

sealed interface PiecesListSideEffect {
    data class ScrollToClickedPiece(val indexInList: Int)
    data object PopBack : PiecesListSideEffect
    data class NavigateToUserProfile(val userData: UserData) : PiecesListSideEffect
}