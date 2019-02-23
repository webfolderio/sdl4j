package io.webfolder.sdl4j;

public class SDL_Rect {
    
    public int x;

    public int y;

    public int w;

    public int h;

    public SDL_Rect() {
        // no op
    }

    public SDL_Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
