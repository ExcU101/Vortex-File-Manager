package io.github.excu101.vortex.ui.navigation

import io.github.excu101.vortex.navigation.dsl.fragment
import io.github.excu101.vortex.navigation.dsl.navigation
import io.github.excu101.vortex.ui.navigation.AppNavigation.Routes.Storage.Bookmark
import io.github.excu101.vortex.ui.navigation.AppNavigation.Routes.Storage.List
import io.github.excu101.vortex.ui.screen.storage.pager.page.bookmark.StorageBookmarkPageFragment
import io.github.excu101.vortex.ui.screen.storage.pager.StorageListPagerFragment
import io.github.excu101.vortex.ui.screen.storage.pager.page.info.StorageItemInfoPageFragment
import io.github.excu101.vortex.ui.screen.storage.pager.page.list.StorageListPageFragment

object AppNavigation {

    val Graph = navigation {
        fragment(route = List.Page) { args ->
            StorageListPageFragment.create(args)
        }
        fragment(route = List.Pager) { args ->
            StorageListPagerFragment.create(args)
        }
        fragment(route = Bookmark.Page) { args ->
            StorageBookmarkPageFragment.create(args)
        }
        fragment(route = List.ItemPageInfo) { args ->
            StorageItemInfoPageFragment.create(args)
        }
    }

    object Routes {
        object Storage {
            object Bookmark {
                const val Page = "StorageBookmarkPage"
            }

            object List {
                const val Page = "StorageListPage"
                const val Pager = "StorageListPager"
                const val ItemPageInfo = "StorageItemPageInfo"
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

            object Pager {
                const val PagerCountKey = "PagerCountKey"
            }
        }
    }

}