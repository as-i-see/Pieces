package com.asisee.streetpieces.model.service

import com.asisee.streetpieces.model.PostData
import com.asisee.streetpieces.model.Subscription
import kotlinx.coroutines.flow.Flow

interface PostService {
    val getPostsByCurrentUser: Flow<List<PostData>>

    fun getPostsByUser(userId: String): Flow<List<PostData>>

    fun getPostsByUserSubscriptions(subscriptions: List<String>) : Flow<List<PostData>>

    fun getPostFlow(pieceId: String): Flow<PostData>

    suspend fun getPost(postId: String): PostData?

    suspend fun save(postData: PostData): String

    suspend fun update(postData: PostData)

    suspend fun delete(pieceId: String)

    fun latestPosts(): Flow<List<PostData>>
}
