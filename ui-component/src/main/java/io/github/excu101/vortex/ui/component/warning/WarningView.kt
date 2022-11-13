package io.github.excu101.vortex.ui.component.warning

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.ColorStateList.valueOf
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT
import androidx.core.view.contains
import androidx.core.view.updatePadding
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.MaterialShapeDrawable
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.pluginsystem.utils.EmptyDrawable
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.storageListWarningActionContentColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListWarningBackgroundColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListWarningIconTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListWarningTitleTextColorKey
import org.xml.sax.Attributes

class WarningView : ThemeLinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private val listeners = mutableListOf<WarningActionListener>()

    private val iconSize = 56.dp
    private val messagePadding = 32.dp

    private val iconView = ImageView(context).apply {
        minimumHeight = iconSize
        minimumWidth = iconSize
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setColorFilter(ThemeColor(storageListWarningIconTintColorKey))
    }

    private val messageView = TextView(context).apply {
        textSize = 18F
        layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setTextColor(ThemeColor(storageListWarningTitleTextColorKey))
        updatePadding(top = 16)
    }

    private val actions: MutableList<Action> = arrayListOf()
    private val views = arrayListOf<View>()

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
        initViews()
    }

    fun replaceActions(values: Collection<Action>) {
        actions.clear()
        actions.addAll(values)
        initViews()
    }

    fun removeAction(value: Action) {
        actions.remove(value)
        initViews()
    }

    private fun initViews() {
        views.forEach {
            removeView(it)
        }
        views.clear()
        for (item in actions) {
            views.add(getItem(item))
        }
        for (view in views) {
            addView(view)
        }
    }

    fun registerListener(listener: WarningActionListener) {
        listeners.add(listener)
    }

    private fun getItem(action: Action): MaterialButton {
        return MaterialButton(
            context,
        ).apply {
            icon = action.icon
            text = action.title
            updatePadding(
                top = 4.dp,
                bottom = 4.dp
            )
            backgroundTintList = valueOf(ThemeColor(storageListWarningBackgroundColorKey))
            elevation = 0F
            rippleColor = valueOf(ThemeColor(storageListWarningActionContentColorKey))
            iconTint = valueOf(ThemeColor(storageListWarningActionContentColorKey))
            setTextColor(ThemeColor(storageListWarningActionContentColorKey))
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            setOnClickListener { view ->
                listeners.forEach { listener ->
                    listener.onWarningActionCall(action)
                }
            }
        }
    }

    init {
        orientation = VERTICAL
        gravity = CENTER
        setBackgroundColor(ThemeColor(storageListWarningBackgroundColorKey))
    }

    override fun onChanged() {
        iconView.setColorFilter(ThemeColor(storageListWarningIconTintColorKey))
        messageView.setTextColor(ThemeColor(storageListWarningTitleTextColorKey))
        setBackgroundColor(ThemeColor(storageListWarningBackgroundColorKey))
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