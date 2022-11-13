package io.github.excu101.pluginsystem.annotation

import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.*

@Target(
    FIELD,
    PROPERTY,
    TYPE_PARAMETER
)
@Retention(SOURCE)
annotation class FixedDimen
