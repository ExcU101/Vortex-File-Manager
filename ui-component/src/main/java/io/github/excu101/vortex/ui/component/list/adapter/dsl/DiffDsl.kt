package io.github.excu101.vortex.ui.component.list.adapter.dsl

@ViewHolderMarker
interface DiffScope<E> {
    var areItemsSame: (old: E, new: E) -> Boolean
    var areContentsSame: (old: E, new: E) -> Boolean
}

fun <E> ViewHolderScope.diff(scope: DiffScope<E>.() -> Unit) {

}