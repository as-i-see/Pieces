package com.asisee.streetpieces.screens.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.NavBackActionToolbar
import com.asisee.streetpieces.common.composable.Post
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.composable.SpacerS
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.screens.own_profile.OwnProfileScreen
import com.asisee.streetpieces.screens.user_profile.UserProfileScreen
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Settings
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
class PostScreen(private val userData: UserData? = null, private val postData: PostData) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<PostScreenModel>(parameters = { parametersOf(userData, postData) })
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when(sideEffect) {
                is PostScreenSideEffect.PopBack -> {
                    navigator.pop()
                }
                is PostScreenSideEffect.NavigateToUserProfile -> {
                    navigator.push(UserProfileScreen(sideEffect.userData))
                }
                is PostScreenSideEffect.NavigateToOwnProfile -> {
                    navigator.push(OwnProfileScreen(sideEffect.userData))
                }
            }
        }
        View(
            state = state,
            popBack = screenModel::popBack,
            title = "Explore",
            toProfile = screenModel::toUserProfile
        )
    }
    @Composable
    private fun View(
        state: PostScreenState,
        popBack: () -> Unit,
        title: String,
        toProfile: (UserData) -> Unit
    ) {
        when(state) {
            PostScreenState.Loading -> {
                ProgressIndicator()
            }
            is PostScreenState.Post -> {
                Column(Modifier.fillMaxWidth()) {
                    NavBackActionToolbar(navigateBack = popBack, title = title, actionIconButton = {})
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Post(
                            postData = state.post,
                            userData = state.author,
                            toProfile = toProfile
                        )
                        SpacerS()
                    }
                    SpacerS()
                }

            }
        }
    }
}