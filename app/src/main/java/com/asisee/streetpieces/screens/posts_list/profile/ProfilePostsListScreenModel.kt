package com.asisee.streetpieces.screens.posts_list.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.PostService
import com.asisee.streetpieces.model.service.UserDataService
import com.asisee.streetpieces.screens.posts_list.PostsListSideEffect
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

@Factory
class ProfilePostsListScreenModel(
    private val postService: PostService,
    private val userDataService: UserDataService,
    private val accountService: AccountService,
) : ScreenModel, ContainerHost<ProfilePostsListState, PostsListSideEffect> {

    override val container = screenModelScope.container<ProfilePostsListState, PostsListSideEffect>(
        ProfilePostsListState(UserData(), emptyList())
    )

    fun popBack() = intent {
        postSideEffect(PostsListSideEffect.PopBack)
    }

    fun toUserProfile(userData: UserData) = intent {
        if (accountService.currentUserId == userData.userId)
            postSideEffect(PostsListSideEffect.NavigateToOwnProfile(userData))
        else postSideEffect(PostsListSideEffect.NavigateToUserProfile(userData))
    }
}