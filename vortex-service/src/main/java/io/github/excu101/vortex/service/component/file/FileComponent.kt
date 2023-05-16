package io.github.excu101.vortex.service.component.file

import io.github.excu101.vortex.service.component.file.media.MediaComponent
import io.github.excu101.vortex.service.component.file.operation.OperationComponent

interface FileComponent {

    fun getMediaComponent(): MediaComponent

    fun getOperationComponent(): OperationComponent

}