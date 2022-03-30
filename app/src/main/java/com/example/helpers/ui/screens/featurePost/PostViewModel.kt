package com.example.helpers.ui.screens.featurePost

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpers.models.Constants
import com.example.helpers.models.data.repo.post.PostRepository
import com.example.helpers.models.data.request.CommentRequest
import com.example.helpers.models.data.response.Comment
import com.example.helpers.models.data.response.PostResponse
import com.example.helpers.models.domain.model.Message
import com.example.helpers.models.domain.model.PostEntry
import com.example.helpers.models.domain.model.PostModel
import com.example.helpers.util.Resource
import com.example.helpers.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(private val postRepository: PostRepository,private val sessionManager: SessionManager) : ViewModel() {
    private var currentPage = 1
    var postList = mutableStateOf<List<PostEntry>>(listOf())
    var allposts = mutableStateOf<List<PostEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    val newPost = mutableStateOf(
        PostModel(
            title = "",
            content = "",
            files = listOf(),
            groupId = "",
            coins = 100
        )
    )
    val groupId = mutableStateOf("")
    val coins = sessionManager.fetchCoin()
    val userIdSesstion = sessionManager.fetchUserId()
    var post = mutableStateOf<PostEntry?>(null)
    private var lastScrollIndex = 0
    private val _scrollUp = MutableLiveData(false)
    val scrollUp: LiveData<Boolean>
        get() = _scrollUp

    init {
        loadPostPaginated()
    }

    var searchValue = mutableStateOf("")
    var searchBarState = mutableStateOf(false)

    fun onSearchBarStateChange(newValue:Boolean){
        searchBarState.value = newValue
    }


    fun onSearchChange(newValue: String){
        viewModelScope.launch {
            searchValue.value = newValue
            if (searchValue.value.isEmpty()) {
                postList.value = allposts.value
                return@launch
            }
            delay(1000)
            val postsFormSearch = allposts.value.filter { data ->
                data.title.contains(searchValue.value, true) || data.content.contains(searchValue.value, true)
            }

            postList.value = postsFormSearch
        }
    }

    fun refresh(groupById: String?) {
        postList.value = listOf()
        currentPage = 1
        loadPostPaginated(groupById)
    }

    fun loadPostPaginated(groupById: String? = null) {
        viewModelScope.launch {
            isLoading.value = true
            val searchBy = groupById ?: if (groupId.value.isEmpty()) null else groupId.value
            val result = postRepository.getPosts(Constants.POST_SIZE, currentPage, searchBy)
            when (result) {
                is Resource.Success -> {
                    endReached.value = currentPage >= result.data!!.totalPages
                    val postListEntry = result.data.posts.filter {
                        !postList.value.map { value -> value.id }.contains(it.id)
                    }.mapIndexed { index, entry ->
                        postResponse2PostEntry(postReponse = entry)
                    }
                    currentPage++
                    loadError.value = ""
                    postList.value += postListEntry
                    delay(500L)
                    allposts.value = postList.value
                    isLoading.value = false
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    delay(500L)

                    isLoading.value = false
                }
                else -> {
                    delay(500L)
                    isLoading.value = false
                }
            }
        }
    }

    fun voteUp(postId: String) {
        viewModelScope.launch {
            viewModelScope.launch {
                postRepository.voteUp(postId)
                val getPostbyId = postRepository.getPostById(postId)

                when (getPostbyId) {
                    is Resource.Success -> {
                        if (getPostbyId.data != null) {
                            val postResponse = postResponse2PostEntry(getPostbyId.data)
                            val replacement =
                                postList.value.map { if (it.id == postId) postResponse else it }
                            postList.value = replacement
                            post.value = postResponse
                        }
                    }
                    is Resource.Error -> {
                        isLoading.value = false
                    }
                    else -> {
                    }
                }


                isLoading.value = false
            }

        }
    }
    fun voteUp(postId: String,commentId:String) {
        viewModelScope.launch {
            viewModelScope.launch {
                postRepository.voteUp(postId, commentId = commentId)
                val getPostbyId = postRepository.getPostById(postId)

                when (getPostbyId) {
                    is Resource.Success -> {
                        if (getPostbyId.data != null) {
                            val postResponse = postResponse2PostEntry(getPostbyId.data)
                            val replacement =
                                postList.value.map { if (it.id == postId) postResponse else it }
                            postList.value = replacement
                            post.value = postResponse
                        }
                    }
                    is Resource.Error -> {
                        isLoading.value = false
                    }
                    else -> {
                    }
                }


                isLoading.value = false
            }

        }
    }
    fun voteDown(postId: String) {
        viewModelScope.launch {
            postRepository.voteDown(postId)
            val getPostbyId = postRepository.getPostById(postId)

            when (getPostbyId) {
                is Resource.Success -> {
                    if (getPostbyId.data != null) {
                        val postResponse = postResponse2PostEntry(getPostbyId.data)
                        val replacement =
                            postList.value.map { if (it.id == postId) postResponse else it }
                        postList.value = replacement
                        post.value = postResponse
                    }
                }
                is Resource.Error -> {

                    isLoading.value = false
                }
                else -> {
                }
            }
            if (getPostbyId.data != null) {
                val postResponse = postResponse2PostEntry(getPostbyId.data)
                val replacement =
                    postList.value.map { if (it.id == postId) postResponse else it }
                postList.value = replacement
                post.value = postResponse
            }

            isLoading.value = false
        }
    }

    fun voteDown(postId: String, commentId: String) {
        viewModelScope.launch {
            postRepository.voteDown(postId, commentId = commentId)
            val getPostbyId = postRepository.getPostById(postId)

            when (getPostbyId) {
                is Resource.Success -> {
                    if (getPostbyId.data != null) {
                        val postResponse = postResponse2PostEntry(getPostbyId.data)
                        val replacement =
                            postList.value.map { if (it.id == postId) postResponse else it }
                        postList.value = replacement
                        post.value = postResponse
                    }
                }
                is Resource.Error -> {

                    isLoading.value = false
                }
                else -> {
                }
            }
            if (getPostbyId.data != null) {
                val postResponse = postResponse2PostEntry(getPostbyId.data)
                val replacement =
                    postList.value.map { if (it.id == postId) postResponse else it }
                postList.value = replacement
                post.value = postResponse
            }

            isLoading.value = false
        }
    }

    fun onTitleChange(title: String) {
        newPost.value = newPost.value.copy(title = title)
    }

    fun onContentChange(content: String) {
        newPost.value = newPost.value.copy(content = content)
    }

    fun onGroupSelected(groupId: String) {
        newPost.value = newPost.value.copy(groupId = groupId)
    }

    fun createPost() {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.newPost(postModel = newPost.value)
            loadPostPaginated()
        }
    }

    fun markAnswerIsCorrect(postId: String, commentId: String) {
        viewModelScope.launch {
            postRepository.markAnswerIsCorrect(postId = postId, commentId = commentId)
        }
    }

    fun loadPostByGroup(groupId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = postRepository.getPosts(Constants.POST_SIZE, currentPage)
            when (result) {
                is Resource.Success -> {
                    endReached.value = currentPage >= result.data!!.totalPages
                    val postListEntry = result.data.posts.mapIndexed { index, entry ->
                        postResponse2PostEntry(postReponse = entry)
                    }
                    currentPage++
                    loadError.value = ""
                    isLoading.value = false
                    postList.value += postListEntry
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> {}
            }
        }
    }

    fun findPostById(id: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = postRepository.getPostById(id)
            try {
                when (result) {
                    is Resource.Success -> {
                        if (result.data != null)
                            post.value = postResponse2PostEntry(result.data)
                    }
                    is Resource.Error -> {
                        post.value = null
                    }
                    else -> {
                        post.value = null
                    }
                }
            } catch (e: Exception) {
                post.value = null
            }
            isLoading.value = false
        }
    }

    fun submitComment(postId: String, message: Message): Boolean {
        viewModelScope.launch {
            isLoading.value = true
            val result = postRepository.newComment(postId = postId, comment = CommentRequest(
                message.message.toRequestBody(
                    MultipartBody.FORM
                )
            ), files = message.images?.map { File(it) })
            try {
                when (result) {
                    is Resource.Success -> {
                        val postResponse = postResponse2PostEntry(result.data!!.post)
                        val replacement =
                            postList.value.map { if (it.id == postId) postResponse else it }
                        postList.value = replacement
                        post.value = postResponse
                    }
                    is Resource.Error -> {

                    }
                    else -> {

                    }
                }
            } catch (e: Exception) {

            }

            isLoading.value = false
        }
        return true
    }


    companion object {
        fun commentToMessage(comments: List<Comment>): List<Message> {
            return comments.map {
                Message(
                    id = it._id,
                    authorName = it.userName,
                    avatarAuthor = it.avatar,
                    message = it.comment,
                    images = it.images,
                    isCorrect = it.correct,
                    userId = it.userId,
                    votes = it.votes
                )
            }.reversed()
        }

        fun postResponse2PostEntry(postReponse: PostResponse): PostEntry {
            return PostEntry(
                title = postReponse.title,
                createdAt = postReponse.createdAt,
                images = postReponse.images,
                comments = commentToMessage(postReponse.comments),
                authorAvatar = postReponse.authorAvatar,
                userName = postReponse.userName,
                id = postReponse.id,
                content = postReponse.content,
                votes = postReponse.votes,
                updatedAt = postReponse.updatedAt,
                userId = postReponse.userId,
                videos = postReponse.videos,
                hideName = postReponse.hideName,
                expired = postReponse.expired,
                coins = postReponse.coins,
                costs = postReponse.costs
            )
        }
    }


    fun updateScrollPosition(newScrollIndex: Int) {
        if (newScrollIndex == lastScrollIndex) return
        _scrollUp.value = newScrollIndex > lastScrollIndex
        lastScrollIndex = newScrollIndex
    }




}