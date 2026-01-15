package com.g12.periodee.engine

object TipsEngine {

    fun getTips(phase: String): List<String> {
        return when (phase) {
            "Règles" -> listOf(
                "Repose‑toi et privilégie le sommeil",
                "Mange des aliments riches en fer",
                "Évite le sport intense"
            )

            "Phase folliculaire" -> listOf(
                "C’est le bon moment pour commencer de nouveaux projets",
                "Fais du cardio léger",
                "Mange plus de fruits et légumes"
            )

            "Ovulation" -> listOf(
                "Ton énergie est au maximum",
                "Bon moment pour le sport et la créativité",
                "Bois beaucoup d’eau"
            )

            else -> listOf(
                "Réduis le stress",
                "Favorise le magnésium",
                "Écoute ton corps"
            )
        }
    }
}
