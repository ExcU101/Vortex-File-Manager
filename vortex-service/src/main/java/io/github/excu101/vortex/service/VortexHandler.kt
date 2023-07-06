package io.github.excu101.vortex.service

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VortexHandler(
    private val context: Context,
) : CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) {

    fun closeNotification(id: Int, delay: Long = 500L) = launch {
        delay(timeMillis = delay)
        context.getSystemService<NotificationManager>()?.cancel(id)
    }

}