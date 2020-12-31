package com.ahyaemon.kabu.chart

import com.ahyaemon.kabu.LocalRepositoryImpl
import com.ahyaemon.kabu.extensions.addChild
import com.ahyaemon.kabu.subcommands.chart.KabuChartService
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

data class Directories(
    val toDataPath: Path,
    val toChartPath: Path,
)

internal class KabuChartServiceTest {

    private fun createTestDirectories(): Directories {
        val fromDataPath = Paths.get(ClassLoader.getSystemResource("KabuChartServiceTest/data").path)
        val fromCsvPath = fromDataPath.addChild("csv")
        val toDataPath = fromDataPath.addChild(UUID.randomUUID().toString()).also { println(it) }
        val toCsvPath = toDataPath.addChild("csv")
        val toChartPath = toDataPath.addChild("chart")
        Files.createDirectories(toCsvPath)
        Files.createDirectories(toChartPath)
        fromCsvPath.toFile().listFiles()!!.forEach {
            Files.copy(it.toPath(), toCsvPath.addChild(it.name))
        }

        return Directories(
            toDataPath,
            toChartPath,
        )
    }

    @Test
    fun chart_success_new() {
        val localRepository = LocalRepositoryImpl()
        val kabuChartService = KabuChartService(localRepository)

        // test 用 csv を tmp ディレクトリにコピー
        val (toDataPath, toChartPath) = createTestDirectories()
        val result = kabuChartService.chart(toDataPath)

        // ディレクトリの下にファイルが 2 つ
        assertAll (
            { result.isRight() shouldBe true },
            { toChartPath.toFile().listFiles()!!.size shouldBe 3 },
            { toChartPath.addChild("1001_日経２２５.csv").toFile().readLines().size shouldBe 5 },
            { toChartPath.addChild("1002_ＴＯＰＩＸ.csv").toFile().readLines().size shouldBe 5 },
        )
    }

    @Test
    fun chart_success_append_withDate() {
        val localRepository = LocalRepositoryImpl()
        val kabuChartService = KabuChartService(localRepository)

        // test 用 csv を tmp ディレクトリにコピー
        val (toDataPath, toChartPath) = createTestDirectories()

        // charts ディレクトリの下に空のファイルを置いておく
        val nikkeiFile = toChartPath.addChild("1001_日経２２５.csv").toFile()
        nikkeiFile.writeText("")
        val topixFile = toChartPath.addChild("1002_ＴＯＰＩＸ.csv").toFile()
        topixFile.writeText("")

        // ファイルの中身が 3 行になるように date.txt を作っておく
        toChartPath.addChild("date.txt").toFile().writeText("201224")

        val result = kabuChartService.chart(toDataPath)

        assertAll(
            { result.isRight() shouldBe true },
            { toChartPath.toFile().listFiles()!!.size shouldBe 3 },
            { nikkeiFile.readLines().size shouldBe 3 },
            { topixFile.readLines().size shouldBe 3 },
        )
    }
}
