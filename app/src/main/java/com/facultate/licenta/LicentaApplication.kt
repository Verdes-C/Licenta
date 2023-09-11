package com.facultate.licenta

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LicentaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val ordersChannel = "508"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ordersChannel,
                "Orders updates",
                NotificationManager.IMPORTANCE_DEFAULT,

                )
            channel.description = "We will send you order updates."
            notificationManager.createNotificationChannel(channel)
        }
    }
}