package io.github.excu101.vortex.data.theme

import android.os.Parcel
import io.github.excu101.manager.model.Dimen
import io.github.excu101.vortex.ui.component.ItemViewTypes
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
class DimenItem(
    override val key: String,
    override val value: Dimen,
) : ThemeItem<Dimen>() {

    companion object : Parceler<Dimen> {
        override fun create(parcel: Parcel): Dimen {
            return Dimen(value = parcel.readInt())
        }

        override fun Dimen.write(parcel: Parcel, flags: Int) {
            parcel.writeInt(this.value)
        }
    }

    override val type: Int
        get() = ItemViewTypes.DIMEN
}