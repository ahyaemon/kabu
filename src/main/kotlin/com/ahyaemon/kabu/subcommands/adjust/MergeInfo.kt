package com.ahyaemon.kabu.subcommands.adjust

import com.ahyaemon.kabu.models.KabuDate

data class MergeInfo(
    // 効力発生日
    val effectiveDate: KabuDate,

    // 銘柄コード
    val code: String,

    // 銘柄名
    val name: String,

    // 併合前比率
    val beforeRatio: Double,

    // 権利付最終日
    val lastDayWithRight: KabuDate
) {

    fun toCsv(): String {
        return "${effectiveDate.hyphenSeparatedDate()},$code,$name,$beforeRatio,${lastDayWithRight.hyphenSeparatedDate()}"
    }

    companion object {
        val header = "効力発生日,銘柄コード,銘柄名,併合前比率,権利付最終日"
    }
}
