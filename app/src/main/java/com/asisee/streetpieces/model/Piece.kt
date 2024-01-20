package com.asisee.streetpieces.model

import com.google.firebase.firestore.DocumentId

data class Piece(
    @DocumentId val id: String = "",
    val title: String = "",
    val dateTimeInEpochSeconds: Long = 0,
    val photoUri: String = "",
    val location: PieceLocation = PieceLocation(),
    val userId: String = "",
)
