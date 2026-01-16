package com.g12.periodee.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.g12.periodee.engine.CycleEngine
import com.g12.periodee.engine.TipsEngine
import com.g12.periodee.data.FirestoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyBuddyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        CoroutineScope(Dispatchers.IO).launch {

            val repo = FirestoreRepository()
            val user = repo.getUser() ?: return@launch

            val phase = CycleEngine.getPhase(
                user.lastPeriodDate,
                user.cycleLength
            )

            val (title, body) = TipsEngine.getBuddyMessage(phase)

            NotificationHelper.show(
                context = context,
                title = title,
                message = body,
                id = 200
            )
        }
    }
}
