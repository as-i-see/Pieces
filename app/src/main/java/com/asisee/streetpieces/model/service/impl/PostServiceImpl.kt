package com.asisee.streetpieces.model.service.impl

import com.asisee.streetpieces.model.Post
import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.PostService
import com.asisee.streetpieces.model.service.UserDataService
import com.asisee.streetpieces.model.service.trace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class PostServiceImpl(
    private val firestore: FirebaseFirestore, private val auth: AccountService, private val userDataService: UserDataService
) : PostService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val getPostsByCurrentUser: Flow<List<PostData>>
        get() =
            auth.currentUserFlow.flatMapLatest { user ->
                firestore
                    .collection(POST_COLLECTION)
                    .whereEqualTo(USER_ID_FIELD, user.id)
                    .dataObjects()
            }

    override fun getPostsByUser(userId: String): Flow<List<PostData>> =
        firestore.collection(POST_COLLECTION).whereEqualTo(USER_ID_FIELD, userId).dataObjects()

    override fun getPostFlow(pieceId: String): Flow<PostData> =
        firestore.collection(POST_COLLECTION).document(pieceId).dataObjects<PostData>().map {
            it ?: PostData()
        }

    override suspend fun getPost(postId: String): PostData? =
        firestore.collection(POST_COLLECTION).document(postId).get().await().toObject()

    override suspend fun save(postData: PostData): String =
        trace(SAVE_PIECE_TRACE) {
            val taskWithUserId = postData.copy(authorId = auth.currentUserId)
            firestore.collection(POST_COLLECTION).add(taskWithUserId).await().id
        }

    override suspend fun update(postData: PostData): Unit =
        trace(UPDATE_PIECE_TRACE) {
            firestore.collection(POST_COLLECTION).document(postData.id).set(postData).await()
        }

    override suspend fun delete(pieceId: String) {
        firestore.collection(POST_COLLECTION).document(pieceId).delete().await()
    }

    override fun latestPosts(): Flow<List<PostData>> =
        firestore
            .collection(POST_COLLECTION)
            .orderBy(SORTING_BY_DATE_FIELD, Query.Direction.DESCENDING)
            .limit(LATEST_PIECES_AMOUNT)
            .dataObjects<PostData>()
//            .mapLatest {
//                it.map {
//                    Post(
//                        author = userDataService.getUserData(it.authorId),
//                        data = it
//                    )
//                }
//            }

    companion object {
        private const val USER_ID_FIELD = "authorId"
        private const val POST_COLLECTION = "posts"
        private const val SAVE_PIECE_TRACE = "savePiece"
        private const val UPDATE_PIECE_TRACE = "updatePiece"
        private const val SORTING_BY_DATE_FIELD = "dateTimeInEpochSeconds"
        private const val LATEST_PIECES_AMOUNT = 20L
    }
}
