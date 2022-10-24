package io.github.excu101.pluginsystem.utils

import io.github.excu101.pluginsystem.provider.PluginManagerImpl.pluginExtensionName
import java.io.File
import java.net.URL

internal fun File.asPluginsUrl(): List<URL> {
    listFiles()?.forEach {
        if (!it.path.endsWith(suffix = pluginExtensionName)) return emptyList()
    }
    return listFiles()?.map { plugin ->
        plugin.toURI()
    }?.map { pluginUri ->
        pluginUri.toURL()
    } ?: emptyList()
}