package com.asisee.streetpieces.screens.feed

import android.content.Context
import android.widget.Toast
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.UserPost
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.FollowerService
import com.asisee.streetpieces.model.service.PostService
import com.asisee.streetpieces.model.service.UserDataService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription

@Factory
class FeedScreenModel(
    private val context: Context,
    private val accountService: AccountService,
    private val followerService: FollowerService,
    private val postService: PostService,
    private val userDataService: UserDataService,
) : ScreenModel, ContainerHost<FeedScreenState, FeedScreenSideEffect> {

    override val container = screenModelScope.container<FeedScreenState, FeedScreenSideEffect>(FeedScreenState.Loading) {
        val currentUser = accountService.currentUserId
        repeatOnSubscription {
            followerService.userSubscriptions(currentUser).flatMapLatest { ids ->
                if (ids.isEmpty())
                    flow {
                        emit(emptyList())
                    }
                else postService.getPostsByUserSubscriptions(ids.map { it.subUserId }).map { postDatas ->
                    postDatas.map { postData ->
                        UserPost(userDataService.getUserData(postData.authorId), postData)
                    }
                }
            }.collect { userPosts ->
                reduce {
                    FeedScreenState.Feed(userPosts)
                }
            }
        }
    }

    fun navigateToUserProfile(userData: UserData) = intent {
        postSideEffect(FeedScreenSideEffect.NavigateToUserProfile(userData))
    }
}