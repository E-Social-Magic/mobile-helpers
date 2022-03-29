package com.example.helpers.models.data.remote


import com.example.helpers.models.data.response.Topic
import com.example.helpers.models.data.response.TopicList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TopicApi {
    @GET("groups")
    suspend fun getTopicList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,

    ): TopicList

    @GET("groups/{group-id}")
    suspend fun getTopicInfo(
        @Path("name") name: String
    ): Topic

}