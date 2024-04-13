package com.asisee.streetpieces.screens.create_piece

import com.asisee.streetpieces.common.exceptions.LocationResultException
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.PieceLocation
import com.github.michaelbull.result.Result

data class CreatePieceState(
    val piece: Piece = Piece(),
    val locationResult: Result<PieceLocation, LocationResultException>? = null,
    val locationIsLoading : Boolean = false,
    val pieceIsUploading: Boolean = false,
)