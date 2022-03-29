package com.example.helpers.models.data.repo.topic.impl

import com.example.helpers.models.data.remote.TopicApi
import com.example.helpers.models.data.repo.topic.TopicRepository
import com.example.helpers.models.data.response.Topic
import com.example.helpers.models.data.response.TopicList
import com.example.helpers.util.Resource

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class TopicRepositoryImpl @Inject constructor(
    private val api: TopicApi
):TopicRepository {

    override suspend fun getTopicList(limit: Int, offset: Int): Resource<TopicList> {
        val response = try {
            api.getTopicList(limit, offset)
        } catch(e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }

    override suspend fun getTopicInfo(pokemonName: String): Resource<Topic> {
        val response = try {
            api.getTopicInfo(pokemonName)
        } catch(e: Exception) {
            return Resource.Error("An unknown error occured.")
        }
        return Resource.Success(response)
    }
}