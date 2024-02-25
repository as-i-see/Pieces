package com.asisee.streetpieces.model

import com.google.firebase.firestore.DocumentId

data class Subscription(
    @DocumentId val id: String = "",
    val userId: String = "",
    val subUserId: String = ""
)
