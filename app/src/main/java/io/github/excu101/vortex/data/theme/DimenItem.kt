package io.github.excu101.vortex.data.theme

import android.os.Parcel
import io.github.excu101.vortex.theme.model.Dimen
import io.github.excu101.vortex.ui.component.ItemViewTypes
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
class DimenItem(
    override val key: String,
    override val value: io.github.excu101.vortex.theme.model.Dimen,
) : ThemeItem<io.github.excu101.vortex.theme.model.Dimen>() {

    companion object : Parceler<io.github.excu101.vortex.theme.model.Dimen> {
        override fun create(parcel: Parcel): io.github.excu101.vortex.theme.model.Dimen {
            return io.github.excu101.vortex.theme.model.Dimen(value = parcel.readInt())
        }

        override fun io.github.excu101.vortex.theme.model.Dimen.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(this.value)
        }
    }

    override val type: Int
        get() = ItemViewTypes.DIMEN
}