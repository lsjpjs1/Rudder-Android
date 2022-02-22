package com.rudder.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.ui.activity.SplashActivity

class MyFireBaseMessagingService: FirebaseMessagingService() {
    private val TAG = "FirebaseService"
    private val NOTIFICATION_TOKEN_KEY = "notificationKey"


    // 파이어베이스 서비스의 토큰을 가져온다
    override fun onNewToken(p0: String) {
        App.prefs.setValue(NOTIFICATION_TOKEN_KEY,p0)
    }

    // 새로운 FCM 메시지가 있을 때 메세지를 받는다
    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        remoteMessage.notification?.let {
            sendNotification(it.body, it.title)
        }
    }

    // FCM 메시지를 보내는 메시지
    private fun sendNotification(body: String?, title: String?) {
        val intent = Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", body)
            putExtra("Notification",title)
        }

        var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "Notification", //채널 ID
                "CHATTING", //채널명
                IMPORTANCE_HIGH //알림음이 울리며 헤드업 알림 표시
            )
            channel.apply {
                enableLights(true)
                lightColor= Color.RED
                enableVibration(true)
                description = "notification"
                notificationManager.createNotificationChannel(channel)
            }
        }

        // 푸시알람 부가설정
        var notificationBuilder = NotificationCompat.Builder(this,"Notification")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())


    }

}