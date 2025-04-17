package com.course.gradle.sortplugin.tasks.clean

import com.course.gradle.sortplugin.SortFilesPlugin
import org.gradle.api.tasks.Delete

open class CleanTask : Delete() {

    init {
        group = SortFilesPlugin.TASK_GROUP_FILES
        description = "Clean build directory"
        delete(project.layout.buildDirectory)
    }
}
