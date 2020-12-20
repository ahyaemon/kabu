package com.ahyaemon.kabu.subcommands.chart

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class DailyValue (
    val date: OffsetDateTime,
    val id: String,
    val n: String,
    val idWithName: String,
    val beginValue: Double,
    val maxValue: Double,
    val minValue: Double,
    val endValue: Double,
    val volume: Int,
    val place: String
) {

    fun toChartString(): String {
        val d = date.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"))
        return "$d,$beginValue,$maxValue,$minValue,$endValue,$volume"
    }

    companion object {
        fun fromCsv(csvLines: List<String>): List<DailyValue> =
            csvLines
                .filter { it.isNotEmpty() }
                .map { line -> fromCsvLine(line) }


        fun fromCsvLine(csvLine: String): DailyValue {
            val sp = csvLine.split(",")
            val dateSp = sp[0].split("/").map { it.toInt() }
            return DailyValue(
                date = OffsetDateTime.of(dateSp[0], dateSp[1], dateSp[2], 0, 0, 0, 0, ZoneOffset.of("+9")),
                id = sp[1],
                n = sp[2],
                idWithName = sp[3].replace(" ", "_"),
                beginValue = sp[4].toDouble(),
                maxValue = sp[5].toDouble(),
                minValue = sp[6].toDouble(),
                endValue = sp[7].toDouble(),
                volume = sp[8].toInt(),
                place = sp[9]
            )
        }
    }
}
