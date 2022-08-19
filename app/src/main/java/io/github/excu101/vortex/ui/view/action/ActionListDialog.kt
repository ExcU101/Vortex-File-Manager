package io.github.excu101.vortex.ui.view.action

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.excu101.pluginsystem.model.GroupAction

class ActionListDialog(context: Context) : BottomSheetDialog(context) {

    private val root = CoordinatorLayout(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    private val list = ActionList(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    val data: List<GroupAction>
        get() = list.adapter.currentList

    override fun onCreate(savedInstanceState: Bundle?): Unit = with(root) {
        super.onCreate(savedInstanceState)
        setContentView(this)
        addView(list)
    }

    override fun onStart() {
        super.onStart()
    }

    fun updateActions(new: List<GroupAction>) {
        list.adapter.submitList(new)
    }

}