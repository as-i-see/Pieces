package com.asisee.streetpieces.model

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val username: String = "",
    val bio: String = "",
    val profilePictureUrl: String = ""
)
