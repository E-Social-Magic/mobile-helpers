package com.example.helpers.models.data.repo.post

import com.example.helpers.models.data.request.CommentRequest
import com.example.helpers.models.data.response.*
import com.example.helpers.models.domain.model.PostModel
import com.example.helpers.util.Resource
import java.io.File

interface PostRepository {
    suspend fun getPosts(limit:Int,offset:Int,groupId:String?=null): Resource<PostListResponse>
    suspend fun voteUp(postId:String):Resource<VoteResponse>
    suspend fun voteDown(postId:String):Resource<VoteResponse>
    suspend fun voteUp(postId:String,commentId: String):Resource<VoteResponse>
    suspend fun voteDown(postId:String,commentId: String):Resource<VoteResponse>
    suspend fun newPost(postModel: PostModel):Resource<PostResponse>
    suspend fun getPostById(postId:String):Resource<PostResponse>
    suspend fun newComment(postId: String,comment: CommentRequest,files:List<File>?):Resource<NewCommentResponse>
    suspend fun markAnswerIsCorrect(postId: String,commentId:String):Resource<MarkCorrectAnswerResponse>
}