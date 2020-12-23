package com.ahyaemon.kabu.subcommands.adjust

import arrow.core.Either
import com.ahyaemon.kabu.extensions.exceptionToLeft
import com.ahyaemon.kabu.models.KabuDate
import io.kotest.matchers.be
import org.jsoup.Jsoup
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class MergeHtmlParserImpl : MergeHtmlParser {
    // 元データ
    // https://kabu.com/investment/meigara/gensi_s.html

    override fun createCsv(htmlPath: Path): Either<Throwable, String> = exceptionToLeft {
        val document = Jsoup.parse(htmlPath.toFile(), Charsets.UTF_8.displayName())
        val csvBody = document.selectFirst(".tbl01")
            .selectFirst("tbody")
            .select("tr")
            .map { tr ->
                val tds = tr.select("td").map { td -> td.text() }
                MergeInfo(
                    effectiveDate = KabuDate.fromSlashSeparated(tds[0]),
                    code = tds[1],
                    name = tds[2],
                    beforeRatio = createBeforeRatio(tds[3]),
                    lastDayWithRight = KabuDate.fromSlashSeparated(tds[4])
                )
            }.joinToString("\n") { mergeInfo -> mergeInfo.toCsv() }

        MergeInfo.header + "\n" + csvBody
    }
}

fun createBeforeRatio(ratio: String): Double {
    val sp = ratio.replace("株", "").split("→").map { it.toDouble() }
    return sp[0] / sp[1]
}
