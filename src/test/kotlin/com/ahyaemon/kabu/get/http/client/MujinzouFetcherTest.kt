package com.ahyaemon.kabu.get.http.client

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class MujinzouFetcherTest {

    @Test
    fun createUrl_2020_1_6() {
        val httpClient = MujinzouClientFactory().mujinzouClient()
        val mujinzouFetcher = MujinzouFetcher(httpClient)
        val offsetDateTime = OffsetDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneOffset.of("+9"))
        val actual = mujinzouFetcher.createUrl(offsetDateTime)

        val expected = "/k_data/2020/20_01/T200106.zip"

        actual shouldBe expected
    }

    // 実際にリクエストが走る
    @Test
    fun get_2020_1_6() {
        val httpClient = MujinzouClientFactory().mujinzouClient()
        val mujinzouFetcher = MujinzouFetcher(httpClient)
        val offsetDateTime = OffsetDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneOffset.of("+9"))
        val actual = mujinzouFetcher.get(offsetDateTime)

        val expected = ClassLoader.getSystemResourceAsStream("T200106.zip")?.readAllBytes()

        actual shouldBe expected
    }
}
