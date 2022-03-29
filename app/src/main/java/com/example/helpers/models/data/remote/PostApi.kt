package com.example.helpers.models.data.remote

import com.example.helpers.models.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface PostApi {
    @GET("posts")
    suspend fun getPosts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("group_id") groupId: String?
    ): Response<PostListResponse>

    @GET("post/{post-id}/vote")
    suspend fun voteUp(
        @Path("post-id") postId: String,
        @Query("up") up: String = "false",
        @Query("down") down: String = "false",
    ): Response<VoteResponse>

    @GET("post/{post-id}/vote/{comment-id}")
    suspend fun voteUp(
        @Path("post-id") postId: String,
        @Path("comment-id") commentId: String,
        @Query("up") up: String = "false",
        @Query("down") down: String = "false",
    ): Response<VoteResponse>
    @GET("post/{post-id}")

    suspend fun getPostById(@Path("post-id") postId: String): Response<PostByIdResponse>

    @Multipart
    @POST("post/new")
    suspend fun newPost(
        @PartMap params: @JvmSuppressWildcards Map<String, RequestBody>,
        @Part files: List<MultipartBody.Part>?,
    ): Response<PostResponse>

    @Multipart
    @PUT("post/{post-id}/comment")
    suspend fun newComment(
        @Path("post-id") postId: String,
        @PartMap params: @JvmSuppressWildcards Map<String, RequestBody>,
        @Part files: List<MultipartBody.Part>?
    ): Response<NewCommentResponse>

    @PUT("post/{post-id}/comment/{comment-id}/markCorrect")
    suspend fun markAnswerIsCorrect(
        @Path("post-id") postId : String,
        @Path("comment-id") commentId:String
    ): Response<MarkCorrectAnswerResponse>
}