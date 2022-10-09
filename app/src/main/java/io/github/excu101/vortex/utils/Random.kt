package io.github.excu101.vortex.utils

import kotlin.random.Random.Default.nextInt

private val names = listOf(
    "John",
    "Marie",
    "Max",
    "Susie",
    "Alex",
    "Sasha",
    "Victoria",
    "Valerica",
    "Valeria"
)

fun RandomString(): String {
    return names[nextInt(0, names.lastIndex)] + nextInt()
}