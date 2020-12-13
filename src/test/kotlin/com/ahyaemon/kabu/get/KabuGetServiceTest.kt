package com.ahyaemon.kabu.get

import arrow.core.Either
import com.ahyaemon.kabu.LocalRepository
import com.ahyaemon.kabu.get.extensions.csvFileName
import com.ahyaemon.kabu.get.http.client.MujinzouClientFactory
import com.ahyaemon.kabu.get.http.client.MujinzouFetcher
import com.ahyaemon.kabu.get.zip.ZipUtil
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class KabuGetServiceTest {

    // 実際にリクエストが走る
    @Test
    fun get() {
        val mujinzouClient = MujinzouClientFactory().httpClient()
        val mujinzouFetcher = MujinzouFetcher(mujinzouClient)
        val localRepository = LocalRepository()
        val zipUtil = ZipUtil()
        val kabuGetService = KabuGetService(mujinzouFetcher, localRepository, zipUtil)

        val offsetDateTime = OffsetDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneOffset.of("+9"))
        val dirPath = Paths.get("/tmp")

        val either = kabuGetService.get(offsetDateTime, dirPath)

        either shouldBe Either.right(KabuGetResult(Paths.get(dirPath.toString(), offsetDateTime.csvFileName())))
    }

}
