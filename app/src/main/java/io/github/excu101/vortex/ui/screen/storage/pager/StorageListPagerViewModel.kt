package io.github.excu101.vortex.ui.screen.storage.pager

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.vortex.base.utils.ViewModelContainerHandler
import io.github.excu101.vortex.base.utils.intent
import io.github.excu101.vortex.base.utils.state
import io.github.excu101.vortex.ui.screen.storage.pager.StorageListPagerScreen.SideEffect
import io.github.excu101.vortex.ui.screen.storage.pager.StorageListPagerScreen.State
import io.github.excu101.vortex.ui.screen.storage.pager.page.list.StorageListPageFragment
import javax.inject.Inject

@HiltViewModel
class StorageListPagerViewModel @Inject constructor(

) : ViewModelContainerHandler<State, SideEffect>(
    initialState = State(
        listOf(StorageListPageFragment())
    )
) {

    fun addFragments(
        vararg fragment: StorageListPageFragment,
    ) = intent {
        state {
            copy(
                fragments = fragments + fragment
            )
        }
    }

    fun removeFragments(
        vararg fragment: StorageListPageFragment,
    ) = intent {
        state {
            copy(
                fragments = fragments - fragment.toSet()
            )
        }
    }

}