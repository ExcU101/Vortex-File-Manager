package io.github.excu101.vortex.ui.screen.main

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.vortex.ui.screen.list.StorageListFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {

    companion object {
        internal val rootId = View.generateViewId()
        internal const val storageListTag = "STORAGE_LIST"
    }

    private val _screen = MutableStateFlow<Pair<Fragment, String?>>(value = Fragment() to null)
    val screen: StateFlow<Pair<Fragment, String?>>
        get() = _screen.asStateFlow()

    init {
        navigateTo(dest = StorageListFragment(), tag = storageListTag)
    }

    fun navigateTo(
        dest: Fragment,
        tag: String? = null,
    ) {
        viewModelScope.launch {
            _screen.emit(value = dest to tag)
        }
    }

}