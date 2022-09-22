package io.github.excu101.vortex.service.remote;

import io.github.excu101.vortex.service.data.ParcelablePath;
import io.github.excu101.vortex.service.remote.RemoteFileStore;
import io.github.excu101.vortex.service.data.ParcelableFileOperation;
import io.github.excu101.vortex.service.data.ParcelableFileOperationObserver;

interface RemoteFileSystemProvider {

    void runOperation(in ParcelableFileOperation operation, in ParcelableFileOperationObserver[] observers);

    boolean isHidden(in ParcelablePath path);

    String getScheme();

    RemoteFileStore getFileStore(in ParcelablePath path);

}