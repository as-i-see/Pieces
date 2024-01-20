package com.asisee.streetpieces.model.service.impl

import com.asisee.streetpieces.model.Piece
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.model.service.trace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PieceStorageServiceImpl @Inject constructor(private val firestore: FirebaseFirestore, private val auth: AccountService) : PieceStorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val piecesByCurrentUser: Flow<List<Piece>>
        get() = auth.currentUserFlow.flatMapLatest { user ->
            firestore.collection(PIECE_COLLECTION).whereEqualTo(USER_ID_FIELD, user.id).dataObjects()
        }

    override fun piecesByUser(userId: String): Flow<List<Piece>> =
        firestore.collection(PIECE_COLLECTION).whereEqualTo(USER_ID_FIELD, userId).dataObjects()

    override fun piece(pieceId: String): Flow<Piece> =
        firestore.collection(PIECE_COLLECTION).document(pieceId).dataObjects<Piece>().map {
            it ?: Piece()
        }


    override suspend fun getPiece(pieceId: String): Piece? =
        firestore.collection(PIECE_COLLECTION).document(pieceId).get().await().toObject()

    override suspend fun save(piece: Piece): String = trace(SAVE_PIECE_TRACE) {
        val taskWithUserId = piece.copy(userId = auth.currentUserId)
        firestore.collection(PIECE_COLLECTION).add(taskWithUserId).await().id
    }

    override suspend fun update(piece: Piece): Unit = trace(UPDATE_PIECE_TRACE) {
        firestore.collection(PIECE_COLLECTION).document(piece.id).set(piece).await()
    }

    override suspend fun delete(pieceId: String) {
        firestore.collection(PIECE_COLLECTION).document(pieceId).delete().await()
    }

    override fun latestPieces(): Flow<List<Piece>> =
        firestore.collection(PIECE_COLLECTION)
            .orderBy(SORTING_BY_DATE_FIELD, Query.Direction.DESCENDING)
            .limit(LATEST_PIECES_AMOUNT)
            .dataObjects()


    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val PIECE_COLLECTION = "pieces"
        private const val SAVE_PIECE_TRACE = "savePiece"
        private const val UPDATE_PIECE_TRACE = "updatePiece"
        private const val SORTING_BY_DATE_FIELD = "dateTimeInEpochSeconds"
        private const val LATEST_PIECES_AMOUNT = 20L
    }
}