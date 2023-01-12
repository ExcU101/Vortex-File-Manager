package io.github.excu101.vortex.provider.settings

import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow

object StorageSettingsItems {

    object BooleanItems {

    }

    object IntItems {
        val PathItemLastModifiedTimeInfoPositionKey = intPreferencesKey(
            name = "settings_int_path_item_modified_time_info_pos"
        )

        val Settings.lastModifiedTimeInfoPosition: Flow<Int>
            get() = read(
                key = PathItemLastModifiedTimeInfoPositionKey,
                default = 1
            )


        val PathItemCountInfoPositionKey = intPreferencesKey(
            name = "settings_int_path_item_count_info_pos"
        )

        val Settings.countInfoPosition: Flow<Int>
            get() = read(
                key = PathItemCountInfoPositionKey,
                default = 0
            )

        val PathItemSizeInfoPositionKey = intPreferencesKey(
            name = "settings_int_path_item_size_info_pos"
        )

        val Settings.sizeInfoPosition: Flow<Int>
            get() = read(
                key = PathItemSizeInfoPositionKey,
                default = 4
            )
    }

}