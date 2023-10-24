package com.example.crypto.ui.serializer

import androidx.datastore.core.Serializer
import com.example.crypto.ui.UserSettings
import com.example.crypto.ui.manager.CryptoManager
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class UserSettingSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<UserSettings> {
    override val defaultValue: UserSettings
        get() = UserSettings()

    override suspend fun readFrom(input: InputStream): UserSettings {
        val decryptedBytes = cryptoManager.decrypt(
            inputStream = input
        )
        return try {
            Json.decodeFromString(
                deserializer = UserSettings.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        val data = Json.encodeToString(
            serializer = UserSettings.serializer(),
            value = t
        )
        cryptoManager.encrypt(
            byteArray = data.encodeToByteArray(),
            outputStream = output
        )
    }


}