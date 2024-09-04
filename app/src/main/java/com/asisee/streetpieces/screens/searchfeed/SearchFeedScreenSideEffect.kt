package com.asisee.streetpieces.screens.searchfeed

import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData

sealed interface SearchFeedScreenSideEffect {
    data class NavigateToPost(val postData: PostData) : SearchFeedScreenSideEffect
}