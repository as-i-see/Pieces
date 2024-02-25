package com.asisee.streetpieces.model.service.impl

import android.net.Uri
import com.asisee.streetpieces.model.service.PhotoStorageService
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PhotoStorageServiceImpl @Inject constructor(storage: FirebaseStorage) : PhotoStorageService {
    private val storageRef = storage.reference

    private suspend fun uploadImageToFolder(uri: Uri, folder: String) =
        suspendCoroutine { continuation ->
            val pieceRef = storageRef.child("$folder/${UUID.randomUUID()}")
            val uploadTask = pieceRef.putFile(uri)
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

    override suspend fun uploadPiecePhoto(uri: Uri) = uploadImageToFolder(uri, PIECES_FOLDER)

    override suspend fun uploadAvatar(uri: Uri) = uploadImageToFolder(uri, AVATARS_FOLDER)

    companion object {
        private const val PIECES_FOLDER = "pieces"
        private const val AVATARS_FOLDER = "avatars"
    }
}
