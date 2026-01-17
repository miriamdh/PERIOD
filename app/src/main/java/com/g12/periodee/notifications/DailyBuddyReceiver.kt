package com.g12.periodee.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.g12.periodee.engine.TipsEngine

class DailyBuddyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        try {
            Log.d("DailyBuddyReceiver", "Daily buddy triggered")

            val (title, body) = TipsEngine.getBuddyMessage("Règles")

            NotificationHelper.show(
                context = context,
                title = title,
                message = body,
                id = 200
            )

        } catch (e: Exception) {
            Log.e("DailyBuddyReceiver", "Erreur buddy", e)
        }
    }
}
