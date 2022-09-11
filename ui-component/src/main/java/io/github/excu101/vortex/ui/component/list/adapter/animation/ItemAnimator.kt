package io.github.excu101.vortex.ui.component.list.adapter.animation

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.SimpleItemAnimator

abstract class ItemAnimator : SimpleItemAnimator() {

    object AnimationDurations {
        var add: Long = 120L
        var remove: Long = 120L
        var move: Long = 250L
        var change: Long = 250L
    }

    private val pendingRemovals = arrayListOf<ViewHolder>()
    private val pendingAdditions = arrayListOf<ViewHolder>()
    private val pendingChanges = arrayListOf<ChangeInfo>()
    private val pendingMoves = arrayListOf<MoveInfo>()

    private val listAdditions = arrayListOf<ArrayList<ViewHolder>>()
    private val listMoves = arrayListOf<ArrayList<MoveInfo>>()
    private val listChanges = arrayListOf<ArrayList<ChangeInfo>>()

    private val addAnimations = arrayListOf<ViewHolder>()
    private val removeAnimations = arrayListOf<ViewHolder>()
    private val changeAnimations = arrayListOf<ViewHolder>()
    private val moveAnimations = arrayListOf<ViewHolder>()

    private val isAddAnimationRunning: Boolean
        get() = addAnimations.isNotEmpty() || pendingAdditions.isNotEmpty()

    private val isRemoveAnimationRunning: Boolean
        get() = removeAnimations.isNotEmpty() || pendingRemovals.isNotEmpty()

    private val isMoveAnimationRunning: Boolean
        get() = moveAnimations.isNotEmpty() || pendingMoves.isNotEmpty()

    private val isChangeAnimationRunning: Boolean
        get() = changeAnimations.isNotEmpty() || pendingChanges.isNotEmpty()

    private class MoveInfo constructor(
        var holder: ViewHolder,
        var fromX: Int,
        var fromY: Int,
        var toX: Int,
        var toY: Int,
    )

    private class ChangeInfo constructor(
        var oldHolder: ViewHolder?,
        var newHolder: ViewHolder?,
        var fromX: Int = 0,
        var fromY: Int = 0,
        var toX: Int = 0,
        var toY: Int = 0,
    ) {
        override fun toString(): String {
            return "ChangeInfo{" +
                    "oldHolder=" + oldHolder +
                    ", newHolder=" + newHolder +
                    ", fromX=" + fromX +
                    ", fromY=" + fromY +
                    ", toX=" + toX +
                    ", toY=" + toY +
                    '}'
        }
    }

    override fun runPendingAnimations() {
        val animatesMove = pendingMoves.isNotEmpty()
        val animatesRemove = pendingRemovals.isNotEmpty()
        val animatesAdd = pendingAdditions.isNotEmpty()
        val animatesChange = pendingChanges.isNotEmpty()

        if (!animatesRemove && !animatesAdd && !animatesChange && !animatesMove) return

        pendingRemovals.forEach(::animateRemoveImpl)
        pendingRemovals.clear()

        if (animatesMove) {
            val moves = arrayListOf<MoveInfo>()
            moves.addAll(pendingMoves)
            listMoves.add(moves)
            pendingChanges.clear()
            val changer = Runnable {
                moves.forEach {
                    animateMoveImpl(
                        holder = it.holder,
                        fromX = it.fromX,
                        fromY = it.fromY,
                        toX = it.toX,
                        toY = it.toY
                    )
                }
                moves.clear()
                listMoves.remove(moves)
            }
            if (animatesRemove) {
                val itemView = moves[0].holder.itemView
                itemView.postOnAnimationDelayed(changer, AnimationDurations.remove)
            } else {
                changer.run()
            }
        }

        if (animatesChange) {
            val changes = arrayListOf<ChangeInfo>()
            changes.addAll(pendingChanges)
            listChanges.add(changes)
            pendingChanges.clear()
            val changer = Runnable {
                changes.forEach {
                    animateChangeImpl(
                        oldHolder = it.oldHolder,
                        newHolder = it.newHolder,
                        fromX = it.fromX,
                        fromY = it.fromY,
                        toX = it.toX,
                        toY = it.toY
                    )
                }
                changes.clear()
                listChanges.remove(changes)
            }
            if (animatesRemove) {
                val itemView = changes.firstOrNull()?.oldHolder?.itemView
                itemView?.postOnAnimationDelayed(changer, AnimationDurations.remove)
            } else {
                changer.run()
            }
        }
    }

