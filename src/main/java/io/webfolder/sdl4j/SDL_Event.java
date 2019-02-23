package io.webfolder.sdl4j;

public class SDL_Event {

    public final int type;
    
    public SDL_Event(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public SDL_KeyboardEvent toKeyboardEvent() {
        if (this instanceof SDL_KeyboardEvent) {
            return (SDL_KeyboardEvent) this;
        } else {
            return null;
        }
    }
}
