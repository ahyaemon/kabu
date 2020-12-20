package com.ahyaemon.kabu.get.http.client

import arrow.core.Either
import com.ahyaemon.kabu.models.KabuDate
import com.ahyaemon.kabu.subcommands.get.http.client.MujinzouClientFactory
import com.ahyaemon.kabu.subcommands.get.http.client.MujinzouFetcher
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class MujinzouFetcherTest {

    // 実際にリクエストが走る
    @Test
    @Disabled
    fun get_2020_1_6() {
        val httpClient = MujinzouClientFactory().httpClient()
        val mujinzouFetcher = MujinzouFetcher(httpClient)
        val date = KabuDate.fromDate("2020-01-06")
        val actual = mujinzouFetcher.get(date)

        val expected = ClassLoader.getSystemResourceAsStream("T200106.zip")?.readAllBytes()

        actual shouldBe expected
    }

    // リクエストはモック
    @Test
    fun get_2020_1_6_mockRequest() {
        val httpClient = mockk<HttpClient>()
        val zipByteArray = ClassLoader.getSystemResourceAsStream("T200106.zip")?.readAllBytes()
        val blockingHttpClient = mockk<BlockingHttpClient>()
        every { httpClient.toBlocking() } returns blockingHttpClient
        every { blockingHttpClient.retrieve(any<HttpRequest<Any>>(), ByteArray::class.java) } returns zipByteArray
        every { blockingHttpClient.close() } returns Unit

        val mujinzouFetcher = MujinzouFetcher(httpClient)
        val date = KabuDate.fromDate("2020-01-06")
        val actual = mujinzouFetcher.get(date)

        actual shouldBe Either.right(zipByteArray)
    }

    @Test
    fun get_failure() {
        val httpClient = mockk<HttpClient>()
        val blockingHttpClient = mockk<BlockingHttpClient>()
        every { httpClient.toBlocking() } returns blockingHttpClient
        val error = RuntimeException("ERROR!!")
        every { blockingHttpClient.retrieve(any<HttpRequest<Any>>(), ByteArray::class.java) } throws error
        every { blockingHttpClient.close() } returns Unit

        val mujinzouFetcher = MujinzouFetcher(httpClient)
        val date = KabuDate.fromDate("2020-01-06")
        val actual = mujinzouFetcher.get(date)

        actual shouldBe Either.left(error)
    }
}
