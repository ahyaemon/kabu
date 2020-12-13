package com.ahyaemon.kabu.get.extensions

import java.time.OffsetDateTime

fun OffsetDateTime.yy() = year.toString().substring(2)
fun OffsetDateTime.mm() = month.value.toString().padStart(2, '0')
fun OffsetDateTime.dd() = dayOfMonth.toString().padStart(2, '0')

fun OffsetDateTime.zipFileName() = "T${yy()}${mm()}${dd()}.zip"
fun OffsetDateTime.csvFileName() = zipFileName().replace(".zip", ".csv")
