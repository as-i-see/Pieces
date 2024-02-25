package com.asisee.streetpieces.model.service

import android.net.Uri

interface PhotoStorageService {
    suspend fun uploadPiecePhoto(uri: Uri): String

    suspend fun uploadAvatar(uri: Uri): String
}
