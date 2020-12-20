package com.ahyaemon.kabu.subcommands.get

import arrow.core.Either
import arrow.core.flatMap
import com.ahyaemon.kabu.LocalRepository
import com.ahyaemon.kabu.extensions.addChild
import com.ahyaemon.kabu.models.KabuDate
import com.ahyaemon.kabu.subcommands.get.extensions.zipFileName
import com.ahyaemon.kabu.subcommands.get.http.client.MujinzouFetcher
import com.ahyaemon.kabu.subcommands.get.zip.ZipUtil
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.nio.file.Paths
import java.time.OffsetDateTime
import javax.inject.Singleton

@Singleton
class KabuGetService(
    private val mujinzouFetcher: MujinzouFetcher,
    private val localRepository: LocalRepository,
    private val zipUtil: ZipUtil
) {

    fun get(date: KabuDate, dirPath: Path): Either<Throwable, KabuGetResult> {
        return mujinzouFetcher.get(date) // zip 取得
                .flatMap{ zipByteArray ->
                    // zip 保存
                    val zipFilePath = dirPath.addChild("zip").addChild(date.zipFileName())
                    localRepository.save(zipByteArray, zipFilePath)
                }.flatMap { zipFilePath ->
                    // 解凍
                    zipUtil.unzip(zipFilePath)
                }.flatMap { zip ->
                    // 解凍したやつ保存
                    val csvFilePath = dirPath.addChild("csv").addChild(zip.name)
                    localRepository.save(zip.content, csvFilePath)
                }.map {
                    KabuGetResult(it)
                }
    }
}

data class KabuGetResult(
        val csvFilePath: Path
)
