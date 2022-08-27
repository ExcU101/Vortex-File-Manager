package io.github.excu101.vortex.data

import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.adapter.Item

data class StorageItem(
    override val value: FileUnit,
) : Item<FileUnit> {

    constructor(path: Path) : this(FileUnit(path))

    override val id: Long
        get() = value.path.hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.FILE_UNIT

}