package com.example.helpers.ui.screens.featureVideo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpers.models.data.repo.post.PostRepository
import com.example.helpers.ui.screens.featurePost.PostViewModel
import com.example.helpers.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VideosViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
    private var currentPage = 1
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var videos = mutableStateOf<List<VideoItem>>(listOf())
    val currentlyPlayingIndex = MutableLiveData<Int?>()

    init {
//        populateListWithFakeData()
        loadVideoPaginated()

    }

    private fun populateListWithFakeData() {
//        val testVideos = listOf(
//            VideoItem(
//                "1",
//                title =   "Java",
//                content = "Can you help me this question ?",
//                mediaUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
//                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
//
//            ),
//            VideoItem(
//                "2",
//                title =   "Kotlin",
//                content = "Can you help me this question ?",
//                mediaUrl ="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
//                thumbnail =  "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg"
//            ),
//            VideoItem(
//                "3",
//                title =   "Python",
//                content = "Can you help me this question ?",
//                mediaUrl ="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
//                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg"
//            ),
//            VideoItem(
//                "4",
//                title =  "C# C++",
//                content = "Can you help me this question ?",
//                mediaUrl ="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
//                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerEscapes.jpg"
//            ),
//            VideoItem(
//                "5",
//                title = "Rust",
//                content = "Can you help me this question ?",
//                mediaUrl ="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
//                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerFun.jpg"
//            ),
//            VideoItem(
//                "6",
//                title =   "Ruby",
//                content = "Can you help me this question ?",
//                mediaUrl ="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
//                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerJoyrides.jpg"
//            ),
//            VideoItem(
//                "7",
//                title =  "Math",
//                content = "Can you help me this question ?",
//                mediaUrl ="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
//                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerMeltdowns.jpg"
//            ),
//            VideoItem(
//                "8",
//                title =  "English",
//                content = "Can you help me this question ?",
//                mediaUrl ="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
//                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/Sintel.jpg"
//            ),
//            VideoItem(
//                "9",
//                title =  "Programming",
//                content =  "Can you help me this question ?",
//                mediaUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
//                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/SubaruOutbackOnStreetAndDirt.jpg"
//            ),
//            VideoItem(
//                "10",
//               title =  "Fix bug",
//                content ="Can you help me this question ?",
//                mediaUrl ="http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
//                thumbnail = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/TearsOfSteel.jpg"
//            ),
//        )
//        videos.value+=testVideos
    }

    fun onPlayVideoClick(playbackPosition: Long, videoIndex: Int) {
        when (currentlyPlayingIndex.value) {
            // video is not playing at the moment
            null -> currentlyPlayingIndex.postValue(videoIndex)
            // this video is already playing
            videoIndex -> {
                currentlyPlayingIndex.postValue(null)
                videos.value = videos.value.toMutableList().also { list ->
                    list[videoIndex] = list[videoIndex].copy(lastPlayedPosition = playbackPosition)
                }
            }
            // video is playing, and we're requesting new video to play
            else -> {
                videos.value = videos.value.toMutableList().also { list ->
                    list[currentlyPlayingIndex.value!!] =
                        list[currentlyPlayingIndex.value!!].copy(lastPlayedPosition = playbackPosition)
                }
                currentlyPlayingIndex.postValue(videoIndex)
            }
        }
    }
    fun loadVideoPaginated() {
        val dispatcher = if (currentPage==1) Dispatchers.Main else Dispatchers.IO
        viewModelScope.launch {
            isLoading.value = true
            val result = postRepository.getPosts(100, currentPage)
            when (result) {
                is Resource.Success -> {
                    endReached.value = currentPage >= result.data!!.totalPages
                    val videoItem = result.data.posts.filter { it.videos.isNotEmpty() }.map{ entry ->
                        val comments = if (!entry.comments.isNullOrEmpty()) PostViewModel.commentToMessage(entry.comments)
                        else listOf()
                        VideoItem(
                            id = entry.id,
                            content=entry.content,
                            title=entry.title,
                            mediaUrl = entry.videos[0],
                            thumbnail = "https://gaplo.tech/content/images/2020/03/android-jetpack.jpg",
                            userName = entry.userName,
                            votes = entry.votes,
                            createdAt = entry.createdAt,
                            updatedAt = entry.updatedAt,
                            comments = comments,
                            authorAvatar = entry.authorAvatar,
                            userId = entry.userId
                        )
                    }
                    currentPage++
                    loadError.value = ""
                    isLoading.value = false
                    videos.value += videoItem
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

}
