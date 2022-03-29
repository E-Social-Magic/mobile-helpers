package com.example.helpers.models.data.repo.post.impl

import android.util.Log
import android.webkit.MimeTypeMap
import com.example.helpers.models.data.remote.PostApi
import com.example.helpers.models.data.repo.post.PostRepository
import com.example.helpers.models.data.request.CommentRequest
import com.example.helpers.models.data.request.NewPostRequest
import com.example.helpers.models.data.response.*
import com.example.helpers.models.domain.model.PostModel
import com.example.helpers.util.ErrorUtils
import com.example.helpers.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.HttpURLConnection
import javax.inject.Inject


@ActivityScoped
class PostRepositoryImpl @Inject constructor(private val api: PostApi) : PostRepository {

    override suspend fun getPosts(
        limit: Int,
        offset: Int,
        groupId: String?
    ): Resource<PostListResponse> {
        val response = api.getPosts(limit = limit, offset = offset, groupId)
        return when (response.code()) {
            HttpURLConnection.HTTP_BAD_METHOD -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_FORBIDDEN -> {
                Resource.Error(data = response.body(), message = "HTTP_FORBIDDEN")
            }
            else -> {
                response.body()?.let {
                    Resource.Success(data = it)
                } ?: Resource.Error("Empty response")
            }
        }


    }

    override suspend fun voteUp(postId: String): Resource<VoteResponse> {
        val response = api.voteUp(postId = postId, up = "true")
        return when (response.code()) {
            HttpURLConnection.HTTP_BAD_METHOD -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_FORBIDDEN -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            else -> {
                response.body()?.let {
                    Resource.Success(data = it)
                } ?: Resource.Error("Empty response")
            }
        }
    }

    override suspend fun voteDown(postId: String): Resource<VoteResponse> {
        val response = api.voteUp(postId = postId, down = "true")
        return when (response.code()) {
            HttpURLConnection.HTTP_BAD_METHOD -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_FORBIDDEN -> {
                Resource.Error(data = response.body(), message = "HTTP_FORBIDDEN")
            }
            else -> {
                response.body()?.let {
                    Resource.Success(data = it)
                } ?: Resource.Error("Empty response")
            }
        }
    }

    override suspend fun newPost(postModel: PostModel): Resource<PostResponse> {
        val map: HashMap<String, RequestBody> = HashMap()
        val newPostRequest = NewPostRequest(
            title = postModel.title.toRequestBody(MultipartBody.FORM),
            content = postModel.content.toRequestBody(MultipartBody.FORM),
            groupId = postModel.groupId.toRequestBody(MultipartBody.FORM),
            hideName = postModel.hideName.toString().toRequestBody(MultipartBody.FORM),
            expired = postModel.expired.toString().toRequestBody(MultipartBody.FORM),
            coins = postModel.coins.toString().toRequestBody(MultipartBody.FORM),
            costs = postModel.costs.toString().toRequestBody(MultipartBody.FORM)
        )

        map["title"] = newPostRequest.title
        map["content"] = newPostRequest.content
        map["hideName"] = newPostRequest.hideName
        map["costs"] = newPostRequest.costs
        map["expired"] = newPostRequest.expired
        map["coins"] = newPostRequest.coins
        map["group_id"] = newPostRequest.groupId
        val uploads = listFiletoMultiBody(postModel.files)
        val response = api.newPost(params = map, files = uploads)
        return when (response.code()) {
            HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_BAD_METHOD -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_FORBIDDEN -> {
                Resource.Error(data = response.body(), message = "HTTP_FORBIDDEN")
            }
            else -> {
                response.body()?.let {
                    Resource.Success(data = it)
                } ?: Resource.Error("Empty response")
            }
        }
    }

    override suspend fun getPostById(postId: String): Resource<PostResponse> {
        val response = api.getPostById(postId)
        return when (response.code()) {
            HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                Log.d("File", ErrorUtils.parseError<String>(errorBody = errorBody).toString())

                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_BAD_METHOD -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_FORBIDDEN -> {
                Resource.Error(data = response.body()?.post, message = "HTTP_FORBIDDEN")
            }
            else -> {
                response.body()?.let {
                    Log.d("FindPostById", it.post.toString())
                    Resource.Success(data = it.post)
                } ?: Resource.Error("Empty response")
            }
        }
    }

    override suspend fun newComment(
        postId: String,
        comment: CommentRequest,
        files: List<File>?
    ): Resource<NewCommentResponse> {


        val map: HashMap<String, RequestBody> = HashMap()
        map["comment"] = comment.comment
        val response =
            api.newComment(postId = postId, params = map, files = listFiletoMultiBody(files))
        return when (response.code()) {
            HttpURLConnection.HTTP_INTERNAL_ERROR -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                Log.d("File", ErrorUtils.parseError<String>(errorBody = errorBody).toString())

                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_BAD_METHOD -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_NOT_FOUND -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                val errorBody = response.errorBody() ?: run {
                    throw NullPointerException()
                }
                return Resource.Error(data = ErrorUtils.parseError(errorBody = errorBody))
            }
            HttpURLConnection.HTTP_FORBIDDEN -> {
                Resource.Error(data = response.body(), message = "HTTP_FORBIDDEN")
            }
            else -> {
                response.body()?.let {
                    Resource.Success(data = it)
                } ?: Resource.Error("Empty response")
            }
        }
    }

    override suspend fun markAnswerIsCorrect(
        postId: String,
        commentId: String
    ): Resource<MarkCorrectAnswerResponse> {
        try {
            val response = api.markAnswerIsCorrect(postId = postId, commentId = commentId)
            return response.body()?.let {
                Resource.Success(data = it)
            } ?: Resource.Error("Empty response")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Resource.Error("Empty response")
    }

    fun listFiletoMultiBody(files: List<File>?): List<MultipartBody.Part>? {
        if (files == null) {
            return null
        }
        return files.map { file ->
            MultipartBody.Part.createFormData(
                name = "files", filename = file.name, file.asRequestBody(
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
                        ?.toMediaTypeOrNull()
                )
            )
        }
    }
}