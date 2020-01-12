package com.dylanbui.routerapp.typicode.push_notification

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import com.dylanbui.android_library.push_manager.DbNotificationData
import com.dylanbui.android_library.push_manager.DbPushManagerMessagingService
import com.dylanbui.android_library.push_manager.DbRemoteMessageData
import com.dylanbui.android_library.utils_extentions.intentFor
import com.dylanbui.routerapp.MainActivity

class MyService : DbPushManagerMessagingService() {

    override fun handleNotificationPayload(context: Context, remoteMessageData: DbRemoteMessageData): DbNotificationData? {
        Log.d("PushManager", "handlePayload - ${remoteMessageData.title} - ${remoteMessageData.body}")

        // create pending intent
//        val notificationIntent = Intent(context, MainActivity::class.java)
//        notificationIntent.putExtra("type", remoteMessageData.title ?: "Default Title"+" "+remoteMessageData.body ?: "Default Message")
        val notificationIntent = intentFor<MainActivity>("message" to "messageBody")
        // notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // create NotificationData containing all necessary information
        return DbNotificationData(
            context = context,
            title = remoteMessageData.title ?: "Default Title",
            message = remoteMessageData.body ?: "Default Message",
            channelId = "channel_global_notifications",     // needed for SDK >= Android "O" (Oreo)
            autoCancel = true,
            pendingIntent = contentIntent)
    }
}


//PushmanagerMessagingService : FirebaseMessagingService() {
//class MyFirebaseMessagingService : FirebaseMessagingService() {
//
//    override fun onNewToken(p0: String) {
//        super.onNewToken(p0)
//
//    }
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        // TODO: Handle FCM messages here.
//
//
//
//        // If the application is in the foreground handle both data and notification messages here.
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated.
//        Log.d(TAG, "From: " + remoteMessage.from)
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.notification?.body)
//    }
//
//    companion object {
//        private const val TAG = "FCM Service"
//    }
//}