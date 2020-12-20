package com.ahyaemon.kabu.models

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class KabuDateTest {

    @Test
    fun fromDate() {
        val kabuDate = KabuDate.fromDate("2020-12-24")
        val offsetDateTime = OffsetDateTime.of(2020, 12, 24, 0, 0, 0, 0, ZoneOffset.of("+9"))

        kabuDate.dateTime shouldBe offsetDateTime
    }

    @Test
    fun fromYYYYMMDD() {
        val kabuDate = KabuDate.fromYYMMDD("201224")
        val offsetDateTime = OffsetDateTime.of(2020, 12, 24, 0, 0, 0, 0, ZoneOffset.of("+9"))

        kabuDate.dateTime shouldBe offsetDateTime
    }

    @Test
    fun fromCsvFileName() {
        val kabuDate = KabuDate.fromCsvFileName("T201224.csv")
        val offsetDateTime = OffsetDateTime.of(2020, 12, 24, 0, 0, 0, 0, ZoneOffset.of("+9"))

        kabuDate.dateTime shouldBe offsetDateTime
    }
}
