package com.ahyaemon.kabu.subcommands.adjust

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.nio.file.Paths
import javax.inject.Singleton

@CommandLine.Command(name = "adjust", description = ["get kabu adjust data"], mixinStandardHelpOptions = true)
@Singleton
class KabuAdjustCommand(
    private val service: KabuAdjustService
): Runnable {

    @CommandLine.Option(
        names = ["-p", "--path"],
        description = ["target data directory path"],
        required = true
    )
    private var directory: String = "/tmp/data"

    override fun run() {
        val dataDirectoryPath = Paths.get(directory)
        service.getAdjustDate(dataDirectoryPath)
            .mapLeft {
                logger.error("failed to get adjust data", it)
                it
            }
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(KabuAdjustCommand::class.java)
    }
}
