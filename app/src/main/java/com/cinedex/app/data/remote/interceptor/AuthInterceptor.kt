package com.cinedex.app.data.remote.interceptor

import com.cinedex.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val token: String = BuildConfig.TMDB_BEARER_TOKEN.takeIf { it.isNotBlank() && it != "null" }
        ?: "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0MDY4ZjhkYjgwYWE3NzExOWFmNTdhNzRlMjEyYzE3ZSIsIm5iZiI6MTc4OTAxMjE1MC40ODg5OTk4LCJzdWIiOiI2YWEyMjhiNmUyY2QxN2QxOGQ2NjgyNDIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.IQhHqtrlfhkxyEnnCpGaR8Z721rx8a6TRj0Jh6eZ_uQ",
    private val apiKey: String = BuildConfig.TMDB_API_KEY.takeIf { it.isNotBlank() && it != "null" }
        ?: "4068f8db80aa77119af57a74e212c17e"
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = if (originalUrl.queryParameter("api_key") == null) {
            originalUrl.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build()
        } else {
            originalUrl
        }

        val requestBuilder = originalRequest.newBuilder()
            .url(newUrl)
            .header("Authorization", "Bearer $token")
            .header("accept", "application/json")

        return chain.proceed(requestBuilder.build())
    }
}
