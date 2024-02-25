package com.asisee.streetpieces

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.asisee.streetpieces.screens.destinations.OwnProfileScreenDestination
import com.asisee.streetpieces.screens.destinations.SearchFeedScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import com.asisee.streetpieces.R.drawable as AppIcon
import com.asisee.streetpieces.R.string as AppText

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    SearchFeed(SearchFeedScreenDestination, AppIcon.ic_search, AppText.search),
    Profile(OwnProfileScreenDestination, AppIcon.ic_avatar, AppText.profile)
}
