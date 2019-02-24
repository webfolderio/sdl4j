package io.webfolder.sdl4j;

public class SDL_KeyboardEvent {

    public final SDL_Keysym keysym;

    public SDL_KeyboardEvent(int sym) {
        this.keysym = new SDL_Keysym(sym);
    }
}
