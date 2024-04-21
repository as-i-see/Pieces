package com.asisee.streetpieces.screens.userpieces

import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.UserData

data class UserPiecesState(
    val userData: UserData,
    val pieces: List<Piece>,
    val clickedPieceIndex: Int,
)
