package io.github.excu101.vortex.provider.settings

import androidx.datastore.preferences.core.intPreferencesKey

object PathItemInfoParserSettingsKeys {

    val PathItemLastModifiedTimeInfoPositionKey =
        intPreferencesKey(name = "settings_int_path_item_modified_time_info_pos")

    val PathItemCountInfoPositionKey =
        intPreferencesKey(name = "settings_int_path_item_count_info_pos")

    val PathItemSizeInfoPositionKey =
        intPreferencesKey(name = "settings_int_path_item_size_info_pos")

}