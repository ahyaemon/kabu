package com.ahyaemon.kabu

import com.ahyaemon.kabu.subcommands.chart.KabuChartCommand
import com.ahyaemon.kabu.subcommands.get.KabuGetCommand
import com.ahyaemon.kabu.subcommands.adjust.KabuAdjustCommand
import io.micronaut.configuration.picocli.PicocliRunner

import picocli.CommandLine.Command

@Command(
        name = "kabu",
        description = ["..."],
        mixinStandardHelpOptions = true,
        subcommands = [
            KabuGetCommand::class,
            KabuChartCommand::class,
            KabuAdjustCommand::class
        ]
)
class KabuCommand : Runnable {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            PicocliRunner.execute(KabuCommand::class.java, *args)
        }
    }

    override fun run() {
        println("sub commands: get, chart, adjust")
    }
}

