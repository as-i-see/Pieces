package com.asisee.streetpieces.screens.own_profile

import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData

data class OwnProfileScreenState (
    val userData: UserData = UserData(),
    val numberOfFollowers: Long = 0,
    val numberOfFollowees: Long = 0,
    val posts: List<PostData> = emptyList()
)