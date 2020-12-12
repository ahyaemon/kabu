package com.ahyaemon.kabu.get.http.client

import arrow.core.Either
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import java.time.OffsetDateTime
import javax.inject.Singleton

@Singleton
class MujinzouFetcher(
        private val mujinzouClient: HttpClient
) {
    fun get(dateTime: OffsetDateTime): Either<Throwable, ByteArray> {
        return try {
            Either.right(mujinzouClient
                    .toBlocking()
                    .retrieve(
                            HttpRequest.GET<Any>(createUrl(dateTime)),
                            ByteArray::class.java
                    ))
        } catch (e: Exception) {
            Either.left(e)
        }
    }

    fun createUrl(dateTime: OffsetDateTime): String = "/k_data/${dateTime.year}/${dateTime.yy()}_${dateTime.mm()}/T${dateTime.yy()}${dateTime.mm()}${dateTime.dd()}.zip"

    private fun OffsetDateTime.yy() = this.year.toString().substring(2)
    private fun OffsetDateTime.mm() = this.month.value.toString().padStart(2, '0')
    private fun OffsetDateTime.dd() = this.dayOfMonth.toString().padStart(2, '0')
}

