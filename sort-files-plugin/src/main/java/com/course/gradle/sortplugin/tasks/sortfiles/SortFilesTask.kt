package com.course.gradle.sortplugin.tasks.sortfiles

import com.course.gradle.sortplugin.SortFilesPlugin
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.file.Directory
import org.gradle.api.tasks.TaskAction

open class SortFilesTask : DefaultTask() {

    init {
        group = SortFilesPlugin.TASK_GROUP_FILES
        description = "Sorts files in given directory into build.files subdirectories based on the sorting type [creationDate,extension]"
    }

    @TaskAction
    fun sortFiles() {
        val filesFolder = project.property(PROPERTY_FILES_FOLDER) as String
        val sortType = getSortingType()
        val outputDirectoryRoot = project.layout.buildDirectory.dir("files").get()

        project.layout.projectDirectory.dir(filesFolder)
            .asFileTree
            .files
            .forEach { file ->
                val outputDirectory = getOutputDirectory(sortType, file, outputDirectoryRoot)
                copyFile(file, outputDirectory)
            }
    }

    private fun getOutputDirectory(sortType: SortingType, file: File, outputDirectoryRoot: Directory): Directory {
        val sortDirectory = when (sortType) {
            SortingType.CREATION_DATE -> getDateDirectory(file)
            SortingType.EXTENSION -> getExtensionDirectory(file)
            SortingType.ALPHABET -> getAlphabetDirectory(file)
        }
        val outputDirectory = outputDirectoryRoot.dir(sortDirectory)
        return outputDirectory
    }

    private fun copyFile(file: File, outputDirectory: Directory) {
        project.mkdir(outputDirectory)
        val filePath = outputDirectory.file(file.name).asFile.toPath()
        Files.copy(file.toPath(), filePath, StandardCopyOption.REPLACE_EXISTING)
    }

    private fun getSortingType(): SortingType {
        val sortType = project.findProperty(PROPERTY_SORT_TYPE) as String?
        return when (sortType) {
            null,
            SortingType.CREATION_DATE.value -> SortingType.CREATION_DATE

            SortingType.EXTENSION.value -> SortingType.EXTENSION
            SortingType.ALPHABET.value -> SortingType.ALPHABET
            else -> {
                throw InvalidUserDataException("Invalid property `${PROPERTY_SORT_TYPE}`. Valid values are: [${SortingType.entries.joinToString { "'${it.value}'" }}]. value provided")
            }
        }
    }

    private fun getDateDirectory(file: File): String {
        val attributes = Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)
        val formatter = DateTimeFormatter.ofPattern("MM-YYYY")
        val localDate = Instant.ofEpochMilli(attributes.creationTime().toMillis())
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
        return formatter.format(localDate)
    }

    private fun getExtensionDirectory(file: File): String {
        return file.extension
    }

    private fun getAlphabetDirectory(file: File): String {
        return file.name.first().toString()
    }

    companion object {

        private const val PROPERTY_FILES_FOLDER = "tasks.files.folder"
        private const val PROPERTY_SORT_TYPE = "tasks.files.sortType"
    }
}
