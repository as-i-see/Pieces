package com.asisee.streetpieces.screens.feed

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.PostService
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

@Factory
class FeedScreenModel(
    private val postService: PostService
) : ScreenModel, ContainerHost<FeedScreenState, FeedScreenSideEffect> {

    override val container = screenModelScope.container<FeedScreenState, FeedScreenSideEffect>(FeedScreenState.Loading)

    fun navigateToUserProfile(userData: UserData) = intent {
        postSideEffect(FeedScreenSideEffect.NavigateToUserProfile(userData))
    }
}