package io.github.excu101.vortex.ui.component.storage

import android.graphics.drawable.Drawable

interface StorageCell {

    var title: String?

    var info: String?

    var icon: Drawable?

    var isCellSelected: Boolean

    var isBookmarked: Boolean

}