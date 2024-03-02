package com.asisee.streetpieces.screens.create_piece

import android.os.Parcelable
import com.asisee.streetpieces.model.Piece
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreatePieceState(
    val piece: Piece = Piece(),
    val usingLocation: Boolean = false,
    val showLoader: Boolean = false,
) : Parcelable