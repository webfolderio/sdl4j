package io.webfolder.sdl4j;

public class SDL_KeyboardEvent extends SDL_Event {

    public final int sym;

    public SDL_KeyboardEvent(int type, int sym) {
        super(type);
        this.sym = sym;
    }
}
