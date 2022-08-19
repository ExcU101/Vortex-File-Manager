package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.model.Plugin
import java.net.URL
import java.net.URLClassLoader
import java.util.*

class PluginLoader(
    private val urls: Array<URL>?,
    private val parentLoader: ClassLoader,
) : URLClassLoader(urls) {

    fun getPlugins(): List<Plugin?> {
        val currentClassLoader = Thread.currentThread().contextClassLoader
        try {
            Thread.currentThread().contextClassLoader = this
            return buildList {
                for (plugin in ServiceLoader.load(Plugin::class.java, this@PluginLoader)) {
                    add(plugin)
                }
            }
        } catch (exception: Exception) {
            throw exception
        } finally {
            Thread.currentThread().contextClassLoader = currentClassLoader
        }
    }

    override fun loadClass(name: String?, resolve: Boolean): Class<*>? {
        val loadedClass: Class<*>? =
            findLoadedClass(name)?.let {
                super.loadClass(name, resolve)
            } ?: parentLoader.loadClass(name)


        if (resolve) resolveClass(loadedClass)

        return loadedClass
    }

}