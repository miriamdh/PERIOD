package com.g12.periodee.engine

data class PhaseContent(
    val buddyTitle: String,
    val buddyBody: String,
    val sport: List<String>,
    val nutrition: List<String>
)

object TipsEngine {

    fun getContent(phase: String): PhaseContent {
        return when (phase) {

            "MENSTRUAL" -> PhaseContent(
                buddyTitle = "💗 Aujourd’hui, douceur avant tout",
                buddyBody = "Ton corps fait un travail important aujourd’hui. C’est normal de te sentir plus fatiguée ou sensible. Ralentir n’est pas un échec : c’est une forme de respect envers toi‑même.",
                sport = listOf("Marche douce", "Étirements lents", "Yoga relaxant", "Respiration"),
                nutrition = listOf("Repas chauds et réconfortants", "Aliments riches en fer", "Hydratation régulière")
            )

            "FOLLICULAR" -> PhaseContent(
                buddyTitle = "✨ Ton énergie remonte",
                buddyBody = "Ton énergie revient progressivement. C’est un bon moment pour reprendre des habitudes qui te font du bien, à ton rythme.",
                sport = listOf("Cardio léger", "Renforcement doux", "Activités qui stimulent sans épuiser"),
                nutrition = listOf("Fruits & légumes frais", "Protéines", "Repas équilibrés")
            )

            "OVULATION" -> PhaseContent(
                buddyTitle = "🔥 Pic d’énergie",
                buddyBody = "Ton énergie est à son maximum. Tu peux te sentir plus confiante, plus expressive. Profite de cette phase pour faire ce qui te fait vibrer.",
                sport = listOf("Séances plus intenses", "Renforcement + cardio", "Activités dynamiques"),
                nutrition = listOf("Bonnes graisses", "Hydratation", "Repas complets")
            )

            else -> PhaseContent(
                buddyTitle = "🌙 Ralentir est normal",
                buddyBody = "Ton corps commence à ralentir. Tu peux ressentir plus d’émotions ou de fatigue. Ce n’est pas “être faible”, c’est ton cycle naturel.",
                sport = listOf("Marche", "Yoga", "Mobilité douce"),
                nutrition = listOf("Magnésium", "Fibres", "Protéines", "Moins de pression sur toi")
            )
        }
    }

    // optionnel: pour notifications "buddy du jour"
    fun getBuddyMessage(phase: String): Pair<String, String> {
        val c = getContent(phase)
        return c.buddyTitle to c.buddyBody
    }
}
