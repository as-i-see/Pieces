package com.asisee.streetpieces.model.service


interface PhotoStorageService {
    suspend fun uploadPiecePhoto(uri: String): String

    suspend fun uploadAvatar(uri: String): String
}
