import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

tasks.register<Delete>("clean") {
    group = tasksGroup
    description = "Clean build directory"

    delete(layout.buildDirectory)
}

tasks.register("sortFiles") {
    group = tasksGroup
    description = "Sorts files in given directory into build.files subdirectories based on the sorting type [creationDate,extension]"

    val filesFolder = project.property("tasks.files.folder") as String
    val sortType = getSortingType(project)
    val outputDirectory = layout.buildDirectory.dir("files").get()

    dependsOn(tasks.named("clean"))

    doLast {
        layout.projectDirectory.dir(filesFolder)
            .asFileTree
            .files
            .forEach { file ->
                val sortDirectory = when (sortType) {
                    SortingType.CREATION_DATE -> getDateDirectory(file)
                    SortingType.EXTENSION -> getExtensionDirectory(file)
                }
                val directory = outputDirectory.dir(sortDirectory)
                mkdir(directory)
                val filePath = directory.file(file.name).asFile.toPath()
                Files.copy(file.toPath(), filePath, StandardCopyOption.REPLACE_EXISTING)
            }
    }
}

private fun getSortingType(project: Project): SortingType {
    val sortType = project.findProperty(SORT_TYPE_PROPERTY) as String?
    return when (sortType) {
        null,
        SortingType.CREATION_DATE.value -> SortingType.CREATION_DATE

        SortingType.EXTENSION.value -> SortingType.EXTENSION
        else -> {
            throw InvalidUserDataException("Invalid property `${SORT_TYPE_PROPERTY}`. Valid values are: [${SortingType.values().joinToString { "'${it.value}'" }}]. value provided")
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

enum class SortingType(val value: String) {
    CREATION_DATE("creationDate"),
    EXTENSION("extension"),
}

private val tasksGroup = "Files"
private val SORT_TYPE_PROPERTY = "tasks.files.sortType"
