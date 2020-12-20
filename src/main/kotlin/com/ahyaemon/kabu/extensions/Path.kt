package com.ahyaemon.kabu.extensions

import java.nio.file.Path
import java.nio.file.Paths

fun Path.addChild(child: String): Path = Paths.get(this.toString(), child)
