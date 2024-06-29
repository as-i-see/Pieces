package com.asisee.streetpieces.screens.create_piece

import com.asisee.streetpieces.common.exceptions.LocationResultException
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.PieceLocation

sealed interface CreatePieceScreenState {
    data object Loading: CreatePieceScreenState

    data class CreatePiece(
        val postData: PostData = PostData(),
        val pieceLocation: PieceLocationState = PieceLocationState.NotUsed
    ) : CreatePieceScreenState

    sealed interface PieceLocationState {
        data object NotUsed: PieceLocationState
        data object Loading: PieceLocationState

        data class Error(val exception: LocationResultException): PieceLocationState

        data class Used(val pieceLocation: PieceLocation) : PieceLocationState
    }
}

