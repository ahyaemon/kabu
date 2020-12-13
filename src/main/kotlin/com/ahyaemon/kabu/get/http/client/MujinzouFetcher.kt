package com.ahyaemon.kabu.get.http.client

import arrow.core.Either
import com.ahyaemon.kabu.get.extensions.mm
import com.ahyaemon.kabu.get.extensions.yy
import com.ahyaemon.kabu.get.extensions.zipFileName
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import java.time.OffsetDateTime
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MujinzouFetcher(
        @param:Named("mujinzou") private val httpClient: HttpClient
) {
    fun get(dateTime: OffsetDateTime): Either<Throwable, ByteArray> {
        return try {
            Either.right(httpClient
                    .toBlocking()
                    .retrieve(
                            HttpRequest.GET<Any>(createUrl(dateTime)),
                            ByteArray::class.java
                    ))
        } catch (e: Exception) {
            Either.left(e)
        }
    }

    fun createUrl(dateTime: OffsetDateTime): String = "/k_data/${dateTime.year}/${dateTime.yy()}_${dateTime.mm()}/${dateTime.zipFileName()}"
}

