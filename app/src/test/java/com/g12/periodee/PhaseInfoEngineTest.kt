package com.g12.periodee

import com.g12.periodee.engine.PhaseInfoEngine

import org.junit.Assert.assertEquals
import org.junit.Test
class PhaseInfoEngineTest {

    @Test
    fun day_3_should_be_menstrual_phase() {
        val phase = PhaseInfoEngine.getPhase(3)
        assertEquals("MENSTRUAL", phase)
    }

    @Test
    fun day_14_should_be_ovulation_phase() {
        val phase = PhaseInfoEngine.getPhase(14)
        assertEquals("OVULATION", phase)
    }
}
