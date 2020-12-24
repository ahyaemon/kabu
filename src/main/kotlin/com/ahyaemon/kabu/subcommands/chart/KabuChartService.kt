package com.ahyaemon.kabu.subcommands.chart

import arrow.core.Either
import arrow.core.flatMap
import com.ahyaemon.kabu.LocalRepositoryImpl
import com.ahyaemon.kabu.extensions.addChild
import com.ahyaemon.kabu.models.KabuDate
import org.slf4j.LoggerFactory
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class KabuChartService(
        private val localRepository: LocalRepositoryImpl
) {

    private val dateFileName = "date.txt"

    fun chart(path: Path): Either<Throwable, Unit> {
        // 何日まで処理済みか取得
        val chartDir = path.addChild("chart")
        val dateTxtPath = chartDir.addChild(dateFileName)
        return localRepository.readDateFile(dateTxtPath)
            .flatMap { savedKabuDate ->
                // csv の一覧を取得
                val csvDir = path.addChild("csv")
                localRepository.ls(csvDir)
                    .map { fileNames -> fileNames.map { fileName -> KabuDate.fromCsvFileName(fileName) } }
                    .map { kabuDates -> kabuDates.filter { kabuDate -> kabuDate.isNewerThan(savedKabuDate) } }
                    .flatMap { kabuDates ->
                        if (kabuDates.isEmpty()) Either.left(NoMoreDateException()) else Either.right(kabuDates)
                    }
            }
            .map { kabuDates ->
                kabuDates.map { kabuDate ->
                    logger.info("{} chart creating...", kabuDate.date())

                    localRepository.readFileAsLines(path.addChild("csv").addChild(kabuDate.csvFileName()))
                        .map { csvLines -> DailyValue.fromCsv(csvLines) }
                        .map { dailyValues ->
                            dailyValues.forEach { dailyValue ->
                                val targetDir = chartDir.addChild("${dailyValue.idWithName}.csv")
                                localRepository.append(dailyValue.toChartString(), targetDir)
                            }
                        }.map { kabuDate }
                    kabuDate
                }
            }
            .map { kabuDates -> kabuDates.reduce { acc, kabuDate -> acc.newer(kabuDate) } }
            .map { kabuDate -> localRepository.save(kabuDate.date(), dateTxtPath) }
            .mapLeft {
                logger.error("failed to create chart", it)
                it
            }
            .map {}
    }

    companion object {
        private val logger = LoggerFactory.getLogger(KabuChartService::class.java)
    }
}
