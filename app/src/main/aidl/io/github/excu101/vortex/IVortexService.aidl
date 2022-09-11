package io.github.excu101.vortex;

import io.github.excu101.vortex.service.VortexServiceProvider;

interface IVortexService {

    void registerProvider(in VortexServiceProvider provider);

}