package com.asisee.streetpieces.screens.pieces_list.profile

import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData

data class ProfilePiecesListState (
    val userData: UserData,
    val pieces: List<PostData>,
)
