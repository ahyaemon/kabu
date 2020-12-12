package com.ahyaemon.kabu.get

import picocli.CommandLine
import javax.inject.Singleton

@CommandLine.Command(name = "get", description = ["get kabu csv data"], mixinStandardHelpOptions = true)
@Singleton
class KabuGetCommand(
        private val kabuGetService: KabuGetService
): Runnable {
    override fun run() {
    }
}
