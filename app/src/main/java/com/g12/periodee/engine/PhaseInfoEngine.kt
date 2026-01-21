package com.g12.periodee.engine

object PhaseInfoEngine {

    fun getPhase(dayOfCycle: Int): String = when {
        dayOfCycle <= 5 -> "MENSTRUAL"
        dayOfCycle <= 13 -> "FOLLICULAR"
        dayOfCycle <= 16 -> "OVULATION"
        else -> "LUTEAL"
    }

    fun getLongText(phase: String): String = when (phase) {
        "MENSTRUAL" ->
            "Pendant les règles, ton corps élimine la muqueuse utérine. " +
                    "Il est normal de ressentir de la fatigue, des douleurs ou une plus grande sensibilité émotionnelle. " +
                    "Cette phase invite naturellement au repos et à l’introspection. " +
                    "Prendre soin de toi ici aide ton corps à mieux repartir ensuite."

        "FOLLICULAR" ->
            "Après les règles, ton corps se régénère progressivement. " +
                    "L’énergie revient, la concentration s’améliore et la motivation augmente. " +
                    "C’est une phase idéale pour organiser, apprendre et démarrer de nouvelles choses, " +
                    "sans te mettre la pression de tout réussir."

        "OVULATION" ->
            "Autour de l’ovulation, les hormones favorisent l’énergie, la confiance et l’expression. " +
                    "Tu peux te sentir plus sociable et plus dynamique. " +
                    "C’est une phase propice à l’action, à la communication et aux activités physiques, " +
                    "tout en restant à l’écoute de tes limites."

        "LUTEAL" ->
            "La phase lutéale prépare le corps à un nouveau cycle. " +
                    "L’énergie peut diminuer et les émotions être plus intenses. " +
                    "Ce n’est pas un défaut, mais un signal pour ralentir, trier, et prendre soin de ton équilibre. " +
                    "Être plus douce avec toi-même est essentiel pendant cette phase."

        else -> "Information indisponible."
    }
}
