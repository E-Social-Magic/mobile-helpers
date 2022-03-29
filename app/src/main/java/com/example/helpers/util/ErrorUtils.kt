package com.example.helpers.util

import com.example.helpers.models.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object ErrorUtils {
    inline fun <reified T> parseError(errorBody: ResponseBody): T {
        return Gson().fromJson(errorBody.charStream(), T::class.java)
    }
}
object RetrofitBuilderUtils{
     val gson: Gson = GsonBuilder()
        .setLenient()
        .create()
     fun  retrofitBuilder(okHttpClient: OkHttpClient ):Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .build()
    }
}
