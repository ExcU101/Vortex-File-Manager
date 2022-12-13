package io.github.excu101.vortex.service.remote;

import io.github.excu101.vortex.service.data.ParcelablePath;
import io.github.excu101.vortex.service.remote.RemoteFileStore;
import io.github.excu101.vortex.service.data.ParcelableFileOperation;
import io.github.excu101.vortex.service.data.ParcelableFileOperationObserver;
import io.github.excu101.vortex.service.remote.RemoteReactiveFileChannel;

interface RemoteFileSystemProvider {

    void runOperation(in ParcelableFileOperation operation, in ParcelableFileOperationObserver[] observers);

    boolean isHidden(in ParcelablePath path);

    String getScheme();

    RemoteFileStore getFileStore(in ParcelablePath path);

    RemoteReactiveFileChannel newReactiveFileChannel(
        in ParcelablePath path,
        in int flags,
        int mode
    );

}