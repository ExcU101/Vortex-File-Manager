package model

import io.github.excu101.filesystem.fs.path.Path

data class Package(
    val id: String = "",
    val descriptor: PackageDescriptor,
    val dependencies: List<Package> = listOf(),
    val optionalDependencies: List<Package> = listOf(),
    val packageFiles: List<Path> = listOf(),
    val replaces: List<Package> = listOf(),
    val conflicts: List<Package> = listOf(),
    val size: Long = -1L,
    val downloadSize: Long = -1L
)