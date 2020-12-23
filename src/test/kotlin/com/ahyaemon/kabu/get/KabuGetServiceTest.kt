package com.ahyaemon.kabu.get

import arrow.core.Either
import com.ahyaemon.kabu.LocalRepositoryImpl
import com.ahyaemon.kabu.models.KabuDate
import com.ahyaemon.kabu.subcommands.get.http.client.MujinzouClientFactory
import com.ahyaemon.kabu.subcommands.get.http.client.MujinzouFetcher
import com.ahyaemon.kabu.subcommands.get.zip.ZipUtil
import com.ahyaemon.kabu.subcommands.get.KabuGetResult
import com.ahyaemon.kabu.subcommands.get.KabuGetService
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

internal class KabuGetServiceTest {

    // 実際にリクエストが走る
    @Test
    @Disabled
    fun get() {
        val mujinzouClient = MujinzouClientFactory().httpClient()
        val mujinzouFetcher = MujinzouFetcher(mujinzouClient)
        val localRepository = LocalRepositoryImpl()
        val zipUtil = ZipUtil()
        val kabuGetService = KabuGetService(mujinzouFetcher, localRepository, zipUtil)

        Files.createTempDirectory(Paths.get("/tmp/kabu/zip"), null)
        Files.createTempDirectory(Paths.get("/tmp/kabu/csv"), null)
        val dirPath = Paths.get("/tmp/kabu")

        val date = KabuDate.fromDate("2020-01-06")
        val either = kabuGetService.get(date, dirPath)

        either shouldBe Either.right(KabuGetResult(Paths.get(dirPath.toString(), "csv", date.csvFileName())))
    }

}
