package io.github.excu101.vortex.service.remote;

import io.github.excu101.vortex.service.data.ParcelablePath;

interface RemoteFileSystem {

    boolean isOpen();

    boolean isReadOnly();

    byte getSeparator();

    String getScheme();

    ParcelablePath getPath(String segment, in String[] other);

}