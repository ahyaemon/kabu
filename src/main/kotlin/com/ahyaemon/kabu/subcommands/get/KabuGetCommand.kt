package com.ahyaemon.kabu.subcommands.get

import com.ahyaemon.kabu.models.KabuDate
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
        val date = KabuDate.fromDate(date)
        kabuGetService.get(date, path)
                .map { logger.info("{} succeeded.", date) }
                .mapLeft { logger.error("{} failed. error: {}", date) }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(KabuGetCommand::class.java)
    }
}
