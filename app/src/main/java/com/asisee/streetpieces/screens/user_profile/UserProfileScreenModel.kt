package com.asisee.streetpieces.screens.user_profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.SubscriptionState
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.UserPieces
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.PostService
import com.asisee.streetpieces.model.service.FollowerService
import com.asisee.streetpieces.model.service.UserDataService
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.syntax.simple.runOn

@Factory
@OptIn(OrbitExperimental::class)
class UserProfileScreenModel(
    private val postService: PostService,
    private val userDataService: UserDataService,
    private val followerService: FollowerService,
    private val accountService: AccountService
) : ScreenModel, ContainerHost<UserProfileScreenState, UserProfileScreenSideEffect> {

    override val container =
        screenModelScope.container<UserProfileScreenState, UserProfileScreenSideEffect>(UserProfileScreenState.Loading)

    fun setUserData(userData: UserData) = intent {
        val selfUserId = accountService.currentUserId
        val subscriptionState = followerService.subscriptionState(selfUserId, userData.userId)
        reduce {
            UserProfileScreenState.UserProfile(
                userData = userData,
                subscriptionState = subscriptionState
            )
        }
        val numberOfFollowers = followerService.numberOfFollowers(userData.userId)
        val numberOfFollowees = followerService.numberOfFollowees(userData.userId)
        runOn(UserProfileScreenState.UserProfile::class) {
            reduce {
                state.copy(
                    numberOfFollowers = numberOfFollowers,
                    numberOfFollowees = numberOfFollowees
                )
            }
        }
        repeatOnSubscription {
            postService.getPostsByUser(userData.userId).collect { pieces ->
                runOn(UserProfileScreenState.UserProfile::class) {
                    reduce {
                        state.copy(
                            pieces = pieces
                        )
                    }
                }
            }
        }
    }

    fun navigateBack() = intent {
        postSideEffect(UserProfileScreenSideEffect.NavigateBack)
    }

    fun navigateToPiece(userPieces: UserPieces, index: Int) = intent {
        postSideEffect(UserProfileScreenSideEffect.NavigateToPiece(userPieces, index))
    }

    fun follow() = intent {
        runOn(UserProfileScreenState.UserProfile::class) {
            val newSubscriptionState = followerService.follow(accountService.currentUserId, state.userData.userId)
            if (state.subscriptionState is SubscriptionState.NotFollowing) {
                reduce {
                    state.copy(
                        subscriptionState = newSubscriptionState,
                        numberOfFollowers = state.numberOfFollowers + 1
                    )
                }
            }
        }
    }

    fun unfollow() = intent {
        runOn(UserProfileScreenState.UserProfile::class) {
            (state.subscriptionState as? SubscriptionState.Following)?.let { subscriptionState ->
                followerService.deleteSubscription(subscriptionState.subscriptionId)
                reduce {
                    state.copy(
                        subscriptionState = SubscriptionState.NotFollowing,
                        numberOfFollowers = state.numberOfFollowers - 1
                    )
                }
            }
        }
    }
}