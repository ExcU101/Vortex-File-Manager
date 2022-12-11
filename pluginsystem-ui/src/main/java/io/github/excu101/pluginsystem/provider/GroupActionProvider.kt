package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.model.GroupAction

interface GroupActionProvider {

    fun getGroups(): List<GroupAction>

}