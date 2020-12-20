package com.ahyaemon.kabu.subcommands.calc

import io.micronaut.configuration.picocli.PicocliRunner
import picocli.CommandLine
import javax.inject.Singleton

@CommandLine.Command(name = "calc", description = ["calculate correlation"], mixinStandardHelpOptions = true)
@Singleton
class KabuCalcCommand : Runnable {
    override fun run() {
        println("kabu calc")
    }
}
