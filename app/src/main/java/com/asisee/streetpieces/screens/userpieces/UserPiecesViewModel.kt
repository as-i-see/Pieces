package com.asisee.streetpieces.screens.userpieces

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.PIECE_ID
import com.asisee.streetpieces.USER_ID
import com.asisee.streetpieces.common.ext.parameterFromParameterValue
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.model.service.UserDataStorageService
import com.asisee.streetpieces.screens.LogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserPiecesViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val pieceStorageService: PieceStorageService,
    private val userDataStorageService: UserDataStorageService,
): LogViewModel(logService) {
    val pieces: Flow<List<Piece>>
    val userData: Flow<UserData>
    private val clickedPieceId: String
    val lazyListState = LazyListState(1)
    init {
        clickedPieceId = savedStateHandle.get<String>(PIECE_ID)!!.parameterFromParameterValue()
        val userId = savedStateHandle.get<String>(USER_ID)!!.parameterFromParameterValue()
        pieces = pieceStorageService.piecesByUser(userId)
        userData = userDataStorageService.userData(userId)
        scrollToClickedPiece()
    }

    private fun scrollToClickedPiece() = viewModelScope.launch {
        val list = pieces.flowOn(Dispatchers.IO).first()
        val indexOfClickedItem = list.indexOfFirst {
            it.id == clickedPieceId
        }
        lazyListState.scrollToItem(indexOfClickedItem)
    }

    fun popUp(popUp: () -> Unit) = popUp()
}