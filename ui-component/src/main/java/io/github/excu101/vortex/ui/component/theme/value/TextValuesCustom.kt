package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.manager.ui.theme.Theme

fun initVortexTextValuesCustom(lines: List<Pair<String, String>>) {
    lines.forEach { (key, value) ->
        Theme[key] = io.github.excu101.manager.model.Text(value = value)
    }
}