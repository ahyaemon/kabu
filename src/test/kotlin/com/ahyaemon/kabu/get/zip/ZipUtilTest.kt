package com.ahyaemon.kabu.get.zip

import arrow.core.Either
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class ZipUtilTest {

    @Test
    fun zip_success() {
        val zip = ZipUtil()
        val path = Paths.get(ClassLoader.getSystemResource("hoge.zip").path)

        val either = zip.unzip(path)

        either.isRight() shouldBe true
        either shouldBe Either.right(Zip("hoge.csv", "hoge\n".toByteArray()))
    }
}