    override fun endAnimation(item: ViewHolder) {
        val itemView = item.itemView

        itemView.animate().cancel()

        pendingMoves.forEachIndexed { index, info ->
            val holder = info.holder
            if (holder == item) {
                holder.itemView.apply {
                    translationX = 0F
                    translationY = 0F
                }
                dispatchMoveFinished(holder)
                pendingMoves.removeAt(index)
            }
        }

        pendingChanges.forEachIndexed { index, info ->
            if (endChangeAnimationIfNecessary(info, item)) {
                if (info.oldHolder == null && info.newHolder == null) {
                    pendingChanges.removeAt(index)
                }
            }
        }

        if (pendingRemovals.remove(item)) {
            cleanupRemoveAnimation(itemView)
            dispatchRemoveFinished(item)
        }

        if (pendingAdditions.remove(item)) {
            cleanupAddAnimation(itemView)
            dispatchAddFinished(item)
        }

        for (moves in listMoves) {
            moves.forEachIndexed { index, info ->
                val holder = info.holder
                if (holder == item) {
                    holder.itemView.apply {
                        translationX = 0F
                        translationY = 0F
                        dispatchMoveFinished(holder)
                        moves.removeAt(index)
                        if (moves.isEmpty()) listMoves.remove(moves)
                    }
                }
            }
        }

        for (additions in listAdditions) {
            additions.forEachIndexed { index, holder ->
                if (item == holder) {
                    holder.itemView.apply {
                        alpha = 1F
                        dispatchAddFinished(holder)
                        additions.removeAt(index)
                        if (additions.isEmpty()) listAdditions.remove(additions)
                    }
                }
            }
        }
        for (changes in listChanges) {
            changes.forEachIndexed { index, info ->
                if (endChangeAnimationIfNecessary(info, item)) {
                    if (info.oldHolder == null && info.newHolder == null) {
                        changes.remove(info)
                    }
                }
                if (changes.isEmpty()) listChanges.remove(changes)
            }
        }

        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    override fun endAnimations() {
        pendingMoves.forEachIndexed { index, info ->
            val holder = info.holder
            holder.itemView.apply {
                translationX = 0F
                translationY = 0F
            }
            dispatchMoveFinished(holder)
            pendingMoves.removeAt(index)
        }
        pendingRemovals.forEachIndexed { index, holder ->
            dispatchRemoveFinished(holder)
            pendingRemovals.removeAt(index)
        }
        pendingAdditions.forEachIndexed { index, holder ->
            holder.itemView.apply {
                alpha = 1F
            }
            dispatchAddFinished(holder)
            pendingAdditions.removeAt(index)
        }
        pendingChanges.forEach(::endChangeAnimationIfNecessary)
        pendingChanges.clear()

        for (moves in listMoves) {
            moves.forEachIndexed { index, info ->
                val holder = info.holder
                holder.itemView.apply {
                    translationX = 0F
                    translationY = 0F
                    dispatchMoveFinished(holder)
                    moves.removeAt(index)
                    if (moves.isEmpty()) listMoves.remove(moves)
                }
            }
        }

        for (additions in listAdditions) {
            additions.forEachIndexed { index, holder ->
                holder.itemView.apply {
                    alpha = 1F
                    dispatchAddFinished(holder)
                    additions.removeAt(index)
                    if (additions.isEmpty()) listAdditions.remove(additions)
                }
            }
        }
        for (changes in listChanges) {
            changes.forEachIndexed { index, info ->
                endChangeAnimationIfNecessary(info)
                if (changes.isEmpty()) listChanges.remove(changes)
            }
        }

        cancel(moveAnimations)
        cancel(removeAnimations)
        cancel(addAnimations)
        cancel(changeAnimations)

        dispatchAnimationsFinished()
    }

    private fun endChangeAnimationIfNecessary(info: ChangeInfo) {
        if (info.oldHolder != null) {
            endChangeAnimationIfNecessary(info, info.oldHolder)
        }
        if (info.newHolder != null) {
            endChangeAnimationIfNecessary(info, info.newHolder)
        }
    }

    private fun endChangeAnimationIfNecessary(
        info: ChangeInfo,
        item: ViewHolder?,
    ): Boolean {
        var oldItem = false
        if (info.newHolder == item) {
            info.newHolder = null
        } else if (info.oldHolder == item) {
            info.oldHolder = null
            oldItem = true
        } else {
            return false
        }
        item?.itemView?.apply {
            alpha = 1F
            translationX = 0F
            translationY = 0F
        }
        dispatchChangeFinished(item, oldItem)
        return true
    }

    private fun reset(holder: ViewHolder) {
        endAnimation(item = holder)
    }

    override fun isRunning(): Boolean {
        return isAddAnimationRunning || isRemoveAnimationRunning || isMoveAnimationRunning || isChangeAnimationRunning
    }

    override fun animateRemove(holder: ViewHolder): Boolean {
        reset(holder = holder)
        pendingRemovals.add(holder)
        return true
    }

    override fun animateAdd(holder: ViewHolder): Boolean {
        reset(holder = holder)
        holder.itemView.alpha = 0F
        pendingAdditions.add(holder)
        return true
    }

    override fun animateChange(
        oldHolder: ViewHolder?,
        newHolder: ViewHolder?,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int,
    ): Boolean {
        if (oldHolder === newHolder) {
            // Don't know how to run change animations when the same view holder is re-used.
            // run a move animation to handle position changes.
            return animateMove(oldHolder!!, fromLeft, fromTop, toLeft, toTop)
        }
        val prevTranslationX = oldHolder!!.itemView.translationX
        val prevTranslationY = oldHolder.itemView.translationY
        val prevAlpha = oldHolder.itemView.alpha
        reset(oldHolder)
        val deltaX: Int = (toLeft - fromLeft - prevTranslationX).toInt()
        val deltaY: Int = (toTop - fromTop - prevTranslationY).toInt()
        pendingChanges.add(
            ChangeInfo(
                oldHolder,
                newHolder,
                fromLeft,
                fromTop,
                toLeft,
                toTop)
        )
        return true
    }

    override fun animateMove(
        holder: ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int,
    ): Boolean {
        var x = fromX
        var y = fromY
        val view = holder.itemView
        x += holder.itemView.translationX.toInt()
        y += holder.itemView.translationY.toInt()
        reset(holder)
        val deltaX = toX - x
        val deltaY = toY - y
        if (deltaX == 0 && deltaY == 0) {
            dispatchMoveFinished(holder)
            return false
        }
        if (deltaX != 0) {
            view.translationX = -deltaX.toFloat()
        }
        if (deltaY != 0) {
            view.translationY = -deltaY.toFloat()
        }

        pendingMoves.add(MoveInfo(holder, fromX, fromY, toX, toY))
        return true
    }

    override fun canReuseUpdatedViewHolder(
        viewHolder: ViewHolder,
        payloads: MutableList<Any>,
    ): Boolean {
        return payloads.isNotEmpty() || super.canReuseUpdatedViewHolder(viewHolder, payloads)
    }

    private fun cancel(holders: List<ViewHolder>) {
        holders.forEach {
            it.itemView.animate().cancel()
        }
    }

    // REGISTERING
    protected fun registerRemoveAnimation(holder: ViewHolder) {
        addAnimations.add(holder)
    }

    protected fun unregisterRemoveAnimation(holder: ViewHolder) {
        addAnimations.remove(holder)
    }

    protected fun registerAddAnimation(holder: ViewHolder) {
        addAnimations.add(holder)
    }

    protected fun unregisterAddAnimation(holder: ViewHolder) {
        addAnimations.remove(holder)
    }

    protected fun registerMoveAnimation(holder: ViewHolder) {
        moveAnimations.add(holder)
    }

    protected fun unregisterMoveAnimation(holder: ViewHolder) {
        moveAnimations.remove(holder)
    }

    protected fun registerChangeAnimation(holder: ViewHolder) {
        changeAnimations.add(holder)
    }

    protected fun unregisterChangeAnimation(holder: ViewHolder) {
        changeAnimations.remove(holder)
    }

    // PREPARING & CLEANINGUP
    protected open fun prepareRemoveAnimation(holder: ViewHolder) {
        prepareAddAnimation(view = holder.itemView)
    }

    protected open fun prepareRemoveAnimation(view: View) {
        view.alpha = 1F
    }

    protected open fun cleanupRemoveAnimation(holder: ViewHolder) {
        cleanupRemoveAnimation(view = holder.itemView)
    }

    protected open fun cleanupRemoveAnimation(view: View) {
        view.alpha = 0F
    }

    protected open fun prepareAddAnimation(holder: ViewHolder) {
        prepareAddAnimation(view = holder.itemView)
    }

    protected open fun prepareAddAnimation(view: View) {
        view.alpha = 0F
    }

    protected open fun cleanupAddAnimation(holder: ViewHolder) {
        cleanupAddAnimation(view = holder.itemView)
    }

    protected open fun cleanupAddAnimation(view: View) {
        view.alpha = 1F
    }

    protected abstract fun animateRemoveImpl(holder: ViewHolder)

    protected abstract fun animateAddImpl(holder: ViewHolder)

    protected abstract fun animateChangeImpl(
        oldHolder: ViewHolder?,
        newHolder: ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int,
    )

    protected abstract fun animateMoveImpl(
        holder: ViewHolder,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int,
    )

}