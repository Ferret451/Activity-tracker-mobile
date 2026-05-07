package com.bsuir.activity_tracker_mobile.data.network

import com.bsuir.activity_tracker_mobile.BuildConfig
import com.bsuir.activity_tracker_mobile.data.auth.TokenStore
import com.bsuir.activity_tracker_mobile.data.network.auth.AuthHeaderInterceptor
import com.bsuir.activity_tracker_mobile.data.network.auth.TokenRefreshAuthenticator
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private val gson: Gson = GsonBuilder()
        .disableHtmlEscaping()
        .create()

    fun createPlainOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    fun createAuthenticatedOkHttpClient(
        tokenStore: TokenStore,
        authRetrofit: Retrofit
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(AuthHeaderInterceptor(tokenStore))
            .addInterceptor(logging)
            .authenticator(TokenRefreshAuthenticator(tokenStore, authRetrofit))
            .build()
    }

    fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}

