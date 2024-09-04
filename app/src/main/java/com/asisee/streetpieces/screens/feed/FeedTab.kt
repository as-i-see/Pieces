package com.asisee.streetpieces.screens.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Home
import com.asisee.streetpieces.R.string as AppText

data object FeedTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(AppText.feed)
            val icon = rememberVectorPainter(EvaIcons.Outline.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(FeedTabScreen())
    }
}