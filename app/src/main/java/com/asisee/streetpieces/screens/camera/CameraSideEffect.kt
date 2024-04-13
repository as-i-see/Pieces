package com.asisee.streetpieces.screens.camera

import android.net.Uri

sealed interface CameraSideEffect {
    data class NavigateWithPhotoResult(val photoUri: Uri) : CameraSideEffect
    data class Error(val message: String) : CameraSideEffect

}