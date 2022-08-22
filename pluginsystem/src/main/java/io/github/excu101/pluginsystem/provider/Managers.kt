package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.provider.impl.GroupManagerImpl
import io.github.excu101.pluginsystem.provider.impl.OperationManagerImpl

object Managers {
    val Group: GroupManager = GroupManagerImpl()
    val Operation: OperationManager = OperationManagerImpl()
}