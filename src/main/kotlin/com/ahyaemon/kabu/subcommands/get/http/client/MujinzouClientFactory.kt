package com.ahyaemon.kabu.subcommands.get.http.client

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import org.reactivestreams.Publisher
import java.net.URL
import javax.inject.Singleton

@Factory
class MujinzouClientFactory {

    @Bean
    @Singleton
    fun httpClient(): HttpClient = HttpClient.create(URL("http://mujinzou.com"))
}

@Singleton
class MujinzouHttpClient: HttpClient {

    private val httpClient = HttpClient.create(URL("http://mujinzou.com"))

    override fun isRunning(): Boolean {
        return httpClient.isRunning
    }

    override fun toBlocking(): BlockingHttpClient {
        return httpClient.toBlocking()
    }

    override fun <I : Any?, O : Any?, E : Any?> exchange(request: HttpRequest<I>?, bodyType: Argument<O>?, errorType: Argument<E>?): Publisher<HttpResponse<O>> {
        return httpClient.exchange(request, bodyType)
    }
}
