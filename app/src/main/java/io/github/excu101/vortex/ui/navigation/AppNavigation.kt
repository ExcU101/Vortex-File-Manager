package io.github.excu101.vortex.ui.navigation

import io.github.excu101.vortex.navigation.dsl.fragment
import io.github.excu101.vortex.navigation.dsl.navigation
import io.github.excu101.vortex.ui.navigation.AppNavigation.Routes.Settings
import io.github.excu101.vortex.ui.navigation.AppNavigation.Routes.Storage.Bookmark
import io.github.excu101.vortex.ui.navigation.AppNavigation.Routes.Storage.Create
import io.github.excu101.vortex.ui.navigation.AppNavigation.Routes.Storage.List
import io.github.excu101.vortex.ui.screen.settings.SettingsPageFragment
import io.github.excu101.vortex.ui.screen.storage.page.bookmark.StorageBookmarkPageFragment
import io.github.excu101.vortex.ui.screen.storage.page.create.StorageItemCreatePage
import io.github.excu101.vortex.ui.screen.storage.page.info.StorageItemInfoPageFragment
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageFragment

object AppNavigation {

    val Graph = navigation {
        fragment(route = List.Page, factory = StorageListPageFragment)
        fragment(route = Bookmark.Page, factory = StorageBookmarkPageFragment)
        fragment(route = List.ItemPageInfo, factory = StorageItemInfoPageFragment)
        fragment(route = Create.Page, factory = StorageItemCreatePage)
        fragment(route = Settings.Page, factory = SettingsPageFragment)
    }

    object Routes {
        object Pager {
            const val Pager = "Pager"
        }

        object Settings {
            const val Page = "Settings"
        }

        object Storage {
            object Bookmark {
                const val Page = "StorageBookmarkPage"
            }

            object List {
                const val Page = "StorageListPage"
                const val ItemPageInfo = "StorageItemPageInfo"
            }

            object Create {
                const val Page = "StorageItemCreatePage"
            }
        }
    }

    object Args {
        object Storage {
            object ItemInfoPage {
                const val ItemInfoPageKey = "PageInfoItem"
            }

            object ListPage {
                const val PageTitleKey = "PageTitle"
            }

            object CreatePage {
                const val ParentDirectoryKey = "ParentDirectory"
            }
        }
    }

}