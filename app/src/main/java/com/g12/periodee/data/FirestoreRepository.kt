package com.g12.periodee.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val USER_ID = "main"

    //User
    fun saveUser(
        firstName: String,
        cycleLength: Int,
        lastPeriodDate: String
    ) {
        val userData = hashMapOf(
            "firstName" to firstName,
            "cycleLength" to cycleLength,
            "lastPeriodDate" to lastPeriodDate
        )
        db.collection("users").document(USER_ID).set(userData)
    }

    suspend fun getUser(): User? {
        val snapshot = db.collection("users").document(USER_ID).get().await()
        return if (snapshot.exists()) {
            User(
                firstName = snapshot.getString("firstName") ?: "",
                lastPeriodDate = snapshot.getString("lastPeriodDate") ?: "",
                cycleLength = snapshot.getLong("cycleLength")?.toInt() ?: 28
            )
        } else null
    }

    //Cycle histoiry
    suspend fun addCycleToHistory(entry: CycleHistoryEntry) {
        db.collection("users")
            .document(USER_ID)
            .collection("cycle_history")
            .add(entry)
            .await()
    }

    suspend fun getCycleHistory(): List<CycleHistoryEntry> {
        val snapshot = db.collection("users")
            .document(USER_ID)
            .collection("cycle_history")
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(CycleHistoryEntry::class.java)?.copy(id = doc.id)
        }
    }

    suspend fun deleteCycle(entryId: String) {
        db.collection("users")
            .document(USER_ID)
            .collection("cycle_history")
            .document(entryId)
            .delete()
            .await()
    }

    suspend fun updateCycle(
        entryId: String,
        newPeriodStart: String,
        newCycleLength: Int
    ) {
        val data = mapOf(
            "periodStart" to newPeriodStart,
            "cycleLength" to newCycleLength
        )

        db.collection("users")
            .document(USER_ID)
            .collection("cycle_history")
            .document(entryId)
            .update(data)
            .await()
    }
}
