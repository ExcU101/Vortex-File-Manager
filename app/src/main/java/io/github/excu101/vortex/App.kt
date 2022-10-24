package io.github.excu101.vortex

import android.content.res.Configuration.*
import android.os.Build
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.UnixFileSystemProvider
import io.github.excu101.vortex.ui.component.theme.value.*

@HiltAndroidApp
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        val provider = UnixFileSystemProvider()
        val system = UnixFileSystem(provider)

        when (val mode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK) {
            UI_MODE_NIGHT_YES -> {
                initVortexDarkColorValues()
            }
            UI_MODE_NIGHT_NO -> {
                initVortexLightColorValues()
            }

            else -> initVortexDarkColorValues()
        }

        initVortexDimenValues()

        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }

        when (locale.language) {
            "en" -> initVortexTextValuesEN()
            "ua" -> initVortexTextValuesUA()
            "ru" -> initVortexTextValuesRU()
            "de" -> initVortexTextValuesDE()
            else -> initVortexTextValuesCustom(lines = emptyList())
        }

        FileProvider.installDefault(system)
    }

}