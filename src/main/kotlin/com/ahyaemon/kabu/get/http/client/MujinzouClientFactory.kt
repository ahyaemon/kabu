package com.ahyaemon.kabu.get.http.client

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.http.client.HttpClient
import java.net.URL

@Factory
class MujinzouClientFactory {

    @Bean
    fun mujinzouClient(): HttpClient = HttpClient.create(URL("http://mujinzou.com"))
}
