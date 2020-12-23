package com.ahyaemon.kabu.subcommands.adjust

import arrow.core.getOrElse
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class SplitHtmlParserTest {

    @Test
    fun createCsv() {
        val htmlPath = Paths.get(ClassLoader.getSystemResource("split/split.html").path)
        val parser = SplitHtmlParserImpl()
        val csv = parser.createCsv(htmlPath).getOrElse { "error" }

        val header = csv.split("\n")[0]
        header shouldContain "効力発生日"
        header shouldContain "割当後比率"

        val line1 = csv.split("\n")[1]
        line1 shouldContain "3107"
        line1 shouldContain "5"
        line1 shouldContain "2021-03-31"
    }

    @Test
    fun createAfterRatio() {
        val afterRatio = SplitHtmlParserImpl.createAfterRatio("1：1.3")
        afterRatio shouldBeGreaterThan 1.2
        afterRatio shouldBeLessThan 1.4
    }
}
