package io.github.excu101.vortex.service.remote;

interface RemoteFileStore {

    long getTotalSpace();

    long getUsableSpace();

    long getUnallocatedSpace();

}