package com.ahyaemon.kabu.subcommands.adjust

import arrow.core.Either
import arrow.core.flatMap
import com.ahyaemon.kabu.LocalRepository
import com.ahyaemon.kabu.extensions.addChild
import java.nio.file.Path
import javax.inject.Singleton

@Singleton
class KabuAdjustServiceImpl(
    private val splitHtmlParser: SplitHtmlParser,
    private val mergeHtmlParser: MergeHtmlParser,
    private val localRepository: LocalRepository
) : KabuAdjustService {

    override fun getAdjustDate(dataDirectoryPath: Path): Either<Throwable, Unit> {
        val adjustPath = dataDirectoryPath.addChild("adjust")
        val splitPath = adjustPath.addChild("split")
        val mergePath = adjustPath.addChild("merge")

        return splitHtmlParser.createCsv(splitPath.addChild("split.html"))
            .flatMap { splitCsv ->
                localRepository.save(splitCsv, splitPath.addChild("split.csv"))
            }
            .flatMap {
                mergeHtmlParser.createCsv(mergePath.addChild("merge.html"))
            }
            .flatMap{ mergeCsv ->
                localRepository.save(mergeCsv, mergePath.addChild("merge.csv"))
            }
            .map {}
    }
}
