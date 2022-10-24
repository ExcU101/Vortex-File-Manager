package io.github.excu101.vortex.ui.screen.main

import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.vortex.base.utils.ViewModelContainerHandler
import io.github.excu101.vortex.base.utils.intent
import io.github.excu101.vortex.base.utils.state
import io.github.excu101.vortex.ui.screen.main.MainScreen.State
import io.github.excu101.vortex.ui.screen.storage.list.StorageListPagerFragment
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModelContainerHandler<State, MainScreen.SideEffect>(
    State(StorageListPagerFragment(), storageListTag)
) {

    companion object {
        internal val rootId = View.generateViewId()
        internal const val storageListTag = "STORAGE_LIST"
    }

//    init {
//        navigateTo(dest = StorageListPagerFragment(), tag = storageListTag)
//    }

    fun navigateTo(
        dest: Fragment,
        tag: String? = null,
    ) = intent {
        state {
            State(
                fragment = dest,
                tag = tag
            )
        }
    }

}