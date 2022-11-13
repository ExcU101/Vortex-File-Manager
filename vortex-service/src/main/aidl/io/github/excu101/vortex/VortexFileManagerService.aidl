package io.github.excu101.vortex;

import io.github.excu101.vortex.service.remote.RemoteFileSystem;
import io.github.excu101.vortex.service.data.VortexServiceInfo;
import io.github.excu101.vortex.service.remote.RemoteFileSystemProvider;
import io.github.excu101.vortex.service.data.ParcelablePath;

interface VortexFileManagerService {

    void install(RemoteFileSystem system);

    RemoteFileSystem getSystem(in String scheme);

    RemoteFileSystemProvider getProvider(in String scheme);

    RemoteFileSystem getDefaultSystem();

    RemoteFileSystemProvider getDefaultProvider();

    VortexServiceInfo getInfo();

}