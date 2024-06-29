package com.asisee.streetpieces.screens.searchfeed

import com.asisee.streetpieces.model.PostData

sealed interface SearchFeedScreenSideEffect {
    data class NavigateToPost(val postData: PostData) : SearchFeedScreenSideEffect
}