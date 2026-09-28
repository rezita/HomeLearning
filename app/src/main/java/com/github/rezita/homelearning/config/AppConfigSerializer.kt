package com.github.rezita.homelearning.config

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object AppConfigSerializer : Serializer<AppConfig> {

    override val defaultValue: AppConfig =
        AppConfig.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppConfig =
        try {
            AppConfig.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException(
                "Cannot read AppConfig.",
                exception,
            )
        }

    override suspend fun writeTo(
        t: AppConfig,
        output: OutputStream,
    ) {
        t.writeTo(output)
    }
}