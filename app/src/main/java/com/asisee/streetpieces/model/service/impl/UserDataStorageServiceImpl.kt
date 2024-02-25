package com.asisee.streetpieces.model.service.impl

import com.asisee.streetpieces.model.UserData
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.UserDataStorageService
import com.asisee.streetpieces.model.service.trace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataStorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) :
    UserDataStorageService {
    override fun userData(userId: String): Flow<UserData> =
        firestore
            .collection(USERDATA_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, userId)
            .dataObjects<UserData>()
            .map { it.firstOrNull() ?: UserData() }

    override suspend fun getUserData(userId: String) =
        firestore
            .collection(USERDATA_COLLECTION)
            .whereEqualTo(USER_ID_FIELD, userId)
            .get()
            .await()
            .toObjects<UserData>()
            .first()

    override suspend fun save(userData: UserData): String =
        trace(SAVE_USERDATA_TRACE) {
            val userDataWithUserId = userData.copy(userId = auth.currentUserId)
            firestore.collection(USERDATA_COLLECTION).add(userDataWithUserId).await().id
        }

    override suspend fun update(userData: UserData): Unit =
        trace(UPDATE_USERDATA_TRACE) {
            firestore
                .collection(USERDATA_COLLECTION)
                .document(userData.userId)
                .set(userData)
                .await()
        }

    override suspend fun delete(userId: String) {
        firestore.collection(USERDATA_COLLECTION).document(userId).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val USERDATA_COLLECTION = "userData"
        private const val SAVE_USERDATA_TRACE = "savePiece"
        private const val UPDATE_USERDATA_TRACE = "updatePiece"
    }
}
