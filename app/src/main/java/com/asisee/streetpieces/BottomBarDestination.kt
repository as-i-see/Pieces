package com.asisee.streetpieces

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.asisee.streetpieces.R.string as AppText
import com.asisee.streetpieces.R.drawable as AppIcon

sealed class BottomBarDestination(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val icon_focused: Int
) {
    data object SearchFeed : BottomBarDestination(
        route = SEARCH_FEED_SCREEN,
        title = AppText.search,
        icon = AppIcon.ic_search,
        icon_focused = AppIcon.ic_search
    )
    data object Profile : BottomBarDestination(
        route = "$PROFILE_SCREEN?$USER_ID={${OWN_USER_ID}}",
        title = AppText.profile,
        icon = AppIcon.ic_avatar,
        icon_focused = AppIcon.ic_avatar
    )

}