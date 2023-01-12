package io.github.excu101.vortex

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.provider.UnixFileSystemProvider
import io.github.excu101.vortex.service.VortexService
import io.github.excu101.vortex.ui.component.theme.value.color.ocean.initOceanDarkColorValues
import io.github.excu101.vortex.ui.component.theme.value.color.ocean.initOceanLightColorValues
import io.github.excu101.vortex.ui.component.theme.value.initVortexDimenValues
import io.github.excu101.vortex.ui.component.theme.value.text.en.initVortexTextValuesEN
import io.github.excu101.vortex.ui.icon.IconInitializer

@HiltAndroidApp
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        val provider = UnixFileSystemProvider()
        val system = UnixFileSystem(provider)

        when (val mode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK) {
            UI_MODE_NIGHT_YES -> {
                initOceanDarkColorValues()
            }

            UI_MODE_NIGHT_NO -> {
                initOceanLightColorValues()
            }

            else -> initOceanDarkColorValues()
        }

        initVortexDimenValues()

        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }

//        when (locale.language) {
//            "en" ->
//            "ua" -> initVortexTextValuesUA()
//            "ru" -> initVortexTextValuesRU()
//            "de" -> initVortexTextValuesDE()
//            else -> initVortexTextValuesCustom(lines = emptyList())
//        }
        initVortexTextValuesEN()
        IconInitializer.context = this
        FileProvider.installDefault(system)
    }

}

fun Context.startVortexService() {
    Intent(this, VortexService::class.java).also {
        startService(it)
    }
}

fun Context.stopVortexService() {
    Intent(this, VortexService::class.java).also {
        stopService(it)
    }
}

fun Context.bindVortexService(
    connection: ServiceConnection,
    flags: Int,
) {
    Intent(this, VortexService::class.java).also {
        bindService(it, connection, flags)
    }
}

fun Context.unbindVortexService(
    connection: ServiceConnection,
) {
    unbindService(connection)
}
