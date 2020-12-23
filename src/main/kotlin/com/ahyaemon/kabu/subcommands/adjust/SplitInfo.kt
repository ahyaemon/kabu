package com.ahyaemon.kabu.subcommands.adjust

import com.ahyaemon.kabu.models.KabuDate
import io.kotest.matchers.date.after

data class SplitInfo(

    // 割当日
    val allocationDate: KabuDate,

    // 銘柄コード
    val code: String,

    // 銘柄名
    val name: String,

    // 割当後比率
    val afterRatio: Double,

    // 権利付最終日
    val lastDayWithRight: KabuDate,

    // 効力発生日
    val effectiveDate: KabuDate,

    // 売却可能予定日
    val expectedSaleDate: KabuDate
) {
    fun toCsv(): String {
        return "${allocationDate.hyphenSeparatedDate()},$code,$name,$afterRatio,${lastDayWithRight.hyphenSeparatedDate()},${effectiveDate.hyphenSeparatedDate()},${expectedSaleDate.hyphenSeparatedDate()}"
    }

    companion object {
        val header = "割当日,銘柄コード,銘柄名,割当後比率,権利付最終日,効力発生日,売却可能予定日"
    }
}
