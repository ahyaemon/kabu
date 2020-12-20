package com.ahyaemon.kabu

import arrow.core.Either
import com.ahyaemon.kabu.extensions.exceptionToLeft
import com.ahyaemon.kabu.models.KabuDate
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class LocalRepository {

    fun save(target: ByteArray, path: Path): Either<Throwable, Path> = exceptionToLeft {
        path.toFile().writeBytes(target)
        path
    }

    fun save(target: String, path: Path): Either<Throwable, Path> = exceptionToLeft {
        path.toFile().writeText(target)
        path
    }

    fun readFileAsString(path: Path): Either<Throwable, String> = exceptionToLeft {
        val file = path.toFile()
        file.readText()
    }

    fun readFileAsLines(path: Path, charset: Charset = Charset.forName("Shift-JIS")): Either<Throwable, List<String>> = exceptionToLeft {
        val file = path.toFile()
        file.readLines(charset)
    }

    fun readDateFile(path: Path): Either<Throwable, KabuDate> = exceptionToLeft {
        val file = path.toFile()
        if (file.exists()) {
            KabuDate.fromYYMMDD(file.readText())
        } else {
            KabuDate.fromYYMMDD("000101")
        }
    }

    fun ls(path: Path): Either<Throwable, List<String>> = exceptionToLeft {
        val files = path.toFile().listFiles()
        files?.map { it.name }?.sortedBy { it } ?: listOf("")
    }

    fun append(line: String, path: Path): Either<Throwable, Path> = exceptionToLeft {
        path.toFile().appendText(line + "\n")
        path
    }
}
