package com.ahyaemon.kabu.subcommands.get.http.client

import arrow.core.Either
import com.ahyaemon.kabu.models.KabuDate
import com.ahyaemon.kabu.subcommands.get.extensions.mm
import com.ahyaemon.kabu.subcommands.get.extensions.yy
import com.ahyaemon.kabu.subcommands.get.extensions.zipFileName
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import java.time.OffsetDateTime
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MujinzouFetcher(
        @param:Named("mujinzou") private val httpClient: HttpClient
) {
    fun get(date: KabuDate): Either<Throwable, ByteArray> {
        val blockingHttpClient = httpClient.toBlocking()
        return try {
            Either.right(blockingHttpClient.retrieve(
                    HttpRequest.GET<Any>(date.kDataUrl()),
                    ByteArray::class.java
            ))
        } catch (e: Exception) {
            Either.left(e)
        } finally {
            blockingHttpClient.close()
        }
    }
}
