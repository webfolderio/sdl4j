package io.webfolder.sdl4j;

public class SDL_Event {

    public final int type;

    public final SDL_KeyboardEvent key;

    public SDL_Event(int type, SDL_KeyboardEvent key) {
        this.type = type;
        this.key = key;
    }

    public int getType() {
        return type;
    }
}
