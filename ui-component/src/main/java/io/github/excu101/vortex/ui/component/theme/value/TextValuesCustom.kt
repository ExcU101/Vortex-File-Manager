package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Text
import io.github.excu101.pluginsystem.ui.theme.Theme

fun initVortexTextValuesCustom(lines: List<Pair<String, String>>) {
    lines.forEach { (key, value) ->
        Theme[key] = io.github.excu101.pluginsystem.model.Text(value = value)
    }
}