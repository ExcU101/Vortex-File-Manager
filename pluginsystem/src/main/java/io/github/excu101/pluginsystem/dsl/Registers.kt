package io.github.excu101.pluginsystem.dsl

import android.graphics.drawable.Drawable
import android.view.View
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.model.Plugin
import io.github.excu101.pluginsystem.provider.Managers
import io.github.excu101.pluginsystem.utils.EmptyDrawable
import kotlin.reflect.KClass

@DslMarker
annotation class RegistersMarker

@RegistersMarker
class Registers(val plugin: Plugin) {

    inner class GroupRegister(
        var name: String,
        var icon: Drawable? = null,
    ) {
        private val _actions: MutableList<Action> = mutableListOf()
        val actions: Collection<Action>
            get() = _actions

        inner class RegistersAction(
            val plugin: Plugin,
            var title: String = "",
            var icon: Drawable = EmptyDrawable,
            var operation: KClass<out FileOperation>? = null,
        ) {
            fun toAction(): Action {
                return Action(title, icon)
            }

            fun toPair(): Pair<Action, KClass<out FileOperation>?> {
                return toAction() to operation
            }
        }

        inline fun action(block: RegistersAction.() -> Unit) {
            val (action, operation) = RegistersAction(plugin).apply(block).toPair()
            register(
                Action(
                    title = action.title,
                    icon = action.icon,
                )
            )
            operation?.let {
                register(operation = it)
            }
        }

        fun register(action: Action, operation: KClass<out FileOperation>) {
            register(operation)
            register(action)
        }

        fun register(operation: KClass<out FileOperation>) {
            Managers.Operation.register(plugin, operation)
        }

        fun register(action: Action) {
            _actions.add(action)
        }

        fun unregister() {
            Managers.Operation.unregister(plugin)
        }
    }

    inline fun Plugin.registerGroup(block: GroupRegister.() -> Unit) {
        val groupBlock = GroupRegister(name = attributes.name).apply(block)

        Managers.Group.register(
            plugin = this,
            value = GroupAction(
                name = groupBlock.name,
                actions = groupBlock.actions
            )
        )
    }

    fun Plugin.unregisterGroup() {
        Managers.Group.unregister(plugin = this)
    }
}

inline fun Plugin.registers(block: Registers.() -> Unit) {
    Registers(plugin = this).apply(block)
}