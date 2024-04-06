package com.journiapp.stampapplication.data.network

import android.content.Context
import com.google.gson.Gson
import com.journiapp.stampapplication.common.SharedPrefHelper
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Add authentication to network requests by adding a session cookie.
 *
 * The session cookie is also stored in Shared Preferences.
 */
object NetworkUtil {

    private var okHttpClient: OkHttpClient? = null

    fun providesOkHttp(context: Context): OkHttpClient {
        if (okHttpClient == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor { chain ->

                    val requestBuilder = chain.request().newBuilder()

                    val cookie = SharedPrefHelper(context).loadUserInfoJson()?.sessionCookie
                    if (cookie != null && cookie != "") {
                        requestBuilder.addHeader("Cookie", cookie)
                    }
                    requestBuilder.addHeader("User-Agent", "Coding Challenge/1.0.0 (Android)")

                    chain.connection()
                    chain.proceed(requestBuilder.build())
                }
                .build()
        }
        return okHttpClient!!
    }

    fun providesRetrofit(context: Context): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .client(providesOkHttp(context))
            .addConverterFactory(GsonConverterFactory.create(Gson())).build()
    }

    fun getCookieString(headers: Headers): String? {
        return headers.get("Set-Cookie")?.let {
            it.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        }
    }

    val BASE_API_URL = "https://www.journiapp.com"
}