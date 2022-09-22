package io.github.excu101.vortex.service.data

import android.os.Parcelable
import io.github.excu101.filesystem.IdRegister
import io.github.excu101.filesystem.fs.operation.FileOperation
import kotlinx.parcelize.Parcelize

@Parcelize
open class ParcelableFileOperation(
    override val id: Int = IdRegister.register(IdRegister.Type.OPERATION),
) : FileOperation(), Parcelable {


    override suspend fun perform() {

    }

}