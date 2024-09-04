package com.asisee.streetpieces.screens.post

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.UserDataService
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription

@Factory
class PostScreenModel(
    @InjectedParam userData: UserData?,
    @InjectedParam postData: PostData,
    private val userDataService: UserDataService,
    private val accountService: AccountService,
) : ScreenModel, ContainerHost<PostScreenState, PostScreenSideEffect> {

    override val container = if (userData == null)
        screenModelScope.container<PostScreenState, PostScreenSideEffect>(PostScreenState.Loading) {
            repeatOnSubscription {
                launch {
                    val authorData = userDataService.getUserData(postData.authorId)
                    reduce {
                        PostScreenState.Post(
                            author = authorData,
                            post = postData
                        )
                    }
                }
            }
        } else screenModelScope.container<PostScreenState, PostScreenSideEffect>(PostScreenState.Post(userData, postData))

    fun popBack() = intent {
        postSideEffect(PostScreenSideEffect.PopBack)
    }

    fun toUserProfile(userData: UserData) = intent {
        if (accountService.currentUserId == userData.userId)
            postSideEffect(PostScreenSideEffect.NavigateToOwnProfile(userData))
        else postSideEffect(PostScreenSideEffect.NavigateToUserProfile(userData))
    }
}