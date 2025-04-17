package com.course.gradle.sortplugin

import com.course.gradle.sortplugin.tasks.clean.CleanTask
import com.course.gradle.sortplugin.tasks.sortfiles.SortFilesTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class SortFilesPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (project.tasks.findByName(CLEAN_TASK_NAME) == null) {
            project.tasks.register(CLEAN_TASK_NAME, CleanTask::class.java)
        }

        project.tasks.register("sortFiles", SortFilesTask::class.java)
            .configure {
                it.dependsOn(CLEAN_TASK_NAME)
            }
    }

    companion object {

        internal const val TASK_GROUP_FILES = "Files"
        private const val CLEAN_TASK_NAME = "clean"
    }
}
