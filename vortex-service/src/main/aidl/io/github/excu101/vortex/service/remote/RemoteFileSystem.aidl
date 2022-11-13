package io.github.excu101.vortex.service.remote;

import io.github.excu101.vortex.service.data.ParcelablePath;
import io.github.excu101.vortex.service.remote.RemoteFileSystemProvider;

interface RemoteFileSystem {

    boolean isOpen();

    boolean isReadOnly();

    byte getSeparator();

    RemoteFileSystemProvider getProvider();

    String getScheme();

    ParcelablePath getPath(String segment, in String[] other);

}