package io.github.excu101.vortex.service.remote;

interface RemoteReactiveFileChannel {

    RemoteReactiveFileChannel write();

    RemoteReactiveFileChannel read();

    void close();

}