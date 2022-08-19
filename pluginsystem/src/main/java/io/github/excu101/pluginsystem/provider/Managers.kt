package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.provider.impl.GroupManagerImpl
import io.github.excu101.pluginsystem.provider.impl.OperationManagerImpl
import io.github.excu101.pluginsystem.provider.impl.ScreenManagerImpl

object Managers {
    val Group: GroupManager = GroupManagerImpl()
    val Screen: ScreenManager = ScreenManagerImpl()
    val Operation: OperationManager = OperationManagerImpl()
}