package com.ahyaemon.kabu

import arrow.core.Either
import com.ahyaemon.kabu.models.KabuDate
import java.nio.charset.Charset
import java.nio.file.Path

interface LocalRepository {

    fun save(target: ByteArray, path: Path): Either<Throwable, Path>

    fun save(target: String, path: Path): Either<Throwable, Path>

    fun readFileAsString(path: Path): Either<Throwable, String>

    fun readFileAsLines(path: Path, charset: Charset = Charset.forName("Shift-JIS")): Either<Throwable, List<String>>

    fun readDateFile(path: Path): Either<Throwable, KabuDate>

    fun ls(path: Path): Either<Throwable, List<String>>

    fun append(line: String, path: Path): Either<Throwable, Path>
}
