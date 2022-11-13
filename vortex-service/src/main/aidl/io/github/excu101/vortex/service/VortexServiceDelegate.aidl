package io.github.excu101.vortex.service;

import io.github.excu101.vortex.service.data.ParcelablePath;

interface VortexServiceDelegate {
    ParcelablePath getPath(String scheme, in String first,in String[] other);
    List<ParcelablePath> getList(in ParcelablePath from);
}