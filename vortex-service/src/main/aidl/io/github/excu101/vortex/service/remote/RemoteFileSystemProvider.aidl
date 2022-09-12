package io.github.excu101.vortex.service.remote;

import io.github.excu101.vortex.service.data.ParcelablePath;
import io.github.excu101.vortex.service.api.FileStoreInfo;

interface RemoteFileSystemProvider {
    void isHidden(in ParcelablePath path);

    FileStoreInfo getFileStoreInfo(in ParcelablePath path);

    String getScheme();
}