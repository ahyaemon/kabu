package com.ahyaemon.kabu.get

import com.ahyaemon.kabu.get.http.client.MujinzouFetcher
import java.time.OffsetDateTime
import javax.inject.Singleton

@Singleton
class KabuGetService(
        private val mujinzouFetcher: MujinzouFetcher
) {

    fun get(dateTime: OffsetDateTime) {
        // zip 取得
        val zipByteArray = mujinzouFetcher.get(dateTime)

        // zip 保存

        // zip 解凍

        // 解凍したやつ保存

    }
}
