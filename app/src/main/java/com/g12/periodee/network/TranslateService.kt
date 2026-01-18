package com.g12.periodee.network

import com.g12.periodee.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder

object TranslateService {

    private const val API_KEY = BuildConfig.TRANSLATE_API_KEY
    private val client = OkHttpClient()

    suspend fun translate(text: String, targetLang: String): String =
        withContext(Dispatchers.IO) {

            if (text.isBlank()) return@withContext text

            val lang = targetLang.lowercase().take(2)
            if (lang == "fr") return@withContext text

            // sécurité : pas de clé → fallback
            if (API_KEY.isBlank() || API_KEY == "null") {
                return@withContext "[${lang.uppercase()}] $text"
            }

            try {
                val url =
                    "https://translation.googleapis.com/language/translate/v2" +
                            "?key=$API_KEY" +
                            "&q=${URLEncoder.encode(text, "UTF-8")}" +
                            "&source=fr" +
                            "&target=$lang"

                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        return@withContext "[${lang.uppercase()}] $text"
                    }

                    val body = response.body?.string().orEmpty()
                    val json = JSONObject(body)

                    json.getJSONObject("data")
                        .getJSONArray("translations")
                        .getJSONObject(0)
                        .getString("translatedText")
                }
            } catch (e: Exception) {
                "[${lang.uppercase()}] $text"
            }
        }
}
