package io.github.excu101.vortex.ui.component.trail

import android.animation.Animator
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.list.adapter.animation.ItemAnimator

object TrailItemAnimator : ItemAnimator() {

    override fun prepareRemoveAnimation(view: View) {
        view.translationY = 0F
    }

    override fun cleanupRemoveAnimation(view: View) {
        view.translationY = -view.height.toFloat()
    }

    override fun prepareAddAnimation(view: View) {
        view.translationY = view.height.toFloat()
    }

    override fun cleanupAddAnimation(view: View) {
        view.translationY = 0F
    }

    override fun animateRemoveImpl(holder: RecyclerView.ViewHolder) {
        val view = holder.itemView
        val animator = view.animate()
        registerRemoveAnimation(holder)
        animator
            .translationY(-view.height.toFloat())
            .setDuration(AnimationDurations.remove)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    dispatchRemoveStarting(holder)
                }

                override fun onAnimationEnd(animation: Animator) {
                    animator.setListener(null)
                    dispatchRemoveFinished(holder)
                    unregisterRemoveAnimation(holder)
                    if (!isRunning) {
                        dispatchAnimationsFinished()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    cleanupRemoveAnimation(view)
                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })

        animator.start()
    }

    override fun animateAddImpl(holder: RecyclerView.ViewHolder) {
        val view = holder.itemView
        val animator = view.animate()
        registerAddAnimation(holder)
        animator
            .translationY(0F)
            .setDuration(AnimationDurations.add)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    dispatchAddStarting(holder)
                }

                override fun onAnimationEnd(animation: Animator) {
                    animator.setListener(null)
                    dispatchAddFinished(holder)
                    unregisterAddAnimation(holder)
                    if (!isRunning) {
                        dispatchAnimationsFinished()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    cleanupAddAnimation(view)
                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })

        animator.start()
    }

    override fun animateChangeImpl(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int,
    ) {

    }

    override fun animateMoveImpl(
        holder: RecyclerView.ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int,
    ) {

    }
}