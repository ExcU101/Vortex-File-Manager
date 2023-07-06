package io.github.excu101.vortex

import android.content.Context
import android.os.Build
import androidx.multidex.MultiDexApplication
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.provider.UnixFileSystemProvider
import io.github.excu101.vortex.di.AppComponent
import io.github.excu101.vortex.theme.value.color.ocean.initOceanLightColorValues
import io.github.excu101.vortex.theme.value.initVortexDimenValues
import io.github.excu101.vortex.theme.value.text.en.initVortexTextValuesEN
import io.github.excu101.vortex.ui.icon.IconInitializer
import io.github.excu101.vortex.di.DaggerAppComponent.builder as AppComponentBuilder

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        val provider = UnixFileSystemProvider()
        val system = UnixFileSystem(provider)


//        when (val mode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK) {
//            UI_MODE_NIGHT_YES -> {
//                initOceanDarkColorValues()
//            }
//
//            UI_MODE_NIGHT_NO -> {
//                initOceanLightColorValues()
//            }
//
//            else ->
        io.github.excu101.vortex.theme.value.color.ocean.initOceanLightColorValues()
//        initOceanDarkColorValues()
//        }

        io.github.excu101.vortex.theme.value.initVortexDimenValues()

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
        io.github.excu101.vortex.theme.value.text.en.initVortexTextValuesEN()
        IconInitializer.context = this
        FileProvider.installDefault(system)
    }

}

val Context.component: AppComponent
    get() = AppComponentBuilder().bindContext(this).build()