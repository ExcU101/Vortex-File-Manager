package io.github.excu101.vortex.data.storage

import io.github.excu101.vortex.base.impl.ResultParser
import io.github.excu101.vortex.base.impl.ResultParserImpl
import io.github.excu101.vortex.data.PathItem

object PathItemParsers {

    val Default: ResultParser<PathItem> = ResultParserImpl()

}