package com.asisee.streetpieces.screens.posts_list.profile

import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData

data class ProfilePostsListState (
    val userData: UserData,
    val pieces: List<PostData>,
)