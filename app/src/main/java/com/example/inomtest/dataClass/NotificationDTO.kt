package com.example.inomtest.dataClass

import android.app.Notification
import java.util.*
import kotlin.collections.ArrayList

data class NotificationDTO(val data: DataNotification)

data class DataNotification(val items: ArrayList<NotificationData>)

data class NotificationData(
    val notificationId : Int = 1,
    val content : String  = "내용",
    val read : Boolean = false,
    val notificationType : String = "TRADE",
    val referenceId : Int = 1,
    val createdAt : String = "2022-01-20T21:28:50"
)
