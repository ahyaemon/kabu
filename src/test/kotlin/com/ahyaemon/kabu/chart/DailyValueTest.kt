package com.ahyaemon.kabu.chart

import com.ahyaemon.kabu.subcommands.chart.DailyValue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.nio.charset.Charset
import java.nio.file.Paths
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class DailyValueTest {

    private fun createDate(year: Int, month: Int, day: Int) = OffsetDateTime.of(year, month, day, 0, 0, 0, 0, ZoneOffset.of("+9"))

    @Test
    fun fromCsv_20201207() {
        val csvLines = Paths.get(ClassLoader.getSystemResource("DailyValueTest/T201207.csv").path).toFile().readLines(Charset.forName("Shift-JIS"))

        val dailyValues = DailyValue.fromCsv(csvLines)

        dailyValues.size shouldBe 2
        val nikkei225 = DailyValue(
            date = createDate(2020, 12, 7),
            id = "1001",
            n = "11",
            idWithName = "1001_日経２２５",
            beginValue = 26894.0,
            maxValue = 26894.0,
            minValue = 26500.0,
            endValue = 26547.0,
            volume = 1166470000,
            place = "東証１部"
        )
        dailyValues shouldContain nikkei225
    }

    @Test
    fun fromCsv_20201208() {
        val csvLines = Paths.get(ClassLoader.getSystemResource("DailyValueTest/T201208.csv").path).toFile().readLines(Charset.forName("Shift-JIS"))

        val dailyValues = DailyValue.fromCsv(csvLines)

        dailyValues.size shouldBe 2
        val nikkei225 = DailyValue(
            date = createDate(2020, 12, 8),
            id = "1001",
            n = "11",
            idWithName = "1001_日経２２５",
            beginValue = 26380.0,
            maxValue = 26523.0,
            minValue = 26327.0,
            endValue = 26467.0,
            volume = 1029910000,
            place = "東証１部"
        )
        dailyValues shouldContain nikkei225
    }

    @Test
    fun toChartString_20201207() {
        val nikkei225 = DailyValue(
            date = createDate(2020, 12, 7),
            id = "1001",
            n = "11",
            idWithName = "1001_日経２２５",
            beginValue = 26894.0,
            maxValue = 26894.0,
            minValue = 26500.0,
            endValue = 26547.0,
            volume = 1166470000,
            place = "東証１部"
        )

        nikkei225.toChartString() shouldBe "2020-12-07,26894.0,26894.0,26500.0,26547.0,1166470000"
    }

    @Test
    fun toChartString_20201228() {
        val nikkei225 = DailyValue(
            date = createDate(2020, 12, 28),
            id = "1001",
            n = "11",
            idWithName = "1001_日経２２５",
            beginValue = 26380.0,
            maxValue = 26523.0,
            minValue = 26327.0,
            endValue = 26467.0,
            volume = 1029910000,
            place = "東証１部"
        )

        nikkei225.toChartString() shouldBe "2020-12-28,26380.0,26523.0,26327.0,26467.0,1029910000"
    }
}
