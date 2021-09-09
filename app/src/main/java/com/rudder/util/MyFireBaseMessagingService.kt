package com.rudder.util

import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.ui.activity.MainActivity

class MyFireBaseMessagingService: FirebaseMessagingService() {
    private val TAG = "FirebaseService"
    private val NOTIFICATION_TOKEN_KEY = "notificationKey"

    // 파이어베이스 서비스의 토큰을 가져온다
    override fun onNewToken(p0: String) {
        App.prefs.setValue(NOTIFICATION_TOKEN_KEY,p0)
        Log.d(TAG, "new Token: $p0")
    }

    // 새로운 FCM 메시지가 있을 때 메세지를 받는다
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)

        // 앱이 포어그라운드 상태에서 Notificiation을 받는 경우
        if(remoteMessage.notification != null) {
            sendNotification(remoteMessage.notification?.body, remoteMessage.notification?.title)
        }
    }

    // FCM 메시지를 보내는 메시지
    private fun sendNotification(body: String?, title: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", body)
            putExtra("Notification",title)
        }

        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 푸시알람 부가설정
        var notificationBuilder = NotificationCompat.Builder(this,"Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(0, notificationBuilder.build())
    }

}