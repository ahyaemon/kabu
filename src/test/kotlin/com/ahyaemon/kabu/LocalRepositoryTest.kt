package com.ahyaemon.kabu

import arrow.core.Either
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths

internal class LocalRepositoryTest {

    @Test
    fun save_success() {
        val localRepository = LocalRepository()
        val byteArray = "abc".toByteArray()
        val path = Paths.get("/tmp/com.ahyaemon.kabu.LocalRepositoryTest.kt.test")

        val either = localRepository.save(byteArray, path)

        path.toFile().readBytes() shouldBe byteArray
        either shouldBe Either.right(path)
    }

    @Test
    fun save_failure() {
        val localRepository = LocalRepository()
        val byteArray = "abc".toByteArray()
        val path = mockk<Path>()
        val error = RuntimeException("ERROR!!")
        every { path.toFile() } throws error

        val either = localRepository.save(byteArray, path)

        either shouldBe Either.left(error)
    }
}
