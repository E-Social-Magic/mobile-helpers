package com.example.helpers.ui.screens.featureGroup

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import androidx.lifecycle.viewModelScope
import com.example.helpers.models.Constants
import com.example.helpers.models.data.repo.topic.TopicRepository
import com.example.helpers.models.domain.model.TopicIndexListEntry
import com.example.helpers.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TopicListViewModel @Inject constructor(private val repository: TopicRepository ) : ViewModel() {
    private var curPage = 1
    var topicList = mutableStateOf<List<TopicIndexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    val selectedTopic = mutableStateOf<List<String>>(listOf())
    init {
        loadTopicPaginated()
    }

    fun loadTopicPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getTopicList(Constants.PAGE_SIZE, curPage)
            when(result) {
                is Resource.Success-> {
                    endReached.value = curPage  >= result.data!!.totalPages

                    val topicIndexEntries = result.data.groups.filter { !topicList.value.map {topicIndexListEntry -> topicIndexListEntry.id}.contains(it.id)  }.mapIndexed{ index, entry ->
                        val url = "https://gaplo.tech/content/images/2020/03/android-jetpack.jpg"
                        TopicIndexListEntry(groupName = if(entry.groupName.isNullOrBlank()) "Math" else entry.groupName, avatar = if(entry.avatar.isNullOrBlank())url else entry.avatar ,id = entry.id)
                    }
                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                    topicList.value += topicIndexEntries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
    fun selectedTopics(newSubjectId: String){

        if (selectedTopic.value.contains(newSubjectId)){
            selectedTopic.value = selectedTopic.value.filter { it!=newSubjectId }
        }
        else{
            selectedTopic.value = selectedTopic.value.plus(newSubjectId)
        }
    }
}