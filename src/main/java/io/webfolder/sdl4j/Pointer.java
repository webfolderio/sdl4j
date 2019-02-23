package io.webfolder.sdl4j;

public abstract class Pointer {

    private long peer;

    public Pointer(long peer) {
        this.peer = peer;
    }

    public long getPeer() {
        return peer;
    }
}
