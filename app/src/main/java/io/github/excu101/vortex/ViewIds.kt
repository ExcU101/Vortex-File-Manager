package io.github.excu101.vortex

import android.view.View

object ViewIds {

    object Navigation {
        object Menu {
            val FileManagerId = View.generateViewId()
            val BookmarksId = View.generateViewId()
            val SettingsId = View.generateViewId()
        }
    }

    object Settings {

        object Menu {
            val SearchId = View.generateViewId()
        }

        object Options {
            val AppearanceId = View.generateViewId()
            val BehaviorId = View.generateViewId()
        }

        object Switch {
            val RootId: Int = View.generateViewId()
        }

        object Text {
            val RootId: Int = View.generateViewId()
        }
    }

    object Storage {
        object Menu {
            val OpenId = View.generateViewId()
            val AddNewId = View.generateViewId()
            val DeleteId = View.generateViewId()
            val RenameId = View.generateViewId()
            val CopyId = View.generateViewId()
            val CopyPathId = View.generateViewId()
            val MoveId = View.generateViewId()
            val InfoId = View.generateViewId()

            val AddWatcherId = View.generateViewId()
            val RemoveWatcherId = View.generateViewId()

            val ShowTasks = View.generateViewId()

            val AddBookmarkId = View.generateViewId()
            val RemoveBookmarkId = View.generateViewId()

            val MoreId = View.generateViewId()
            val TasksId = View.generateViewId()
            val SortId = View.generateViewId()
            val SearchId = View.generateViewId()

            val ProvideFullStorageAccessId = View.generateViewId()
            val ProvideStorageAccessId = View.generateViewId()

            val SelectAll = View.generateViewId()
            val DeselectAll = View.generateViewId()
        }

        object Create {
            val CreateConfirmId = View.generateViewId()
        }

        object Sort {
            val ColumnId = View.generateViewId()
            val GridId = View.generateViewId()

            val AscendingId = View.generateViewId()
            val DescendingId = View.generateViewId()

            val NameId = View.generateViewId()
            val PathId = View.generateViewId()
            val SizeId = View.generateViewId()

            val OnlyFoldersId = View.generateViewId()
            val OnlyFilesId = View.generateViewId()
        }

        object Item {
            val RootId = View.generateViewId()
            val IconId = View.generateViewId()
        }

        object Trail {
            val RootId = View.generateViewId()
        }
    }

}