package io.github.excu101.vortex.navigation.page

fun interface BackListener {

    fun onBackActivated(): Boolean

}

fun BackListener(): BackListener {
    return BackListener { false }
}