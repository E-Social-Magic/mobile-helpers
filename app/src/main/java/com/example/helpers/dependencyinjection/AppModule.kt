package com.example.helpers.dependencyinjection

import android.content.Context
import com.example.helpers.MessagesActivity
import com.example.helpers.R
import com.example.helpers.models.data.remote.GroupApi
import com.example.helpers.models.data.remote.PostApi
import com.example.helpers.models.data.remote.TopicApi
import com.example.helpers.models.data.remote.UserApi
import com.example.helpers.models.data.repo.group.GroupRepository
import com.example.helpers.models.data.repo.group.impl.GroupRepositoryImpl
import com.example.helpers.models.data.repo.post.PostRepository
import com.example.helpers.models.data.repo.post.impl.PostRepositoryImpl
import com.example.helpers.models.data.repo.topic.TopicRepository
import com.example.helpers.models.data.repo.topic.impl.TopicRepositoryImpl
import com.example.helpers.models.data.repo.user.UserRepository
import com.example.helpers.models.data.repo.user.impl.UserRepositoryImpl
import com.example.helpers.util.AuthInterceptor
import com.example.helpers.util.RetrofitBuilderUtils
import com.example.helpers.util.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.notifications.handler.NotificationConfig
import io.getstream.chat.android.client.notifications.handler.NotificationHandlerFactory
import io.getstream.chat.android.core.ExperimentalStreamChatApi
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import io.getstream.chat.android.offline.experimental.plugin.Config
import io.getstream.chat.android.offline.experimental.plugin.OfflinePlugin
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserRepository(api: UserApi): UserRepository = UserRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideTopicRepository(
        api: TopicApi
    ): TopicRepository = TopicRepositoryImpl(api)

    @Singleton
    @Provides
    fun providePostRepository(api: PostApi): PostRepository = PostRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideGroupRepository(api: GroupApi): GroupRepository = GroupRepositoryImpl(api)


    @Provides
    @Singleton
    fun provideUserApi(@Named("OkHttpClientInterceptor") okHttpClient: OkHttpClient): UserApi {

        return RetrofitBuilderUtils.retrofitBuilder(okHttpClient)
            .create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTopicApi(@Named("OkHttpClientInterceptor") okHttpClient: OkHttpClient): TopicApi {
        return RetrofitBuilderUtils.retrofitBuilder(okHttpClient).create(TopicApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGroupApi(@Named("OkHttpClientInterceptor") okHttpClient: OkHttpClient): GroupApi {
        return RetrofitBuilderUtils.retrofitBuilder(okHttpClient).create(GroupApi::class.java)
    }

    @Singleton
    @Provides
    fun providePostApi(@Named("OkHttpClientInterceptor") okHttpClient: OkHttpClient): PostApi {
        return RetrofitBuilderUtils.retrofitBuilder(okHttpClient).create(PostApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context) = SessionManager(context)

    @Singleton
    @Provides
    @Named("AuthInterceptor")
    fun provideAuthInterceptor(sessionManager: SessionManager): Interceptor =
        AuthInterceptor(sessionManager)

    @Singleton
    @Provides
    @Named("OkHttpClientInterceptor")
    fun provideOkHttpClient(@Named("AuthInterceptor") interceptor: Interceptor): OkHttpClient {
                val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @OptIn(InternalStreamChatApi::class, ExperimentalStreamChatApi::class)
    @Singleton
    @Provides
    fun provideChatClient(@ApplicationContext context: Context): ChatClient {
        val notificationConfig = NotificationConfig()
        val notificationHandler = NotificationHandlerFactory.createNotificationHandler(
            context = context,
            newMessageIntent = { _: String, channelType: String, channelId: String ->
                MessagesActivity.createIntent(
                    context = context,
                    channelId = "$channelType:$channelId"
                )
            }
        )
        val offlinePlugin = OfflinePlugin(Config(userPresence = true, persistenceEnabled = true))
        return ChatClient.Builder(context.getString(R.string.api_key), context)
            .notifications(notificationConfig, notificationHandler)
            .withPlugin(offlinePlugin)
            .build()
    }


}


