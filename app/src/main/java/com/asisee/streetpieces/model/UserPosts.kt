package com.asisee.streetpieces.model

data class UserPosts(
    val userData: UserData,
    val piecesData: List<PostData>
)
