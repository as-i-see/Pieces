package com.asisee.streetpieces.screens.own_profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.UserPosts
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.PostService
import com.asisee.streetpieces.model.service.FollowerService
import com.asisee.streetpieces.model.service.UserDataService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.syntax.simple.subIntent

@Factory
@OptIn(OrbitExperimental::class)
class OwnProfileScreenModel(
    private val postService: PostService,
    private val userDataService: UserDataService,
    private val followerService: FollowerService,
    private val accountService: AccountService
) : ScreenModel, ContainerHost<OwnProfileScreenState, OwnProfileScreenSideEffect> {
    override val container = screenModelScope.container<OwnProfileScreenState, OwnProfileScreenSideEffect>(OwnProfileScreenState()) {
        intent(registerIdling = false) {
            repeatOnSubscription {
                accountService.currentUserFlow.collectLatest { user ->
                    coroutineScope {
                        launch { collectPosts(user.id) }
                        launch { collectFollowers(user.id) }
                        launch { collectUserData(user.id) }
                    }
                }
            }
        }
    }

    private suspend fun collectUserData(userId: String) = subIntent {
        userDataService.userData(userId).collect { userData ->
            reduce {
                state.copy(
                    userData = userData
                )
            }
        }
    }

    private suspend fun collectPosts(userId: String) = subIntent {
        postService.getPostsByUser(userId).collect { posts ->
            reduce {
                state.copy(
                    posts = posts
                )
            }
        }
    }

    private suspend fun collectFollowers(userId: String) = subIntent {
        val numberOfFollowers = followerService.numberOfFollowers(userId)
        val numberOfFollowees = followerService.numberOfSubscriptions(userId)
        reduce {
            state.copy(
                numberOfFollowers = numberOfFollowers,
                numberOfFollowees = numberOfFollowees,
            )
        }
    }

    fun navigateBack() = intent {
        postSideEffect(OwnProfileScreenSideEffect.NavigateBack)
    }

    fun navigateToSettings() = intent {
        postSideEffect(OwnProfileScreenSideEffect.NavigateToSettings)
    }

    fun navigateToPiece(userPosts: UserPosts, clickedPostIndex: Int) = intent {
        postSideEffect(OwnProfileScreenSideEffect.NavigateToPiece(userPosts, clickedPostIndex))
    }

    fun navigateToPostCreation() = intent {
        postSideEffect(OwnProfileScreenSideEffect.NavigateToPostCreation)
    }
}