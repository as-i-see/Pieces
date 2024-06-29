package com.asisee.streetpieces.screens.searchfeed

import com.asisee.streetpieces.model.PostData

sealed interface SearchFeedScreenState {
    data object Loading: SearchFeedScreenState

    data class SearchFeed(
        val posts: List<PostData>
    ) : SearchFeedScreenState
}