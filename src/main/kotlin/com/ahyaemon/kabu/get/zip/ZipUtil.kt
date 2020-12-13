package com.ahyaemon.kabu.get.zip

import arrow.core.Either
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class ZipUtil {
    fun unzip(path: Path): Either<Throwable, Zip> {
        return FileSystems.newFileSystem(path, null).let { fs ->
            Either.right(Zip(
                    name = path.getCsvName(),
                    content = Files.newInputStream(fs.getPath(path.getCsvName())).readAllBytes()
            ))
        }
    }

    private fun Path.getCsvName() = this.fileName.toString().replace("zip", "csv")
}
