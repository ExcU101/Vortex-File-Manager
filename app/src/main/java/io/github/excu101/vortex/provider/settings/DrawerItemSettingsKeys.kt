package io.github.excu101.vortex.provider.settings

import androidx.datastore.preferences.core.intPreferencesKey

object DrawerItemSettingsKeys {

    val FileManagerItemPositionKey = intPreferencesKey(name = "settings_int_file_manager_item_pos")
    val BookmarkItemPositionKey = intPreferencesKey(name = "settings_int_bookmark_item_pos")

}