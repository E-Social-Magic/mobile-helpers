package com.example.helpers.ui.screens.featureChat

import android.content.Context
import com.example.helpers.MessagesActivity
import io.getstream.chat.android.client.BuildConfig
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.notifications.handler.NotificationHandlerFactory
import io.getstream.chat.android.core.ExperimentalStreamChatApi
import io.getstream.chat.android.core.internal.InternalStreamChatApi
import io.getstream.chat.android.offline.experimental.plugin.Config
import io.getstream.chat.android.offline.experimental.plugin.OfflinePlugin

@OptIn(InternalStreamChatApi::class)
object ChatHelper {

    /**
     * Initializes the SDK with the given API key.
     */
    @OptIn(ExperimentalStreamChatApi::class)
    fun initializeSdk(context: Context, apiKey: String) {
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

        val logLevel = if (BuildConfig.DEBUG) ChatLogLevel.ALL else ChatLogLevel.NOTHING

        ChatClient.Builder(apiKey, context)
            .withPlugin(offlinePlugin)
            .logLevel(logLevel)
            .build()
    }

    /**
     * Initializes [ChatClient] with the given user and saves it to the persistent storage.
     */

}
