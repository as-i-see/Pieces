package com.asisee.streetpieces.screens.piece

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.OWN_USER_ID
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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PiecesViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val pieceStorageService: PieceStorageService,
    private val userDataStorageService: UserDataStorageService,
): LogViewModel(logService) {
    val piece: Flow<Piece>
    val userData: Flow<UserData>

    init {
        val pieceId = savedStateHandle.get<String>(PIECE_ID)!!.parameterFromParameterValue()
        val userId = savedStateHandle.get<String>(USER_ID)!!.parameterFromParameterValue()
        piece = pieceStorageService.piece(pieceId)
        userData = userDataStorageService.userData(userId)
    }

    fun popUp(popUp: () -> Unit) = popUp()
}