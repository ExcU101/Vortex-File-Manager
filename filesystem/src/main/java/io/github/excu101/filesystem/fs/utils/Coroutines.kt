package io.github.excu101.filesystem.fs.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob

fun IoScope() = CoroutineScope(SupervisorJob() + IO)