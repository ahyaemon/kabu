package com.ahyaemon.kabu

import arrow.core.Either
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class LocalRepository {

    fun save(target: ByteArray, path: Path): Either<Throwable, Unit> {
        return try {
            path.toFile().writeBytes(target)
            Either.right(Unit)
        } catch (e: Exception){
            Either.left(e)
        }
    }
}
