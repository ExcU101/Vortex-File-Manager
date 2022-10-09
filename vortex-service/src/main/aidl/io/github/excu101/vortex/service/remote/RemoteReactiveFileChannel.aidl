package io.github.excu101.vortex.service.remote;

import io.github.excu101.vortex.service.listener.ParcelableFileChannelListener;

interface RemoteReactiveFileChannel {

    RemoteReactiveFileChannel write();

    RemoteReactiveFileChannel read();

    RemoteReactiveFileChannel addListener(in ParcelableFileChannelListener listener);

    RemoteReactiveFileChannel removeListener(in ParcelableFileChannelListener listener);

    void close();

}