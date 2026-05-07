package com.bsuir.activity_tracker_mobile.data.auth

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.time.OffsetDateTime
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import androidx.core.content.edit

class TokenStore(context: Context) {
    private val appContext = context.applicationContext

    private val prefs: SharedPreferences = appContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val secretKey: SecretKey by lazy { getOrCreateSecretKey(KEY_ALIAS) }

    fun getTokensOrNull(): AuthTokens? {
        val access = getDecryptedOrNull(KEY_ACCESS) ?: return null
        val refresh = getDecryptedOrNull(KEY_REFRESH) ?: return null
        val tokenType = getDecryptedOrNull(KEY_TOKEN_TYPE) ?: TOKEN_TYPE_BEARER
        val accessExp = getDecryptedOrNull(KEY_ACCESS_EXP) ?: return null
        val refreshExp = getDecryptedOrNull(KEY_REFRESH_EXP) ?: return null

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
        prefs.edit {
            putString(KEY_ACCESS, encrypt(tokens.accessToken))
                .putString(KEY_REFRESH, encrypt(tokens.refreshToken))
                .putString(KEY_TOKEN_TYPE, encrypt(tokens.tokenType))
                .putString(KEY_ACCESS_EXP, encrypt(tokens.accessTokenExpiresAt.toString()))
                .putString(KEY_REFRESH_EXP, encrypt(tokens.refreshTokenExpiresAt.toString()))
        }
    }

    fun clear() {
        prefs.edit { clear() }
    }

    private fun getDecryptedOrNull(key: String): String? {
        val encoded = prefs.getString(key, null) ?: return null
        return try {
            decrypt(encoded)
        } catch (_: Exception) {
            null
        }
    }

    private fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val iv = cipher.iv
        val cipherText = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        val out = ByteArray(iv.size + cipherText.size)
        System.arraycopy(iv, 0, out, 0, iv.size)
        System.arraycopy(cipherText, 0, out, iv.size, cipherText.size)

        return Base64.encodeToString(out, Base64.NO_WRAP)
    }

    private fun decrypt(encoded: String): String {
        val data = Base64.decode(encoded, Base64.NO_WRAP)
        require(data.size > GCM_IV_LENGTH_BYTES) { "Invalid encrypted payload" }

        val iv = data.copyOfRange(0, GCM_IV_LENGTH_BYTES)
        val cipherText = data.copyOfRange(GCM_IV_LENGTH_BYTES, data.size)

        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val plain = cipher.doFinal(cipherText)
        return plain.toString(Charsets.UTF_8)
    }

    private fun getOrCreateSecretKey(alias: String): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }

        val existingKey = keyStore.getKey(alias, null) as? SecretKey
        if (existingKey != null) return existingKey

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE
        )

        val spec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(AES_KEY_SIZE_BITS)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    companion object {
        private const val PREFS_NAME = "secure_auth_tokens"
        private const val KEY_ALIAS = "auth_tokens_aes_key"

        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val KEY_TOKEN_TYPE = "token_type"
        private const val KEY_ACCESS_EXP = "access_expires_at"
        private const val KEY_REFRESH_EXP = "refresh_expires_at"

        const val TOKEN_TYPE_BEARER = "Bearer"

        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
        private const val AES_KEY_SIZE_BITS = 256
        private const val GCM_TAG_LENGTH_BITS = 128
        private const val GCM_IV_LENGTH_BYTES = 12
    }
}

