package io.webfolder.sdl4j;

import static io.webfolder.sdl4j.SDL.*;
import static io.webfolder.sdl4j.SDL.SDL_DestroyWindow;
import static io.webfolder.sdl4j.SDL.SDL_INIT_EVERYTHING;
import static io.webfolder.sdl4j.SDL.SDL_Init;
import static io.webfolder.sdl4j.SDL.SDL_WINDOWPOS_CENTERED;
import static io.webfolder.sdl4j.SDL.SDL_WINDOW_HIDDEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SDLTest {

    @Test
    public void t001_test_init() {
        int ret = SDL_Init(SDL_INIT_EVERYTHING);
        assertEquals(0, ret);
    }

    @Test
    public void t002_test_create_window() {
        SDL_Window window = SDL_CreateWindow("sdl4j", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 640, 480, SDL_WINDOW_HIDDEN);
        assertNotNull(window);
        assertTrue(window.getPeer() > 0);
        SDL_DestroyWindow(window);
    }

    @Test
    public void t003_test_create_renderer() {
        SDL_Window window = SDL_CreateWindow("sdl4j", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 640, 480, SDL_WINDOW_HIDDEN);
        assertNotNull(window);
        SDL_Renderer renderer = SDL_CreateRenderer(window, -1, 0);
        assertTrue(renderer.getPeer() > 0);
        assertNotNull(renderer);
        SDL_DestroyRenderer(renderer);
        SDL_DestroyWindow(window);
    }

    @Test
    public void t004_test_set_logical_size() {
        SDL_Init(SDL_INIT_EVERYTHING);
        SDL_Window window = SDL_CreateWindow("sdl4j", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 640, 480, SDL_WINDOW_HIDDEN);
        SDL_Renderer renderer = SDL_CreateRenderer(window, -1, 0);
        int ret = SDL_RenderSetLogicalSize(renderer, 200, 200);
        assertEquals(0, ret);
        SDL_DestroyRenderer(renderer);
        SDL_DestroyWindow(window);        
    }

    @Test
    public void t005_test_renderer_draw_color() {
        SDL_Window window = SDL_CreateWindow("sdl4j", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 640, 480, SDL_WINDOW_HIDDEN);
        SDL_Renderer renderer = SDL_CreateRenderer(window, -1, 0);
        SDL_RenderSetLogicalSize(renderer, 200, 200);
        int ret = SDL_SetRenderDrawColor(renderer, 255, 255, 255, 0);
        assertEquals(0, ret);
        SDL_DestroyRenderer(renderer);
        SDL_DestroyWindow(window);
    }

    @Test
    public void t005_test_renderer_clear() {
        SDL_Window window = SDL_CreateWindow("sdl4j", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 640, 480, SDL_WINDOW_HIDDEN);
        SDL_Renderer renderer = SDL_CreateRenderer(window, -1, 0);
        SDL_RenderSetLogicalSize(renderer, 200, 200);
        int ret = SDL_RenderClear(renderer);
        assertEquals(0, ret);
        SDL_DestroyRenderer(renderer);
        SDL_DestroyWindow(window);        
    }

    @Test
    public void t006_test_renderer_fill_rect() {
        SDL_Window window = SDL_CreateWindow("sdl4j", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 640, 480, SDL_WINDOW_HIDDEN);
        SDL_Renderer renderer = SDL_CreateRenderer(window, -1, 0);
        SDL_RenderSetLogicalSize(renderer, 200, 200);
        SDL_Rect rectangle = new SDL_Rect(10, 10, 10, 10);
        int ret = SDL_RenderFillRect(renderer, rectangle);
        assertEquals(0, ret);
        SDL_DestroyRenderer(renderer);
        SDL_DestroyWindow(window);
    }

    @Test
    public void t006_test_delay() {
        long start = System.currentTimeMillis();
        SDL_Delay(100);
        long end = System.currentTimeMillis();
        assertTrue(end - start >= 100);
    }
}
