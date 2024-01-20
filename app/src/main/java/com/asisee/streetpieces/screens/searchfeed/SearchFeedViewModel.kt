package com.asisee.streetpieces.screens.searchfeed

import com.asisee.streetpieces.PIECE_ID
import com.asisee.streetpieces.PIECE_SCREEN
import com.asisee.streetpieces.USER_ID
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.screens.LogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchFeedViewModel @Inject constructor(pieceStorageService: PieceStorageService, logService: LogService) : LogViewModel(logService) {
    val pieces = pieceStorageService.latestPieces()

    fun openPieceScreen(open: (String) -> Unit, pieceId: String, userId: String) {
        open("$PIECE_SCREEN?$PIECE_ID={$pieceId}&$USER_ID={$userId}")
    }

}