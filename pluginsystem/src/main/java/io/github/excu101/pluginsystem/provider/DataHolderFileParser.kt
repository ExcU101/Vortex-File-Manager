package io.github.excu101.pluginsystem.provider

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.pluginsystem.model.DataHolder

interface DataHolderFileParser<T : DataHolder<*>> {

    fun read(path: Path)

    val keys: List<String>

    val values: List<T>

}