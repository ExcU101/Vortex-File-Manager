package io.github.excu101.vortex.ui.view.warning

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.ColorStateList.valueOf
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.makeMeasureSpec
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT
import androidx.core.view.contains
import androidx.core.view.updatePadding
import com.google.android.material.button.MaterialButton
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.fileWarningActionContentColorKey
import io.github.excu101.vortex.ui.theme.key.fileWarningBackgroundColorKey
import io.github.excu101.vortex.ui.view.dp
import io.github.excu101.vortex.ui.view.removeViewFrom

class WarningView : FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val iconSize = 56.dp
    private val messagePadding = 32.dp

    private val iconView = ImageView(context).apply {
        minimumHeight = iconSize
        minimumWidth = iconSize
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    private val messageView = TextView(context).apply {
        textSize = 18F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        updatePadding(top = 16)
    }

    private val actions: MutableList<Action> = arrayListOf()

    var message: String
        get() = messageView.text.toString()
        set(value) {
            ensureContainingMessage()
            messageView.text = value
        }

    val isContainingIcon: Boolean
        get() = contains(iconView)

    val isContainingMessage: Boolean
        get() = contains(messageView)

    private fun ensureContainingMessage() {
        if (!isContainingMessage) {
            addView(messageView, 1)
        }
    }

    private fun ensureContainingIcon() {
        if (!isContainingIcon) {
            addView(iconView, 0)
        }
    }

    fun setIcon(@DrawableRes id: Int) {
        ensureContainingIcon()
        iconView.setImageResource(id)
    }

    fun addAction(value: Action) {
        actions.add(value)
        refreshActions()
    }

    fun remove(value: Action) {
        actions.remove(value)
        refreshActions()
    }

    private fun refreshActions() {
        removeViewFrom(range = 2..actions.lastIndex + 2)
        actions.forEachIndexed { index, action ->
            val view = MaterialButton(context).apply {
                icon = action.icon
                text = action.title
                iconTint = valueOf(Theme<Int, Color>(fileWarningActionContentColorKey))
                setTextColor(Theme<Int, Color>(fileWarningActionContentColorKey))
                layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            }
            addView(view, index + 2)
        }
    }

    init {
        setBackgroundColor(Theme<Int, Color>(fileWarningBackgroundColorKey))
        isClickable = true
        isFocusable = true
        setIcon(R.drawable.ic_info_24)
        message = "Fuck"
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        iconView.layout(
            width / 2 - iconView.width,
            height / 2 - iconSize,
            width / 2 + iconView.width,
            height / 2 + iconSize,
        )

        messageView.layout(
            width / 2 - messageView.width,
            height / 2 + iconSize,
            width / 2 + messageView.width,
            height / 2 + iconSize + messagePadding,
        )

    }
}