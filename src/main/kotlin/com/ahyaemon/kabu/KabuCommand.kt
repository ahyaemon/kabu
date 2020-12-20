package com.ahyaemon.kabu

import com.ahyaemon.kabu.subcommands.calc.KabuCalcCommand
import com.ahyaemon.kabu.subcommands.chart.KabuChartCommand
import com.ahyaemon.kabu.subcommands.get.KabuGetCommand
import io.micronaut.configuration.picocli.PicocliRunner

import picocli.CommandLine.Command

@Command(
        name = "kabu",
        description = ["..."],
        mixinStandardHelpOptions = true,
        subcommands = [KabuGetCommand::class, KabuCalcCommand::class, KabuChartCommand::class]
)
class KabuCommand : Runnable {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            PicocliRunner.execute(KabuCommand::class.java, *args)
        }
    }

    override fun run() {
        println("sub commands: get, calc")
    }
}

