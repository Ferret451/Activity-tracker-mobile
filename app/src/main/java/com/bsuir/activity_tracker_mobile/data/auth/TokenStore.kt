package com.bsuir.activity_tracker_mobile.data.auth

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.time.OffsetDateTime

class TokenStore(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getTokensOrNull(): AuthTokens? {
        val access = prefs.getString(KEY_ACCESS, null) ?: return null
        val refresh = prefs.getString(KEY_REFRESH, null) ?: return null
        val tokenType = prefs.getString(KEY_TOKEN_TYPE, TOKEN_TYPE_BEARER) ?: TOKEN_TYPE_BEARER
        val accessExp = prefs.getString(KEY_ACCESS_EXP, null) ?: return null
        val refreshExp = prefs.getString(KEY_REFRESH_EXP, null) ?: return null

        return try {
            AuthTokens(
                accessToken = access,
                refreshToken = refresh,
                tokenType = tokenType,
                accessTokenExpiresAt = OffsetDateTime.parse(accessExp),
                refreshTokenExpiresAt = OffsetDateTime.parse(refreshExp)
            )
        } catch (_: Exception) {
            null
        }
    }

    fun saveTokens(tokens: AuthTokens) {
        prefs.edit()
            .putString(KEY_ACCESS, tokens.accessToken)
            .putString(KEY_REFRESH, tokens.refreshToken)
            .putString(KEY_TOKEN_TYPE, tokens.tokenType)
            .putString(KEY_ACCESS_EXP, tokens.accessTokenExpiresAt.toString())
            .putString(KEY_REFRESH_EXP, tokens.refreshTokenExpiresAt.toString())
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "secure_auth_tokens"
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val KEY_TOKEN_TYPE = "token_type"
        private const val KEY_ACCESS_EXP = "access_expires_at"
        private const val KEY_REFRESH_EXP = "refresh_expires_at"

        const val TOKEN_TYPE_BEARER = "Bearer"
    }
}

