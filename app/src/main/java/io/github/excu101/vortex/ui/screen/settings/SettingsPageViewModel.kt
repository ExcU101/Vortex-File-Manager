package io.github.excu101.vortex.ui.screen.settings

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.vortex.base.utils.ViewModelContainerHandler
import io.github.excu101.vortex.base.utils.intent
import io.github.excu101.vortex.provider.settings.Settings
import io.github.excu101.vortex.ui.screen.settings.SettingPageScreen.SideEffect
import io.github.excu101.vortex.ui.screen.settings.SettingPageScreen.State
import javax.inject.Inject

@HiltViewModel
class SettingsPageViewModel @Inject constructor(
    private val settings: Settings,
    private val handle: SavedStateHandle,
) : ViewModelContainerHandler<State, SideEffect>(State()) {

    fun read() = intent {

    }

}