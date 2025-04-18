package com.course.gradle.sortplugin.tasks.sortfiles.mapping

import java.io.File

internal class AlphabetDirectoryMapper {

    fun getDirectory(file: File): String {
        return file.name.first().toString()
    }
}
