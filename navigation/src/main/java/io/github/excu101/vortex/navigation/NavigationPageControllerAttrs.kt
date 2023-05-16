package io.github.excu101.vortex.navigation

import androidx.annotation.IntDef

@IntDef(
    flag = true, value = [
        TRANSITION_FADE,
        TRANSITION_SLIDE_VERTICAL,
        TRANSITION_SLIDE_HORIZONTAL
    ]
)
@Retention(AnnotationRetention.SOURCE)
annotation class Transition

const val TRANSITION_FADE = 0
const val TRANSITION_SLIDE_VERTICAL = 1 shl 1
const val TRANSITION_SLIDE_HORIZONTAL = 1 shl 2

@IntDef(
    flag = true, value = [
        DIRECTION_FORWARD,
        DIRECTION_BACKWARD,
    ]
)
annotation class Direction

const val DIRECTION_FORWARD = 1 shl 3
const val DIRECTION_BACKWARD = 1 shl 4

fun mode(
    @Transition
    transition: Int = TRANSITION_FADE,
    @Direction
    direction: Int = DIRECTION_FORWARD,
    duration: Int = 300,
): Int = transition or direction or duration