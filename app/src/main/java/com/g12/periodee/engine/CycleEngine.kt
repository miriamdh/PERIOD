package com.g12.periodee.engine

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.temporal.ChronoUnit

object CycleEngine {

    private val formatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE)

    // Jour actuel du cycle (1 → cycleLength)
    fun getDayOfCycle(lastPeriod: String, cycleLength: Int): Int {
        val lastDate = LocalDate.parse(lastPeriod, formatter)
        val today = LocalDate.now()
        val days = ChronoUnit.DAYS.between(lastDate, today).toInt()
        return (days % cycleLength) + 1
    }

    // Phase du cycle
    fun getPhase(lastPeriod: String, cycleLength: Int): String {
        val day = getDayOfCycle(lastPeriod, cycleLength)
        return when {
            day <= 5 -> "Règles"
            day <= 13 -> "Phase folliculaire"
            day <= 16 -> "Ovulation"
            else -> "Phase lutéale"
        }
    }

    // Prochaine date des règles
    fun getNextPeriodDate(lastPeriod: String, cycleLength: Int): LocalDate {
        val lastDate = LocalDate.parse(lastPeriod, formatter)
        return lastDate.plusDays(cycleLength.toLong())
    }

    //Date du rappel J‑1 (veille des règles)
    fun getReminderDate(lastPeriod: String, cycleLength: Int): LocalDate {
        val next = getNextPeriodDate(lastPeriod, cycleLength)
        return next.minusDays(1)
    }
}
