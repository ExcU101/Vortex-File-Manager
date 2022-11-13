package io.github.excu101.vortex.ui.component.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

interface FragmentFactory<T : Fragment> {

    fun create(args: Bundle?): T {
        val fragment = createFragment()
        fragment.arguments = args
        return fragment
    }

    fun createFragment(): T

    class Default<T : Fragment>(private val clazz: KClass<T>) : FragmentFactory<T> {
        override fun createFragment(): T {
            return clazz.constructors.first().call()
        }
    }
}

