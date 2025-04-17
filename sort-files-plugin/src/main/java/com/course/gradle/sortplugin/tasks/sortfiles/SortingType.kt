package com.course.gradle.sortplugin.tasks.sortfiles

internal enum class SortingType(val value: String) {
    CREATION_DATE("creationDate"),
    EXTENSION("extension"),
    ALPHABET("alphabet"),
}
