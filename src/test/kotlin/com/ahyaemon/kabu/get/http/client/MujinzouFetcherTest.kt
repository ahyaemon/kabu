package com.ahyaemon.kabu.get.http.client

import arrow.core.Either
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class MujinzouFetcherTest {

    @Test
    fun createUrl_2020_1_6() {
        val httpClient = MujinzouClientFactory().httpClient()
        val mujinzouFetcher = MujinzouFetcher(httpClient)
        val offsetDateTime = OffsetDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneOffset.of("+9"))
        val actual = mujinzouFetcher.createUrl(offsetDateTime)

        val expected = "/k_data/2020/20_01/T200106.zip"

        actual shouldBe expected
    }

    // 実際にリクエストが走る
    @Test
    @Disabled
    fun get_2020_1_6() {
        val httpClient = MujinzouClientFactory().httpClient()
        val mujinzouFetcher = MujinzouFetcher(httpClient)
        val offsetDateTime = OffsetDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneOffset.of("+9"))
        val actual = mujinzouFetcher.get(offsetDateTime)

        val expected = ClassLoader.getSystemResourceAsStream("T200106.zip")?.readAllBytes()

        actual shouldBe expected
    }

    // リクエストはモック
    @Test
    fun get_2020_1_6_mockRequest() {
        val httpClient = mockk<HttpClient>()
        val zipByteArray = ClassLoader.getSystemResourceAsStream("T200106.zip")?.readAllBytes()

        every {
            httpClient.toBlocking().retrieve(any<HttpRequest<Any>>(), ByteArray::class.java)
        } returns zipByteArray
        val mujinzouFetcher = MujinzouFetcher(httpClient)
        val offsetDateTime = OffsetDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneOffset.of("+9"))
        val actual = mujinzouFetcher.get(offsetDateTime)

        actual shouldBe Either.right(zipByteArray)
    }

    @Test
    fun get_failure() {
        val httpClient = mockk<HttpClient>()
        val error = RuntimeException("ERROR!!")
        every {
            httpClient.toBlocking().retrieve(any<HttpRequest<Any>>(), ByteArray::class.java)
        } throws error
        val mujinzouFetcher = MujinzouFetcher(httpClient)
        val offsetDateTime = OffsetDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneOffset.of("+9"))
        val actual = mujinzouFetcher.get(offsetDateTime)

        actual shouldBe Either.left(error)
    }
}
