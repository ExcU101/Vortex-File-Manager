package manager

import io.github.excu101.filesystem.fs.operation.FileOperation
import model.Package

interface PackageObserver {

    fun onAction(action: FileOperation.Action)

    fun onRemovePackage(pac: Package)

    fun onInstallPackage(pac: Package)

}