package com.asisee.streetpieces.model

import DataPreview
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
@DataPreview
data class PostData(
    @DocumentId val id: String = "",
    val title: String = "",
    val epochSecondsCreatedAt: Long = 0,
    val pictureUrl: String = "",
    val location: PieceLocation? = null,
    val authorId: String = "",
) : Parcelable
