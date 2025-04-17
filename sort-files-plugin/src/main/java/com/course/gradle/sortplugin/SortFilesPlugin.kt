package com.course.gradle.sortplugin

import com.course.gradle.sortplugin.tasks.clean.CleanTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class SortFilesPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.register("clean", CleanTask::class.java)
    }

    companion object {

        internal const val TASK_GROUP_FILES = "Files"
    }
}
