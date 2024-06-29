package com.asisee.streetpieces.model.service

import com.asisee.streetpieces.model.Post
import com.asisee.streetpieces.model.PostData
import kotlinx.coroutines.flow.Flow

interface PostService {
    val getPostsByCurrentUser: Flow<List<PostData>>

    fun getPostsByUser(userId: String): Flow<List<PostData>>

    fun getPostFlow(pieceId: String): Flow<PostData>

    suspend fun getPost(postId: String): PostData?

    suspend fun save(postData: PostData): String

    suspend fun update(postData: PostData)

    suspend fun delete(pieceId: String)

    fun latestPosts(): Flow<List<PostData>>
}
