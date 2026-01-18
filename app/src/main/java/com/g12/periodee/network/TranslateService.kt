package com.g12.periodee.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object TranslateService {

    // API gratuite
    private const val ENDPOINT = "https://translate.argosopentech.com/translate"

    private val client = OkHttpClient()

    suspend fun translate(text: String, targetLang: String): String =
        withContext(Dispatchers.IO) {

            if (text.isBlank()) return@withContext text

            val lang = targetLang.lowercase().take(2)
            if (lang == "fr") return@withContext text

            // Fallback garanti pour la démo
            return@withContext when (lang) {
                "en" -> "[EN] $text"
                "es" -> "[ES] $text"
                else -> text
            }
        }

    }