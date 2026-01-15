package com.g12.periodee.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveUser(firstName: String, cycleLength: Int, lastPeriodDate: String) {
        val userData = hashMapOf(
            "firstName" to firstName,
            "cycleLength" to cycleLength,
            "lastPeriodDate" to lastPeriodDate
        )

        db.collection("users")
            .document("main")
            .set(userData)
    }

    suspend fun getUser(): User? {
        val snapshot = db.collection("users")
            .document("main")
            .get()
            .await()

        return if (snapshot.exists()) {
            User(
                firstName = snapshot.getString("firstName") ?: "",
                lastPeriodDate = snapshot.getString("lastPeriodDate") ?: "",
                cycleLength = snapshot.getLong("cycleLength")?.toInt() ?: 28
            )
        } else {
            null
        }
    }
}
