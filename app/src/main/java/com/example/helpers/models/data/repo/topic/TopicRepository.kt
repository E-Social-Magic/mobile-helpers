package com.example.helpers.models.data.repo.topic

import com.example.helpers.models.data.response.Topic
import com.example.helpers.models.data.response.TopicList
import com.example.helpers.util.Resource

interface TopicRepository {
    suspend fun getTopicList(limit:Int,offset: Int): Resource<TopicList>
    suspend fun getTopicInfo(pokemonName: String): Resource<Topic>
}