package io.github.excu101.vortex.ui.component.warning

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT
import androidx.core.view.contains
import androidx.core.view.updatePadding
import com.google.android.material.button.MaterialButton
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeColorChangeListener
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.removeViewFrom
import io.github.excu101.vortex.ui.component.theme.key.fileWarningActionContentColorKey
import io.github.excu101.vortex.ui.component.theme.key.fileWarningBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.fileWarningIconTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.fileWarningTitleTextColorKey

class WarningView : ThemeLinearLayout {

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

    var message: CharSequence?
        get() = messageView.text
        set(value) {
            ensureContainingMessage()
            messageView.text = value
        }

    var icon: Drawable?
        get() = iconView.drawable
        set(value) {
            ensureContainingIcon()
            iconView.setImageDrawable(value)
        }

    val isContainingIcon: Boolean
        get() = contains(iconView)

    val isContainingMessage: Boolean
        get() = contains(messageView)

    private fun ensureContainingMessage() {
        if (!isContainingMessage) {
            addView(messageView)
        }
    }

    private fun ensureContainingIcon() {
        if (!isContainingIcon) {
            addView(iconView)
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
        actions.forEachIndexed { index, action ->
            val view = MaterialButton(context).apply {
                icon = action.icon
                text = action.title
                iconTint = valueOf(ThemeColor(fileWarningActionContentColorKey))
                setTextColor(ThemeColor(fileWarningActionContentColorKey))
                layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            }
            addView(view, index + 2)
        }
    }

    init {
        orientation = VERTICAL
        gravity = CENTER
        setBackgroundColor(ThemeColor(fileWarningBackgroundColorKey))
    }

    override fun onChanged() {
        iconView.setColorFilter(ThemeColor(fileWarningIconTintColorKey))
        messageView.setTextColor(ThemeColor(fileWarningTitleTextColorKey))
        setBackgroundColor(ThemeColor(fileWarningBackgroundColorKey))
    }

//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//
//        iconView.layout(
//            width / 2 - iconView.width,
//            height / 2 - iconSize,
//            width / 2 + iconView.width,
//            height / 2 + iconSize,
//        )
//
//        messageView.layout(
//            width / 2 - messageView.width,
//            height / 2 + iconSize,
//            width / 2 + messageView.width,
//            height / 2 + iconSize + messagePadding,
//        )
//    }

}