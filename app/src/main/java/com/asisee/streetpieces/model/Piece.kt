package com.asisee.streetpieces.model

import DataPreview
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
@DataPreview
data class Piece(
    @DocumentId val id: String = "",
    val title: String = "",
    val dateTimeInEpochSeconds: Long = 0,
    val photoUri: String = "",
    val location: PieceLocation? = null,
    val userId: String = "",
) : Parcelable
