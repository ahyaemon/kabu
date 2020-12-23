package com.ahyaemon.kabu

import arrow.core.Either
import com.ahyaemon.kabu.extensions.exceptionToLeft
import com.ahyaemon.kabu.models.KabuDate
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl : LocalRepository {

    override fun save(target: ByteArray, path: Path): Either<Throwable, Path> = exceptionToLeft {
        path.toFile().writeBytes(target)
        path
    }

    override fun save(target: String, path: Path): Either<Throwable, Path> = exceptionToLeft {
        path.toFile().writeText(target)
        path
    }

    override fun readFileAsString(path: Path): Either<Throwable, String> = exceptionToLeft {
        val file = path.toFile()
        file.readText()
    }

    override fun readFileAsLines(path: Path, charset: Charset): Either<Throwable, List<String>> = exceptionToLeft {
        val file = path.toFile()
        file.readLines(charset)
    }

    override fun readDateFile(path: Path): Either<Throwable, KabuDate> = exceptionToLeft {
        val file = path.toFile()
        if (file.exists()) {
            KabuDate.fromYYMMDD(file.readText())
        } else {
            KabuDate.fromYYMMDD("000101")
        }
    }

    override fun ls(path: Path): Either<Throwable, List<String>> = exceptionToLeft {
        val files = path.toFile().listFiles()
        files?.map { it.name }?.sortedBy { it } ?: listOf("")
    }

    override fun append(line: String, path: Path): Either<Throwable, Path> = exceptionToLeft {
        path.toFile().appendText(line + "\n")
        path
    }
}
