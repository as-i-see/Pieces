package com.asisee.streetpieces.model.service

import com.asisee.streetpieces.model.Piece
import kotlinx.coroutines.flow.Flow

interface PieceStorageService {
    val piecesByCurrentUser: Flow<List<Piece>>
    fun piecesByUser(userId: String): Flow<List<Piece>>
    fun piece(pieceId: String) : Flow<Piece>
    suspend fun getPiece(pieceId: String): Piece?
    suspend fun save(piece: Piece): String
    suspend fun update(piece: Piece)
    suspend fun delete(pieceId: String)
    fun latestPieces(): Flow<List<Piece>>
}