package io.github.excu101.vortex.data.storage

import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.ResultParser
import io.github.excu101.vortex.provider.storage.impl.EmptyResultParser
import io.github.excu101.vortex.provider.storage.impl.ListResultParser

object PathItemParsers {

    val Default: ResultParser<PathItem> = ListResultParser()
    val NoTitle: ResultParser<PathItem> = EmptyResultParser

}