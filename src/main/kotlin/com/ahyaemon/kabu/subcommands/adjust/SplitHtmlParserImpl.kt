package com.ahyaemon.kabu.subcommands.adjust

import arrow.core.Either
import com.ahyaemon.kabu.extensions.exceptionToLeft
import com.ahyaemon.kabu.models.KabuDate
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class SplitHtmlParserImpl : SplitHtmlParser {
    // 元データ
    // https://kabu.com/investment/meigara/bunkatu_s.html

    override fun createCsv(htmlPath: Path): Either<Throwable, String> = exceptionToLeft {
        val document = Jsoup.parse(htmlPath.toFile(), Charsets.UTF_8.displayName())
        val csvBody = document.selectFirst(".tbl01")
            .selectFirst("tbody")
            .select("tr")
            .map { tr ->
                val tds = tr.select("td").map { td -> td.text() }
                SplitInfo(
                    allocationDate = KabuDate.fromSlashSeparated(tds[0]),
                    code = tds[1],
                    name = tds[2],
                    afterRatio = createAfterRatio(tds[3]),
                    lastDayWithRight = KabuDate.fromSlashSeparated(tds[4]),
                    effectiveDate = KabuDate.fromSlashSeparated(tds[5]),
                    expectedSaleDate = KabuDate.fromSlashSeparated(tds[6])
                )
            }
            .joinToString("\n") { it.toCsv() }

        SplitInfo.header + "\n" + csvBody
    }

    companion object {

        private val logger = LoggerFactory.getLogger(SplitHtmlParserImpl::class.java)

        fun createAfterRatio(ratio: String): Double {
            val sp = ratio.split("：")

            if (sp.size < 2) {
                logger.error("invalid ratio: {}", ratio)
            }

            return sp[1].toDouble() / sp[0].toDouble()
        }
    }
}
