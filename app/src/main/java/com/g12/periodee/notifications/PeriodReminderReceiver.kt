package com.g12.periodee.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PeriodReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationHelper.show(
            context = context,
            title = "🌸 Period",
            message = "Tes règles arrivent demain",
            id = 100
        )

    }
}
