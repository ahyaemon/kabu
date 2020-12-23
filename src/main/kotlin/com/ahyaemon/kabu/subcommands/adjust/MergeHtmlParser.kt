package com.ahyaemon.kabu.subcommands.adjust

import arrow.core.Either
import java.nio.file.Path

interface MergeHtmlParser {

    fun createCsv(htmlPath: Path): Either<Throwable, String>
}


