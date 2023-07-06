package io.github.excu101.vortex.service

import android.os.Binder
import io.github.excu101.vortex.di.ServiceComponent


class VortexServiceBinder internal constructor(
    internal val component: ServiceComponent,
) : Binder()