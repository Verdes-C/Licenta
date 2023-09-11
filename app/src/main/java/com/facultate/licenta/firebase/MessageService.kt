package com.facultate.licenta.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BADGE_ICON_SMALL
import androidx.core.app.NotificationCompat.BigPictureStyle
import androidx.core.app.NotificationCompat.BigTextStyle
import androidx.core.app.NotificationCompat.DecoratedCustomViewStyle
import com.facultate.licenta.MainActivity
import com.facultate.licenta.R
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Variables
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageService : FirebaseMessagingService() {

    private val tag = "CLOUD MESSAGE"
    private val ordersChannel = "508"
    override fun onNewToken(token: String) {
        sendTokenToServer(token)
    }

    private fun sendTokenToServer(token: String) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            if (user.email != null) {
                FirebaseFirestore.getInstance().collection("Users").document(user.email!!).get()
                    .addOnSuccessListener {
                        it.reference.update("fcmToken", token)
                    }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(tag, "From: ${message}")
        if (message.data.isNotEmpty()) {
            showNotification(message.data["title"], message.data["body"])
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val contentView =
            RemoteViews(this.packageName, R.layout.notification).apply {
                setTextViewText(R.id.notification_title, title)
                setTextViewText(R.id.notification_text, body)
            }

        val deepLinkIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("inkquill://${Screens.Orders.route}"))
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            deepLinkIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, ordersChannel)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_action_name)
            .setBadgeIconType(BADGE_ICON_SMALL)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources,R.drawable.ic_action_name))
            .setContentTitle(title)
            .setContentText(body)
            .setContent(contentView)
            .setStyle(BigPictureStyle())
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

}