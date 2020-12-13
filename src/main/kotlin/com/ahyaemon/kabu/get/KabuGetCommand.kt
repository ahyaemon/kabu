package com.ahyaemon.kabu.get

import arrow.core.extensions.either.monad.forEffect
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import picocli.CommandLine.Option
import java.nio.file.Paths
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Singleton

@CommandLine.Command(name = "get", description = ["get kabu csv data"], mixinStandardHelpOptions = true)
@Singleton
class KabuGetCommand(
        private val kabuGetService: KabuGetService
): Runnable {

    @Option(
            names = ["-p", "--path"],
            description = ["target directory path"],
            required = true
    )
    private var directory: String = "/tmp"

    @Option(
            names = ["-d", "--date"],
            description = ["date: uuuu-MM-dd"],
            required = true
    )
    private var date: String = "2020-01-06"

    override fun run() {
        val path = Paths.get(directory)
        val sp = date.split("-").map { it.toInt() }
        val offsetDateTime = OffsetDateTime.of(sp[0], sp[1], sp[2], 0, 0, 0, 0, ZoneOffset.of("+9"))
        kabuGetService.get(offsetDateTime, path)
                .map { logger.info("kabu get succeeded. csv: {}", it.csvFilePath) }
                .mapLeft { logger.error("kabu get failed", it) }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(KabuGetCommand::class.java)
    }
}
