package com.asisee.streetpieces.screens.piece

import androidx.lifecycle.SavedStateHandle
import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.model.service.UserDataStorageService
import com.asisee.streetpieces.screens.LogViewModel
import com.asisee.streetpieces.screens.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PieceViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val pieceStorageService: PieceStorageService,
    private val userDataStorageService: UserDataStorageService,
) : LogViewModel(logService) {
    private val navArgs: PieceScreenNavArgs = savedStateHandle.navArgs()
    val piece: Flow<Piece> = pieceStorageService.piece(navArgs.pieceId)
    val userData: Flow<UserData> = userDataStorageService.userData(navArgs.userId)
}
