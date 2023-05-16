package io.github.excu101.vortex.ui.screen.navigation

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import io.github.excu101.manager.ui.theme.Theme
import io.github.excu101.manager.ui.theme.ThemeColorChangeListener
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.ui.component.BarOwner
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.navigation.Routes
import io.github.excu101.vortex.ui.screen.storage.StoragePageController


class NavigationActivity : ComponentActivity(), ThemeColorChangeListener, BarOwner,
    ItemViewListener<Item<*>> {

    private var binding: NavigationPageBinding? = null
    private var controller: NavigationPageController? = null

    override val bar: Bar?
        get() = binding?.bar

    override fun onCreate(inState: Bundle?) {
        super.onCreate(inState)

        controller = NavigationPageController(context = this)

        controller?.graph?.controller?.init(StoragePageController(context = this))

        setContentView(controller?.getContentView())

        if (inState != null)
            controller?.graph?.restoreNavigation(inState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        controller?.graph?.saveNavigation(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (controller?.onBackActivated() == false)
            super.onBackPressed()
    }

    override fun onClick(view: View, item: Item<*>, position: Int) {
        when (item) {
            is DrawerItem -> {
                when (view.id) {
                    ViewIds.Navigation.Menu.SettingsId -> {
//                        controller.navigate(route = AppNavigation.Routes.Settings.Page)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        binding = null

        Theme.unregisterColorChangeListener(this)
//        stopVortexService()
        super.onDestroy()
    }

    override fun onColorChanged() {
//        binding?.root?.background = ColorDrawable(ThemeColor(backgroundColorKey))
    }
}