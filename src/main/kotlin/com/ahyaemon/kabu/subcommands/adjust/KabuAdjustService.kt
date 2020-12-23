package com.ahyaemon.kabu.subcommands.adjust

import arrow.core.Either
import java.nio.file.Path

interface KabuAdjustService {
    fun getAdjustDate(dataDirectoryPath: Path): Either<Throwable, Unit>
}
