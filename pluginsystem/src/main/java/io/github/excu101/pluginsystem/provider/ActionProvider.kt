package io.github.excu101.pluginsystem.provider

import io.github.excu101.pluginsystem.model.Action

interface ActionProvider {

    fun getActions(): List<Action>

}