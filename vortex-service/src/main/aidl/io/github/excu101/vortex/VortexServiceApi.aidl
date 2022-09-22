package io.github.excu101.vortex;

import io.github.excu101.vortex.service.remote.RemoteFileSystem;
import io.github.excu101.vortex.service.remote.RemoteFileSystemProvider;

interface VortexServiceApi {

    RemoteFileSystem getSystem();

    RemoteFileSystemProvider getProvider();

    void notify(int id,in String message);


}