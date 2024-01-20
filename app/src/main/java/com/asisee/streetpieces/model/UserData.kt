package com.asisee.streetpieces.model

import android.net.Uri
import com.google.firebase.firestore.DocumentId

data class UserData(
    @DocumentId val id: String = "",
    val userId: String = "",
    val name: String = "",
    val username: String = "",
    val bio: String = "",
    val photoUri: String = ""
)
