package io.github.excu101.vortex.navigation.dsl

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import io.github.excu101.vortex.navigation.NavigationController
import io.github.excu101.vortex.navigation.navigator.FragmentNavigator.Arguments
import io.github.excu101.vortex.navigation.navigator.Navigator.Option

interface OptionsBuilder {

    fun build(): Array<Option>

}

class Argument(
    var key: String? = null,
    var item: Parcelable? = null,
)

inline fun OptionsBuilder.argument(block: Argument.() -> Unit) {
    val argument = Argument().apply(block)
    (this as OptionsBuilderImpl).addArgument(argument.key, argument.item)
}

@PublishedApi
internal class OptionsBuilderImpl : OptionsBuilder {
    private var args: Bundle? = null
    private val options = mutableListOf<Option>()

    fun addArgument(key: String?, item: Parcelable?) {
        if (args == null) {
            args = Bundle()
        }

        args?.putParcelable(key, item)
    }

    override fun build(): Array<Option> {
        if (args != null) {
            options += Arguments(original = args!!)
        }

        return options.toTypedArray()
    }
}

inline fun NavigationController.navigate(
    route: String,
    block: OptionsBuilder.() -> Unit,
) {
    navigate(route, *OptionsBuilderImpl().apply(block).build())
}

fun Arguments(vararg pairs: Pair<String, Any?>): Arguments {
    return Arguments(bundleOf(*pairs))
}