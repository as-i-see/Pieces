package com.asisee.streetpieces.screens.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.OWN_USER_ID
import com.asisee.streetpieces.PIECE_ID
import com.asisee.streetpieces.SETTINGS_SCREEN
import com.asisee.streetpieces.SPLASH_SCREEN
import com.asisee.streetpieces.TAKE_PHOTO_SCREEN
import com.asisee.streetpieces.USER_ID
import com.asisee.streetpieces.USER_PIECES_SCREEN
import com.asisee.streetpieces.common.ext.parameterFromParameterValue
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.model.service.SubscriptionStorageService
import com.asisee.streetpieces.model.service.UserDataStorageService
import com.asisee.streetpieces.screens.LogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val pieceStorageService: PieceStorageService,
    private val userDataStorageService: UserDataStorageService,
    private val subscriptionStorageService: SubscriptionStorageService,
    private val accountService: AccountService
) : LogViewModel(logService) {
    var userId: String
    val pieces : Flow<List<Piece>>
    val userData : Flow<UserData>
    val nSubscribers : MutableState<Long?> = mutableStateOf(null)
    val nSubscriptions : MutableState<Long?> = mutableStateOf(null)
    var subscriptionId: MutableState<String?> = mutableStateOf(null) // TODO wrap
    val isOwnProfile: Boolean
    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)
    fun onAddClick(openScreen: (String) -> Unit) = openScreen(TAKE_PHOTO_SCREEN)
    fun onPieceClick(openScreen: (String) -> Unit, pieceId: String, userId: String) =
        openScreen("$USER_PIECES_SCREEN?$PIECE_ID={$pieceId}&$USER_ID={$userId}")
    init {
        userId = savedStateHandle.get<String>(USER_ID)!!.parameterFromParameterValue()
        if (userId == OWN_USER_ID) {
            userId = accountService.currentUserId
        }
        pieces = pieceStorageService.piecesByUser(userId)
        userData = userDataStorageService.userData(userId)
        isOwnProfile = userId == accountService.currentUserId
        viewModelScope.launch(Dispatchers.IO) {
            subscriptionId.value = subscriptionStorageService.subscriptionId(userId)
            nSubscribers.value = subscriptionStorageService.numberOfSubscribers(userId)
            nSubscriptions.value = subscriptionStorageService.numberOfSubscriptions(userId)
        }
    }

    fun checkAnonymousUser(openAndPopUpCurrent: (String) -> Unit) {
        accountService.currentUser?.let {
            if (it.isAnonymous)
                openAndPopUpCurrent(SPLASH_SCREEN)
        }
    }

    fun follow() = viewModelScope.launch {
        subscriptionId.value = subscriptionStorageService.subscribeCurrentUserTo(userId)
    }

    fun unfollow() = viewModelScope.launch {
        subscriptionId.value?.let {
            subscriptionStorageService.deleteSubscription(it)
        }
        subscriptionId.value = null
    }

}
