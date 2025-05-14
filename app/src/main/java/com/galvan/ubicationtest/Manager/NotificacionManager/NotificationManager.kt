package com.adedeveloment.slotmachine.manager

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.galvan.ubicationtest.Data.GlobalConfigurations.GlobalConfigurations.CHANNEL_ID
import com.galvan.ubicationtest.Data.GlobalConfigurations.GlobalConfigurations.CHANNEL_ID_BACKGORUND
import com.galvan.ubicationtest.Data.GlobalConfigurations.GlobalConfigurations.GROUP_KEY_MESSAGES
import com.galvan.ubicationtest.Data.GlobalConfigurations.GlobalConfigurations.GROUP_NOTIFICATION
import com.galvan.ubicationtest.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton


@SuppressLint("NotificationPermission")
@Singleton
class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val manager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private var countNotification = 0
    val managerData = ManagerData(context)

    init {
//        createNotificationChannel()
//        createNotificationChannelBackgorund()
//        createSummaryNotification()
    }

//    fun createSilentNotification(): Notification {
//        return NotificationCompat.Builder(context, CHANNEL_ID_BACKGORUND)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .setCategory(NotificationCompat.CATEGORY_SYSTEM)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setSilent(true)
//            .build()
//    }

//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                context.getString(R.string.channel),
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            manager.createNotificationChannel(channel)
//        }
//    }

//    private fun createNotificationChannelBackgorund() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID_BACKGORUND,
//                context.getString(R.string.channel2),
//                NotificationManager.IMPORTANCE_NONE
//            )
//            manager.createNotificationChannel(channel)
//        }
//    }
//
//    private fun createSummaryNotification(): Notification {
//        val inboxStyle = NotificationCompat.InboxStyle()
//            .setBigContentTitle(GROUP_NOTIFICATION)
//            .setSummaryText("$countNotification $GROUP_NOTIFICATION")
//
//        return NotificationCompat.Builder(context, CHANNEL_ID)
//            .setContentTitle(GROUP_NOTIFICATION)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setGroup(GROUP_KEY_MESSAGES)
//            .setGroupSummary(true)
//            .setStyle(inboxStyle)
//            .setAutoCancel(true)
//            .build()
//    }
//
//    fun createNotification(title: String, content: String) {
//        countNotification++
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setContentTitle(title)
//            .setContentText(content)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setGroup(GROUP_KEY_MESSAGES)
//            .setAutoCancel(true)
//            .build()
//        manager.notify(getCountNotification(), notification)
//        manager.notify(0, createSummaryNotification())
//    }


//    fun createUpdateNotification(version:String) {
//        countNotification++
//        val updateIntent = Intent(context, UpdateActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//
//        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setContentTitle(context.getString(R.string.update_app))
//            .setContentText(context.getString(R.string.newVersionapp) + version)
//            .setSmallIcon(R.drawable.baseline_system_update_24)
//            .setStyle(NotificationCompat.BigTextStyle().bigText(context.getString(R.string.newVersionapp) + version))
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setGroup(GROUP_KEY_MESSAGES)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent) // Asignar el intent a la notificación
//            .build()
//
//        manager.notify(getCountNotification(), notification)
//        manager.notify(0, createSummaryNotification())
//    }

    fun deleteNotification(){
        manager.cancel(0)
    }


    private fun getCountNotification(): Int {
        return Random().nextInt(60000)
    }
}
