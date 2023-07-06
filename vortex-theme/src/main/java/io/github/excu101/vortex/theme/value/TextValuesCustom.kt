package io.github.excu101.vortex.theme.value

import io.github.excu101.vortex.theme.Theme
import io.github.excu101.vortex.theme.model.Text

fun initVortexTextValuesCustom(lines: List<Pair<String, String>>) {
    lines.forEach { (key, value) ->
        Theme[key] = Text(value = value)
    }
}