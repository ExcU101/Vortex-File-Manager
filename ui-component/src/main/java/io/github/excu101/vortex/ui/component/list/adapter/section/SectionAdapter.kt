package io.github.excu101.vortex.ui.component.list.adapter.section

import android.util.SparseIntArray
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.github.excu101.vortex.ui.component.list.adapter.selection.SelectionAdapter

abstract class SectionAdapter<T, VH : ViewHolder> : SelectionAdapter<T, VH>() {

    private var sectionPositionCache: SparseIntArray? = null
    private var sectionCache: SparseIntArray? = null
    private var sectionCountCache: SparseIntArray? = null
    abstract var sectionCount: Int

    override fun isSelected(position: Int): Boolean = isSelected(
        getSectionForPosition(position),
        getPositionInSectionForPosition(position)
    )

    abstract fun isSelected(
        section: Int,
        position: Int,
    ): Boolean

    override fun item(position: Int): T {
        return item(
            section = getSectionForPosition(position),
            position = getPositionInSectionForPosition(position)
        )
    }

    override fun getItemCount(): Int {
        var count = 0
        for (i in 0..sectionCount) {
            count += internalGetCountForSection(i)
        }

        return count
    }

    private fun cleanupCache() {
        if (sectionCache == null) {
            sectionCache = SparseIntArray()
            sectionPositionCache = SparseIntArray()
            sectionCountCache = SparseIntArray()
        } else {
            sectionCache!!.clear()
            sectionPositionCache!!.clear()
            sectionCountCache!!.clear()
        }
        sectionCount = -1
    }

    private fun internalGetCountForSection(section: Int): Int {
        val cachedSectionCount = sectionCountCache!![section, Int.MAX_VALUE]
        if (cachedSectionCount != Int.MAX_VALUE) {
            return cachedSectionCount
        }
        val sectionCount = getCountForSection(section)
        sectionCountCache!!.put(section, sectionCount)
        return sectionCount
    }

    fun getSectionForPosition(position: Int): Int {
        val cachedSection: Int = sectionCache!!.get(position, Int.MAX_VALUE)
        if (cachedSection != Int.MAX_VALUE) {
            return cachedSection
        }
        var sectionStart = 0
        var i = 0
        val n: Int = sectionCount
        while (i < n) {
            val sectionCount: Int = internalGetCountForSection(i)
            val sectionEnd = sectionStart + sectionCount
            if (position in sectionStart until sectionEnd) {
                sectionCache!!.put(position, i)
                return i
            }
            sectionStart = sectionEnd
            i++
        }
        return -1
    }

    open fun getPositionInSectionForPosition(position: Int): Int {
        val cachedPosition: Int = sectionPositionCache!!.get(position, Int.MAX_VALUE)
        if (cachedPosition != Int.MAX_VALUE) {
            return cachedPosition
        }
        var sectionStart = 0
        var i = 0
        val n: Int = sectionCount
        while (i < n) {
            val sectionCount: Int = internalGetCountForSection(i)
            val sectionEnd = sectionStart + sectionCount
            if (position in sectionStart until sectionEnd) {
                val positionInSection = position - sectionStart
                sectionPositionCache!!.put(position, positionInSection)
                return positionInSection
            }
            sectionStart = sectionEnd
            i++

        }
        return -1
    }

    abstract fun getCountForSection(section: Int): Int

    abstract fun getItemViewType(section: Int, position: Int): Int

    abstract fun item(section: Int, position: Int): T

    abstract fun onBindViewHolder(section: Int, position: Int, holder: ViewHolder)

}