package io.github.excu101.vortex.service.remote;

interface RemoteFileStore {

    String getName();

    String getType();

    long getTotalSpace();

    long getUsableSpace();

    long getUnallocatedSpace();

    long getBlockSize();

}