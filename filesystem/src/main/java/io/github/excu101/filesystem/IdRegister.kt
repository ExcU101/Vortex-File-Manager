package io.github.excu101.filesystem

import kotlin.random.Random
import kotlin.random.nextInt

object IdRegister {

    private val _data: ArrayList<Pair<Int, Type>> = ArrayList()
    val data: List<Pair<Int, Type>>
        get() = _data

    fun register(index: Int, type: Type): Boolean = if (_data.contains(index to type)) {
        false
    } else {
        _data.add(index to type)
        true
    }

    fun register(type: Type): Int {
        var index = 0
        while (_data.contains(index to type)) {
            index = Random.nextInt(0..Int.MAX_VALUE)
        }
        _data.add(index to type)
        return index
    }

    enum class Type {
        PATH,
        OPERATION,
        MODEL
    }

}