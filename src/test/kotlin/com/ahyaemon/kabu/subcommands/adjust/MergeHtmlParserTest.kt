package com.ahyaemon.kabu.subcommands.adjust

import arrow.core.getOrElse
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class MergeHtmlParserTest {

    @Test
    fun createCsv() {
        val htmlPath = Paths.get(ClassLoader.getSystemResource("merge/merge.html").path)
        val parser = MergeHtmlParserImpl()
        val csv = parser.createCsv(htmlPath).getOrElse { "error" }

        val header = csv.split("\n")[0]
        header shouldContain "効力発生日"
        header shouldContain "併合前比率"

        val line1 = csv.split("\n")[1]
        line1 shouldContain "2060"
        line1 shouldContain "フィード・ワン"
        line1 shouldContain "2020-09-28"
    }

    @Test
    fun createBeforeRatio() {
        val ratio = createBeforeRatio("10株→1株")
        ratio shouldBeGreaterThan 9.9
        ratio shouldBeLessThan 10.1
    }
}
