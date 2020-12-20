package com.ahyaemon.kabu.subcommands.chart

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import picocli.CommandLine.Option
import java.nio.file.Paths
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Singleton

@CommandLine.Command(name = "chart", description = ["日毎の csv から銘柄ごとのチャートを作る"], mixinStandardHelpOptions = true)
@Singleton
class KabuChartCommand(
        private val kabuChartService: KabuChartService
): Runnable {

    @Option(
            names = ["-p", "--path"],
            description = ["target directory path"],
            required = true
    )
    private var directory: String = "/tmp"

    override fun run() {
        val path = Paths.get(directory)
        kabuChartService.chart(path)
    }
}
