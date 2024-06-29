package com.asisee.streetpieces.screens.searchfeed

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.service.PostService
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription

@Factory
class SearchFeedScreenModel(
    private val postService: PostService
) : ScreenModel, ContainerHost<SearchFeedScreenState, SearchFeedScreenSideEffect> {

    override val container =
        screenModelScope.container<SearchFeedScreenState, SearchFeedScreenSideEffect>(SearchFeedScreenState.Loading) {
            repeatOnSubscription {
                launch {
                    postService.latestPosts().collect {
                        reduce {
                            SearchFeedScreenState.SearchFeed(it)
                        }
                    }
                }
            }
        }

    fun toPost(postData: PostData) = intent {
        postSideEffect(SearchFeedScreenSideEffect.NavigateToPost(postData))
    }

//    fun navigateToUserProfile(userData: UserData) = intent {
//        postSideEffect(FeedScreenSideEffect.NavigateToUserProfile(userData))
//    }
}