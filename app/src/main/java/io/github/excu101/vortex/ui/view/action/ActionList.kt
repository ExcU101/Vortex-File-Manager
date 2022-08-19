package io.github.excu101.vortex.ui.view.action

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ActionList(context: Context) : RecyclerView(context) {

    private val actionAdapter = ActionAdapter()

    override fun getAdapter(): ActionAdapter = actionAdapter

    init {
        adapter = actionAdapter
        layoutManager = LinearLayoutManager(context)
    }

}