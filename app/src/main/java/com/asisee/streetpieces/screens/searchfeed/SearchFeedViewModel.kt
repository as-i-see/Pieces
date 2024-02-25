package com.asisee.streetpieces.screens.searchfeed

import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.screens.LogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchFeedViewModel
@Inject
constructor(pieceStorageService: PieceStorageService, logService: LogService) :
    LogViewModel(logService) {
    val pieces = pieceStorageService.latestPieces()
}
