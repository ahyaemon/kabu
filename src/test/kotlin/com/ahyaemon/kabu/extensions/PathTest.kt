package com.ahyaemon.kabu.extensions

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class PathTest {

    @Test
    fun addChild() {
        val path = Paths.get("/tmp")
        val added = path.addChild("child")

        added.toString() shouldBe "/tmp/child"
    }
}
