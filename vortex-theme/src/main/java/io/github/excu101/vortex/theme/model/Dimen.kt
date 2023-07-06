package io.github.excu101.vortex.theme.model

class Dimen(override val value: Int) : DataHolder<Int> {

    companion object {
        @JvmStatic
        val Zero = Dimen(value = 0)
    }

}