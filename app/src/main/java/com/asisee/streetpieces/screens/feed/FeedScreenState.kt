package com.asisee.streetpieces.screens.feed

import com.asisee.streetpieces.model.UserPost

sealed interface FeedScreenState {
    data object Loading : FeedScreenState

    data class Feed(val posts: List<UserPost>) : FeedScreenState
}