package io.github.excu101.vortex

import android.view.View

object ViewIds {

    object Navigation {

        object Menu {
            const val FileManagerId = R.id.file_manager_dest
            const val BookmarksId = R.id.bookmarks_dest
            const val SettingsId = R.id.settings_dest
        }
    }

    object Storage {
        object Menu {
            const val OpenId = R.id.file_manager_menu_open_op
            const val AddNewId = R.id.file_manager_menu_add_new_op
            const val DeleteId = R.id.file_manager_menu_delete_op
            const val RenameId = R.id.file_manager_menu_rename_op
            const val CopyId = R.id.file_manager_menu_copy_op
            const val CopyPathId = R.id.file_manager_menu_copy_path_op
            const val MoveId = R.id.file_manager_menu_move_op
            const val InfoId = R.id.file_manager_menu_info_op

            const val AddWatcherId = R.id.file_manager_menu_add_watcher_op
            const val RemoveWatcherId = R.id.file_manager_menu_remove_watcher_op

            const val ShowTasks = R.id.file_manager_menu_show_tasks

            const val MoreId = R.id.file_manager_menu_more
            val TasksId = View.generateViewId()
            const val SortId = R.id.file_manager_menu_sort
            val SearchId = View.generateViewId()

            const val ProvideFullStorageAccessId =
                R.id.file_manager_menu_provide_full_storage_access_op
            const val ProvideStorageAccessId = R.id.file_manager_menu_provide_storage_access_op
            const val ProvideRestrictedDirectoryAccess =
                R.id.file_manager_menu_provide_restricted_dir_access
        }

        object Item {
            const val RootId = R.id.file_manager_item_root
            const val IconId = R.id.file_manager_item_icon
        }

        object Trail {
            const val RootId = R.id.file_manager_trail_root
        }
    }

}