package com.asisee.streetpieces.model.service.impl

import androidx.core.net.toUri
import com.asisee.streetpieces.model.service.PhotoStorageService
import com.google.firebase.storage.FirebaseStorage
import org.koin.core.annotation.Single
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
@Single
class PhotoStorageServiceImpl(storage: FirebaseStorage) : PhotoStorageService {
    private val storageRef = storage.reference

    private suspend fun uploadImageToFolder(uri: String, folder: String) =
        suspendCoroutine { continuation ->
            val pieceRef = storageRef.child("$folder/${UUID.randomUUID()}")
            val uploadTask = pieceRef.putFile(uri.toUri())
            uploadTask
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    pieceRef.downloadUrl
                }
                .addOnFailureListener { throw it }
                .addOnSuccessListener { continuation.resume(it.toString()) }
        }

    override suspend fun uploadPiecePhoto(uri: String) = uploadImageToFolder(uri, POSTS_FOLDER)

    override suspend fun uploadAvatar(uri: String) = uploadImageToFolder(uri, AVATARS_FOLDER)

    companion object {
        private const val POSTS_FOLDER = "posts"
        private const val AVATARS_FOLDER = "avatars"
    }
}
