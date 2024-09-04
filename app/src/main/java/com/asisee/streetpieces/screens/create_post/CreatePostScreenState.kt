package com.asisee.streetpieces.screens.create_post

import com.asisee.streetpieces.common.exceptions.LocationResultException
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.PieceLocation
import com.asisee.streetpieces.screens.sign_in.SignInScreenState.SignIn

sealed interface CreatePostScreenState {
    data object Loading: CreatePostScreenState

    data class CreatePost(
        val postData: PostData = PostData(),
        val pieceLocation: PieceLocationState = PieceLocationState.NotUsed
    ) : CreatePostScreenState

    sealed interface PieceLocationState {
        data object NotUsed: PieceLocationState
        data object Loading: PieceLocationState

        data class Error(val exception: LocationResultException): PieceLocationState

        data class Used(val pieceLocation: PieceLocation) : PieceLocationState
    }

    fun fill(title: String) = (this as? CreatePost)?.copy(postData = postData.copy(title = title)) ?: this
}

