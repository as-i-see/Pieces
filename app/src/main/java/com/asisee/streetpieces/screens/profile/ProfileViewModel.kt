package com.asisee.streetpieces.screens.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.model.service.SubscriptionStorageService
import com.asisee.streetpieces.model.service.UserDataStorageService
import com.asisee.streetpieces.screens.LogViewModel
import com.asisee.streetpieces.screens.navArgs
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
    private val navArgs: ProfileScreenNavArgs = savedStateHandle.navArgs()
    val pieces: Flow<List<Piece>> = pieceStorageService.piecesByUser(navArgs.userId)
    val userData: Flow<UserData> = userDataStorageService.userData(navArgs.userId)
    val nSubscribers: MutableState<Long?> = mutableStateOf(null)
    val nSubscriptions: MutableState<Long?> = mutableStateOf(null)
    var subscriptionId: MutableState<String?> = mutableStateOf(null) // TODO wrap

    init {
        viewModelScope.launch(Dispatchers.IO) {
            subscriptionId.value = subscriptionStorageService.subscriptionId(navArgs.userId)
            nSubscribers.value = subscriptionStorageService.numberOfSubscribers(navArgs.userId)
            nSubscriptions.value = subscriptionStorageService.numberOfSubscriptions(navArgs.userId)
        }
    }

    fun checkAnonymousUser(toSplashScreen: () -> Unit) {
        accountService.currentUser?.let { if (it.isAnonymous) toSplashScreen() }
    }

    fun follow() =
        viewModelScope.launch {
            subscriptionId.value = subscriptionStorageService.subscribeCurrentUserTo(navArgs.userId)
        }

    fun unfollow() =
        viewModelScope.launch {
            subscriptionId.value?.let { subscriptionStorageService.deleteSubscription(it) }
            subscriptionId.value = null
        }
}
