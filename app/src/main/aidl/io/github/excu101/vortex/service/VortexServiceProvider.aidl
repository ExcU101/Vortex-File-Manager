package io.github.excu101.vortex.service;

import io.github.excu101.vortex.data.PathItem;

interface VortexServiceProvider {
    void navigateTo(in PathItem item);
    void runCommand(String name);
}