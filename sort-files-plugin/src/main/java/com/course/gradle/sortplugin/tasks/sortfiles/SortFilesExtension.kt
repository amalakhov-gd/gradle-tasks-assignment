package com.course.gradle.sortplugin.tasks.sortfiles

import org.gradle.api.provider.Property

interface SortFilesExtension {

    val filesFolder: Property<String>
    val sortType: Property<String>
}
