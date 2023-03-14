package com.naarad.naaradsdk

import androidx.core.app.NotificationCompat
import com.naarad.naaradsdk.models.NaaradRemoteMessage
import com.naarad.naaradsdk.services.NaaradMessagingService

class NotificationHandlerService: NaaradMessagingService() {
    override fun buildNotification(remoteMessage: NaaradRemoteMessage): NotificationCompat.Builder? {
        return super.buildNotification(remoteMessage)?.setSmallIcon(R.drawable.rocket)
    }
}