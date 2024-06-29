package com.asisee.streetpieces.screens.post

import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData

sealed interface PostScreenState {
    data object Loading: PostScreenState
    data class Post (
        val author: UserData,
        val post: PostData,
    ) : PostScreenState
}
