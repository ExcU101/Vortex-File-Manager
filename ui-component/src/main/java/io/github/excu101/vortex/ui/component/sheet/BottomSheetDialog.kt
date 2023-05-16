package io.github.excu101.vortex.ui.component.sheet

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Property
import android.view.Gravity
import android.view.Gravity.BOTTOM
import android.view.View
import android.view.View.*
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import android.view.WindowManager.LayoutParams.*
import android.widget.FrameLayout
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat.getInsetsController
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.manager.model.Color
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


// I hate Bottom Sheets & Android Dialogs...

open class BottomSheetDialog(
    context: Context,
) : Dialog(context), SheetContainerListener {

    private var isDismissed = false

    private val DrawableAlphaProperty =
        object : Property<ColorDrawable, Int>(Int::class.java, "alpha") {
            override fun get(`object`: ColorDrawable): Int {
                return `object`.alpha
            }

            override fun set(`object`: ColorDrawable, value: Int) {
                `object`.alpha = value
                container.invalidate()
            }
        }

    private val shape = MaterialShapeDrawable(
        builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 50F)
            .setTopRightCorner(CornerFamily.ROUNDED, 50F)
            .build()
    ).apply {
        setTint(ThemeColor(mainDrawerBackgroundColorKey))
    }

    private var animator: Animator? = null

    protected var isFullscreen = false

    private val scrim = ColorDrawable(0xff000000.toInt())

    protected val container = SheetContainer(context).apply {
        background = scrim

        registerListener(this@BottomSheetDialog)
    }

    protected val content = FrameLayout(container.context).apply {
        background = shape

    }

    init {
        shape.elevation = 24F
        requireWindow().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or FLAG_LAYOUT_IN_SCREEN)
            } else {
                addFlags(FLAG_LAYOUT_INSET_DECOR or FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or FLAG_LAYOUT_IN_SCREEN)
            }
        }
        container.setLayerType(LAYER_TYPE_HARDWARE, null)
        container.fitsSystemWindows = true
        container.registerListener(listener = this)
        ViewCompat.setOnApplyWindowInsetsListener(this@BottomSheetDialog.container) { v, insets ->
            WindowInsetsCompat.CONSUMED
        }
        scrim.alpha = 0
        container.invalidate()
    }

    fun setContent(view: View) {
        content.addView(view)
        hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requireWindow().enterTransition = null
        requireWindow().exitTransition = null
//        if (Build.VERSION.SDK_INT >= 30) {
        setDecorFitsSystemWindows(requireWindow(), false)
//        }
        setContentView(container, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        content.visibility = INVISIBLE
        container.addView(content, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            gravity = BOTTOM
        })

        requireWindow().apply {
            statusBarColor = Color.Transparent.value
            navigationBarColor = Color.Transparent.value

            val attrs = attributes

            attrs.width = MATCH_PARENT
            attrs.gravity = Gravity.TOP or Gravity.LEFT
            attrs.dimAmount = 0F
            attrs.flags = attrs.flags and FLAG_DIM_BEHIND.inv()
            attrs.height = MATCH_PARENT

            if (isFullscreen) {
                val controller = getInsetsController(this, this@BottomSheetDialog.container)
                controller.hide(statusBars())
            }

            if (Build.VERSION.SDK_INT >= 28) {
                attrs.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }

            attributes = attrs
        }
    }

    override fun show() {
        super.show()
        isDismissed = false
        cancelAnimation()

        scrim.alpha = 0
        container.invalidate()

        MainScope().launch {
            startEnterAnimation()
        }
    }

    private fun startEnterAnimation() {
        if (!isShowing) {
            return
        }
        content.visibility = VISIBLE

        content.translationY = content.measuredHeight.toFloat()
        animator = AnimatorSet().apply {
            playTogether(ObjectAnimator.ofFloat(content, TRANSLATION_Y, 0F))
            playTogether(ObjectAnimator.ofInt(scrim, DrawableAlphaProperty, 50))
            duration = 300L
            startDelay = 20L
            addListener(
                onEnd = { animation ->
                    if (animator != null && animation == animator) {
                        animator = null
                    }
                },
                onCancel = { animation ->
                    if (animator != null && animation == animator) {
                        animator = null
                    }
                }
            )
        }

        animator?.start()
    }

    override fun dismiss() {
        super.dismiss()
        if (!isShowing) {
            return
        }
        cancelAnimation()
    }

    private fun startCloseAnimation() {
        animator = AnimatorSet().apply {
            playTogether(ObjectAnimator.ofFloat(content,
                TRANSLATION_Y,
                content.measuredHeight.toFloat())
            )
            playTogether(ObjectAnimator.ofInt(scrim, DrawableAlphaProperty, 0))
            duration = 250L

            addListener(
                onEnd = { animation ->
                    clearAnimation(animation) {
                        container.removeView(content)
                        onDetachedFromWindow()
                    }
                },
                onCancel = { animation ->
                    clearAnimation(animation) {

                    }
                }
            )
        }

        animator?.start()
    }

    private inline fun clearAnimation(animation: Animator, onResult: () -> Unit = {}) {
        if (animator != null && animation == animator) {
            animator = null
            onResult()
        }
    }

    private fun cancelAnimation() {
        if (animator != null) {
            animator!!.cancel()
            animator = null
        }
    }

    override fun onNestedPreScroll(dx: Int, dy: Int, consumed: IntArray) {
        cancelAnimation()
        var currentTranslation = content.translationY
        if (currentTranslation > 0 && dy > 0) {
            currentTranslation -= dy
            consumed[1] = dy
            if (currentTranslation < 0) {
                currentTranslation = 0F
            }
            content.translationY = currentTranslation
            container.invalidate()
        }
    }

    override fun onScrollAccepted() {
        cancelAnimation()
    }

    open fun onTranslationYChanged(translationY: Float) {

    }

    protected fun requireWindow(): Window {
        return window ?: throw IllegalArgumentException()
    }
}