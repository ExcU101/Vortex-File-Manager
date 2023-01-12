package io.github.excu101.vortex.service.notification

import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

interface NotificationCenter {

    fun message(
        id: Int,
        title: String,
        message: String,
        @DrawableRes
        iconRes: Int,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT
    )

}