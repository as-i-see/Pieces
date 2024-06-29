package com.asisee.streetpieces.screens.pieces_list.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.PostService
import com.asisee.streetpieces.model.service.UserDataService
import com.asisee.streetpieces.screens.pieces_list.PiecesListSideEffect
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

@Factory
class ProfilePiecesListScreenModel(
    private val postService: PostService,
    private val userDataService: UserDataService,
) : ScreenModel, ContainerHost<ProfilePiecesListState, PiecesListSideEffect> {

    override val container = screenModelScope.container<ProfilePiecesListState, PiecesListSideEffect>(
        ProfilePiecesListState(UserData(), emptyList())
    )

    fun popBack() = intent {
        postSideEffect(PiecesListSideEffect.PopBack)
    }

    fun toUserProfile(userData: UserData) = intent {
        postSideEffect(PiecesListSideEffect.NavigateToUserProfile(userData))
    }
}