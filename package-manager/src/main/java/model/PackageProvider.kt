package model

import io.github.excu101.filesystem.fs.path.Path

interface PackageProvider {

    fun providePackagePath(): Path

    fun providePackageConfPath(): Path

}