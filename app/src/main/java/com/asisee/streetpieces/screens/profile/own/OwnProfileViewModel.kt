package com.asisee.streetpieces.screens.profile.own

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnProfileViewModel
@Inject
constructor(
    logService: LogService,
    private val pieceStorageService: PieceStorageService,
    private val userDataStorageService: UserDataStorageService,
    private val subscriptionStorageService: SubscriptionStorageService,
    private val accountService: AccountService
) : LogViewModel(logService) {
    val pieces: Flow<List<Piece>>
    val userData: Flow<UserData>
    val nSubscribers: MutableState<Long?> = mutableStateOf(null)
    val nSubscriptions: MutableState<Long?> = mutableStateOf(null)

    init {
        accountService.currentUserId.let { ownUserId ->
            if (ownUserId.isNotEmpty()) {
                pieces = pieceStorageService.piecesByUser(ownUserId)
                userData = userDataStorageService.userData(ownUserId)
                viewModelScope.launch(Dispatchers.IO) {
                    nSubscribers.value = subscriptionStorageService.numberOfSubscribers(ownUserId)
                    nSubscriptions.value =
                        subscriptionStorageService.numberOfSubscriptions(ownUserId)
                }
            } else {
                pieces = emptyFlow()
                userData = emptyFlow()
            }
        }
    }

    fun checkAnonymousUser(toSplashScreen: () -> Unit) {
        accountService.currentUser?.let { if (it.isAnonymous) toSplashScreen() }
    }
}
