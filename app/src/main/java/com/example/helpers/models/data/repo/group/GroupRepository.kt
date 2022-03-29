package com.example.helpers.models.data.repo.group

import com.example.helpers.models.data.response.TopicList
import com.example.helpers.util.Resource

interface GroupRepository {
    suspend fun getGroups(): Resource<TopicList>
    suspend fun getGroups(userId:String): Resource<TopicList>

}