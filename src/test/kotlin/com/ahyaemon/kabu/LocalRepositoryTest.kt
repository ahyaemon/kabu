package com.ahyaemon.kabu

import arrow.core.Either
import com.ahyaemon.kabu.models.KabuDate
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.nio.file.Path
import java.nio.file.Paths

internal class LocalRepositoryTest {

    @Test
    fun save_success() {
        val localRepository = LocalRepositoryImpl()
        val byteArray = "abc".toByteArray()
        val path = Paths.get("/tmp/com.ahyaemon.kabu.LocalRepositoryTest.kt.test")

        val either = localRepository.save(byteArray, path)

        path.toFile().readBytes() shouldBe byteArray
        either shouldBe Either.right(path)
    }

    @Test
    fun save_failure() {
        val localRepository = LocalRepositoryImpl()
        val byteArray = "abc".toByteArray()
        val path = mockk<Path>()
        val error = RuntimeException("ERROR!!")
        every { path.toFile() } throws error

        val either = localRepository.save(byteArray, path)

        either shouldBe Either.left(error)
    }

    @Test
    fun readFileAsString_success() {
        val localRepository = LocalRepositoryImpl()
        val csvPath = Paths.get(ClassLoader.getSystemResource("hoge.csv").path)

        val either = localRepository.readFileAsString(csvPath)

        either shouldBe Either.right("hoge\n")
    }

    @Test
    fun readDateFile_success() {
        val localRepository = LocalRepositoryImpl()
        val dateFilePath = Paths.get(ClassLoader.getSystemResource("KabuChartServiceTest/data/chart/date.txt").path)

        val either = localRepository.readDateFile(dateFilePath)

        either shouldBe Either.right(KabuDate.fromYYMMDD("201209"))
    }

    @Test
    fun ls_success() {
        val localRepository = LocalRepositoryImpl()
        val dirPath = Paths.get(ClassLoader.getSystemResource("dir").path)

        val either = localRepository.ls(dirPath)

        either.shouldBe(Either.right(listOf("a.txt", "b.txt")))
    }
}
