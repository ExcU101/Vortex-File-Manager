package io.github.excu101.vortex.ui.component.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.addListener

fun FadeInAnimation(target: View) = AnimatorSet().apply {
    playTogether(ObjectAnimator.ofFloat(target, View.ALPHA, 0.0f, 1.0f))
}

fun FadeOutAnimation(target: View) = AnimatorSet().apply {
    playTogether(ObjectAnimator.ofFloat(target, View.ALPHA, 1.0f, 0.0f))
}