package com.ahyaemon.kabu.get

import com.ahyaemon.kabu.get.http.client.MujinzouFetcher
import java.time.OffsetDateTime
import javax.inject.Singleton

@Singleton
class KabuGetService(
        private val mujinzouFetcher: MujinzouFetcher
) {

    fun get(dateTime: OffsetDateTime) {
        val response = mujinzouFetcher.get(dateTime)
        println(response)
    }
}
