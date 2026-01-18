package com.g12.periodee.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object TranslateService {

    // API gratuite (sans clé)
    private const val ENDPOINT = "https://translate.argosopentech.com/translate"

    private val client = OkHttpClient()

    suspend fun translate(text: String, targetLang: String): String = withContext(Dispatchers.IO) {
        try {
            if (text.isBlank()) return@withContext text

            val safeTarget = targetLang.lowercase()
            if (safeTarget == "fr") return@withContext text

            val json = JSONObject().apply {
                put("q", text)
                put("source", "fr")
                put("target", safeTarget)
                put("format", "text")
            }

            val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url(ENDPOINT)
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext text

                val raw = response.body?.string().orEmpty()
                val translated = JSONObject(raw).optString("translatedText", "")
                if (translated.isBlank()) text else translated
            }
        } catch (e: Exception) {
            // Fallback
            text
        }
    }
}
