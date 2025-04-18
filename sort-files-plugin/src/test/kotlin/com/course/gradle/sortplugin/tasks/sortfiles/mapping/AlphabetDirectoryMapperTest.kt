package com.course.gradle.sortplugin.tasks.sortfiles.mapping

import java.io.File
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class AlphabetDirectoryMapperTest {

    @Test
    fun `get output directory`() {
        val mapper = AlphabetDirectoryMapper()

        val file = File("aFile.txt")
        val directory = mapper.getDirectory(file)

        Assertions.assertEquals("a", directory)
    }
}
