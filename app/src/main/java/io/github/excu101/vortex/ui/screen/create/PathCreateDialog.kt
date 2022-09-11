package io.github.excu101.vortex.ui.screen.create

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialog

class PathCreateDialog(context: Context) : BottomSheetDialog(context) {

    private val root = LinearLayout(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(root)
    }

}