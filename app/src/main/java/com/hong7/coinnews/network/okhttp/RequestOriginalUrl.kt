package com.hong7.coinnews.network.okhttp

import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class RequestOriginalUrl {

    fun invoke(redirectedUrl: String): String {
        // todo parse first if error network connect
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(redirectedUrl)
            .header(
                "User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36"
            )
            .addHeader("Cookie", "CONSENT=YES+cb.20220419-08-p0.cs+FX+111")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val htmlContent = response.body?.string() ?: throw IOException("Empty response body")
            val soup: Document = Jsoup.parse(htmlContent)

            val link = soup.selectFirst("a")?.attr("href") ?: "No link found"

            return link
        }
    }
}