package io.webfolder.sdl4j;

public class SDL_DisplayMode {

    long format;
    /** < pixel format */
    int w;
    /** < width, in screen coordinates */
    int h;
    /** < height, in screen coordinates */
    int refresh_rate; /** < refresh rate (or zero for unspecified) */
    public long getFormat() {
        return format;
    }
    public void setFormat(long format) {
        this.format = format;
    }
    public int getW() {
        return w;
    }
    public void setW(int w) {
        this.w = w;
    }
    public int getH() {
        return h;
    }
    public void setH(int h) {
        this.h = h;
    }
    public int getRefresh_rate() {
        return refresh_rate;
    }
    public void setRefresh_rate(int refresh_rate) {
        this.refresh_rate = refresh_rate;
    }
}
