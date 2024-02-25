package com.asisee.streetpieces.screens.userpieces

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.model.service.UserDataStorageService
import com.asisee.streetpieces.screens.LogViewModel
import com.asisee.streetpieces.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPiecesViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val pieceStorageService: PieceStorageService,
    private val userDataStorageService: UserDataStorageService,
) : LogViewModel(logService) {
    private val navArgs: UserPiecesScreenNavArgs = savedStateHandle.navArgs()
    val pieces: Flow<List<Piece>> = pieceStorageService.piecesByUser(navArgs.userId)
    val userData: Flow<UserData> = userDataStorageService.userData(navArgs.userId)
    val lazyListState = LazyListState(1)

    init {
        scrollToClickedPiece()
    }

    private fun scrollToClickedPiece() =
        viewModelScope.launch {
            val list = pieces.flowOn(Dispatchers.IO).first()
            val indexOfClickedItem = list.indexOfFirst { it.id == navArgs.clickedPieceId }
            lazyListState.scrollToItem(indexOfClickedItem)
        }
}
