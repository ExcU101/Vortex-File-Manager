package io.github.excu101.pluginsystem.common

import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.UnixFileSystemProvider
import io.github.excu101.filesystem.unix.utils.unixDeleteClass
import io.github.excu101.pluginsystem.dsl.registers
import io.github.excu101.pluginsystem.model.Plugin

class DefaultOperationsPlugin : Plugin {

    override val attributes: Plugin.Attributes = object : Plugin.Attributes {

        override val name: String
            get() = "Vortex operations plugin"

        override val version: String
            get() = "1.0.0"

        override val packageName: String
            get() = "io.github.excu101.plugins"
    }

    override fun activate() = registers {
        FileProvider.installDefault(system = UnixFileSystem(UnixFileSystemProvider()))
        registerGroup {
            name = "Vortex operations"
        }

        register(unixDeleteClass())

    }

    override fun disable() {
        registers {
            unregisterGroup()
        }
    }
}