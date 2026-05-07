package com.bsuir.activity_tracker_mobile.core

import android.content.Context
import com.bsuir.activity_tracker_mobile.data.auth.AuthRepository
import com.bsuir.activity_tracker_mobile.data.auth.TokenStore
import com.bsuir.activity_tracker_mobile.data.network.ApiClient
import com.bsuir.activity_tracker_mobile.data.network.api.AuthApi
import com.bsuir.activity_tracker_mobile.data.network.api.MeApi
import com.bsuir.activity_tracker_mobile.data.profile.ProfileRepository
import com.bsuir.activity_tracker_mobile.domain.usecase.auth.ValidateAuthInputUseCase
import com.bsuir.activity_tracker_mobile.domain.usecase.profile.ValidateProfileInputUseCase

object ServiceLocator {
    @Volatile
    private var instance: Services? = null

    fun provide(context: Context): Services {
        return instance ?: synchronized(this) {
            instance ?: create(context.applicationContext).also { instance = it }
        }
    }

    private fun create(appContext: Context): Services {
        val tokenStore = TokenStore(appContext)

        val plainOkHttp = ApiClient.createPlainOkHttpClient()
        val authRetrofit = ApiClient.createRetrofit(plainOkHttp)
        val authApi = authRetrofit.create(AuthApi::class.java)
        val authRepository = AuthRepository(authApi, tokenStore)
        val validateAuthInputUseCase = ValidateAuthInputUseCase(appContext)

        val authedOkHttp = ApiClient.createAuthenticatedOkHttpClient(
            tokenStore = tokenStore,
            authRetrofit = authRetrofit
        )
        val authedRetrofit = ApiClient.createRetrofit(authedOkHttp)
        val meApi = authedRetrofit.create(MeApi::class.java)
        val profileRepository = ProfileRepository(meApi)
        val validateProfileInputUseCase = ValidateProfileInputUseCase(appContext)

        return Services(
            appContext = appContext,
            tokenStore = tokenStore,
            authRepository = authRepository,
            validateAuthInputUseCase = validateAuthInputUseCase,
            profileRepository = profileRepository,
            validateProfileInputUseCase = validateProfileInputUseCase,
            authRetrofit = authRetrofit,
            authedRetrofit = authedRetrofit
        )
    }
}

data class Services(
    val appContext: Context,
    val tokenStore: TokenStore,
    val authRepository: AuthRepository,
    val validateAuthInputUseCase: ValidateAuthInputUseCase,
    val profileRepository: ProfileRepository,
    val validateProfileInputUseCase: ValidateProfileInputUseCase,
    val authRetrofit: retrofit2.Retrofit,
    val authedRetrofit: retrofit2.Retrofit
)

