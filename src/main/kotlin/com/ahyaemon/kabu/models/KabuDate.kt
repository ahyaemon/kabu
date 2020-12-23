package com.ahyaemon.kabu.models

import java.time.OffsetDateTime
import java.time.ZoneOffset

data class KabuDate(
    val dateTime: OffsetDateTime
) {

    fun yy(): String = dateTime.year.toString().substring(2)
    fun mm(): String = dateTime.month.value.toString().padStart(2, '0')
    fun dd(): String = dateTime.dayOfMonth.toString().padStart(2, '0')

    fun date(): String = "${yy()}${mm()}${dd()}"
    fun zipFileName(): String = "T${date()}.zip"
    fun csvFileName(): String = "T${date()}.csv"

    fun kDataUrl(): String = "/k_data/${dateTime.year}/${yy()}_${mm()}/${zipFileName()}"

    fun isNewerThan(k: KabuDate): Boolean = this.dateTime.isAfter(k.dateTime)
    fun newer(k: KabuDate): KabuDate = if (this.isNewerThan(k)) this else k

    fun hyphenSeparatedDate(): String = "${dateTime.year}-${mm()}-${dd()}"

    companion object {

        // date: yyyy-mm-dd
        fun fromDate(date: String, zoneOffset: ZoneOffset = ZoneOffset.of("+9")): KabuDate {
            val sp = date.split("-").map { it.toInt() }
            val offsetDateTime = OffsetDateTime.of(sp[0], sp[1], sp[2], 0, 0, 0, 0, zoneOffset)
            return KabuDate(offsetDateTime)
        }

        // date: yymmdd
        fun fromYYMMDD(date: String, zoneOffset: ZoneOffset = ZoneOffset.of("+9")): KabuDate {
            val sp = listOf("20" + date.substring(0, 2), date.substring(2, 4), date.substring(4, 6)).map { it.toInt() }
            val offsetDateTime = OffsetDateTime.of(sp[0], sp[1], sp[2], 0, 0, 0, 0, zoneOffset)
            return KabuDate(offsetDateTime)
        }

        fun fromSlashSeparated(date: String, zoneOffset: ZoneOffset = ZoneOffset.of("+9")): KabuDate {
            val sp = date.split("/").map { it.toInt() }
            val offsetDateTime = OffsetDateTime.of(sp[0], sp[1], sp[2], 0, 0, 0, 0, zoneOffset)
            return KabuDate(offsetDateTime)
        }

        // date: Tyymmdd.csv
        fun fromCsvFileName(date: String, zoneOffset: ZoneOffset = ZoneOffset.of("+9")): KabuDate {
            return fromYYMMDD(date.substring(1, 7), zoneOffset)
        }
    }
}
