package com.dylanbui.android_library.push_manager

import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import com.dylanbui.android_library.R

/**
 * <p>NotificationLight contains all information for handling smartphone-led on incoming message</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
data class DbNotificationLight(val argb: Int, val onMS: Int, val offMS: Int)

/**
 * <p>NotificationData is a wrapper for creating a custom notification based on a incomming remote message.</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
data class DbNotificationData(val context: Context,
                            val title: String,
                            val message: String,
                            val soundUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                            val smallIcon: Int = R.drawable.ic_baseline_notifications_24px,
                            val autoCancel: Boolean = true,
                            val channelId: String = context.getString(R.string.default_notification_channel_id),
                            var color: Int? = null,
                            val colorize: Boolean = false,
                            val contentInfo: String? = null,
                            val category: String? = null,
                            val light: DbNotificationLight? = null,
                            val timeoutAfter: Long? = null,
                            val subtext: String? = null,
                            val publicVersion: DbNotificationData? = null,
                            val onGoing: Boolean? = null,
                            val ticker: String? = null,
                            val useChronometer: Boolean? = null,
                            val notificationId: Int? = null,
                            val pendingIntent: PendingIntent? = null,
                            val notificationType: DbNotificationType = DbNotificationType.STACK)

enum class DbNotificationType { SINGLE, STACK }

/**
 * <p>RemoteMessageData is a model for holding all notification information that's available from incomming message</p>
 *
 * @since    1.0.0
 * @version  1.0.0
 * @author   grumpyshoe
 *
 */
data class DbRemoteMessageData(
    var tag: String? = null,
    var bodyLocalizationKey: String? = null,
    var bodyLocalizationArgs: Array<String>? = null,
    var title: String? = null,
    var body: String? = null,
    var clickAction: String? = null,
    var color: String? = null,
    var icon: String? = null,
    var link: Uri? = null,
    var sound: String? = null,
    var titleLocalizationKey: String? = null,
    var titleLocalizationArgs: Array<String>? = null,
    var data: MutableMap<String, String>? = null,
    var messageType: String? = null,
    var collapseKey: String? = null,
    var from: String? = null,
    var messageId: String? = null,
    var originalPriority: Int? = null,
    var priority: Int? = null,
    var sentTime: Long? = null,
    var to: String? = null,
    var ttl: String? = null)

/**
 * <p>PushManager - interface for easy access to FCM</p>
 *
 * @version  1.2.0
 * @since    1.0.0
 * @author   grumpyshoe
 *
 */
interface DbPushManager {
    /**
     * register device for cloudmessaging
     *
     */
    fun register(context: Context, onTokenReceived: (String) -> Unit, onFailure: (Exception?) -> Unit)
    /**
     * unregister device from cloudmessaging
     *
     */
    fun unregister(context: Context, token: String)
    /**
     * subscribe to topic
     *
     */
    fun subscribeToTopic(topic: String, onSuccess: (() -> Unit)? = null, onFailure: ((Exception?) -> Unit)? = null)
    /**
     * unsubscribe from topic
     *
     */
    fun unsubscribeFromTopic(topic: String, onSuccess: (() -> Unit)? = null, onFailure: ((Exception?) -> Unit)? = null)
}