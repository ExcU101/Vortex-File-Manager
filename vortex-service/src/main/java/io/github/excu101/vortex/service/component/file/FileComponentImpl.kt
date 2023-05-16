package io.github.excu101.vortex.service.component.file

import android.content.Context
import io.github.excu101.vortex.service.component.file.media.MediaComponent
import io.github.excu101.vortex.service.component.file.media.MediaComponentImpl
import io.github.excu101.vortex.service.component.file.operation.OperationComponent
import io.github.excu101.vortex.service.component.file.operation.OperationComponentImpl

internal class FileComponentImpl(
    context: Context
) : FileComponent {

    internal val media = MediaComponentImpl(context)
    internal val operation = OperationComponentImpl()

    override fun getMediaComponent(): MediaComponent = media

    override fun getOperationComponent(): OperationComponent = operation

}