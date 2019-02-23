package io.webfolder.sdl4j;

import static io.webfolder.sdl4j.SDL.SDLK_DOWN;
import static io.webfolder.sdl4j.SDL.SDLK_LEFT;
import static io.webfolder.sdl4j.SDL.SDLK_RIGHT;
import static io.webfolder.sdl4j.SDL.SDLK_UP;
import static io.webfolder.sdl4j.SDL.SDL_CreateRenderer;
import static io.webfolder.sdl4j.SDL.SDL_CreateWindow;
import static io.webfolder.sdl4j.SDL.SDL_Delay;
import static io.webfolder.sdl4j.SDL.SDL_GetError;
import static io.webfolder.sdl4j.SDL.SDL_INIT_EVERYTHING;
import static io.webfolder.sdl4j.SDL.SDL_Init;
import static io.webfolder.sdl4j.SDL.SDL_KEYDOWN;
import static io.webfolder.sdl4j.SDL.SDL_PollEvent;
import static io.webfolder.sdl4j.SDL.SDL_QUIT;
import static io.webfolder.sdl4j.SDL.SDL_RenderClear;
import static io.webfolder.sdl4j.SDL.SDL_RenderFillRect;
import static io.webfolder.sdl4j.SDL.SDL_RenderPresent;
import static io.webfolder.sdl4j.SDL.SDL_RenderSetLogicalSize;
import static io.webfolder.sdl4j.SDL.SDL_SetRenderDrawColor;

/**
 * @see http://headerphile.com/sdl2/sdl2-part-3-drawing-rectangles
 */
public class Game {

    private int posX = 100;
    private int posY = 200;
    private int sizeX = 300;
    private int sizeY = 400;

    SDL_Window window;
    SDL_Renderer renderer;

    SDL_Rect playerPos = new SDL_Rect();

    public boolean InitEverything() {
        if (!InitSDL())
            return false;

        if (!CreateWindow())
            return false;

        if (!CreateRenderer())
            return false;

        SetupRenderer();

        return true;
    }

    public boolean InitSDL() {
        if (SDL_Init(SDL_INIT_EVERYTHING) == -1) {
            System.err.println(" Failed to initialize SDL : " + SDL_GetError());
            return false;
        }
        return true;
    }

    public boolean CreateWindow() {
        window = SDL_CreateWindow("Server", posX, posY, sizeX, sizeY, 0);
        if (window == null) {
            System.err.println("Failed to create window : " + SDL_GetError());
            return false;
        }

        return true;
    }

    public boolean CreateRenderer() {
        renderer = SDL_CreateRenderer(window, -1, 0);

        if (renderer == null) {
            System.err.println("Failed to create renderer : " + SDL_GetError());
            return false;
        }

        return true;
    }

    public void SetupRenderer() {
        // Set size of renderer to the same as window
        SDL_RenderSetLogicalSize(renderer, sizeX, sizeY);

        // Set color of renderer to green
        SDL_SetRenderDrawColor(renderer, 0, 255, 0, 255);
    }

    public void Render() {
        // Clear the window and make it all green
        SDL_RenderClear(renderer);

        // Change color to blue
        SDL_SetRenderDrawColor(renderer, 0, 0, 255, 255);

        // Render our "player"
        SDL_RenderFillRect(renderer, playerPos);

        // Change color to green
        SDL_SetRenderDrawColor(renderer, 0, 255, 0, 255);

        // Render the changes above
        SDL_RenderPresent(renderer);
    }

    public void RunGame() {
        boolean loop = true;

        while (loop) {
            SDL_Event event = null;
            while ((event = SDL_PollEvent()) != null) {
                if (event.type == SDL_QUIT)
                    loop = false;
                else if (event.type == SDL_KEYDOWN) {
                    switch (event.toKeyboardEvent().sym) {
                    case SDLK_RIGHT:
                        ++playerPos.x;
                        break;
                    case SDLK_LEFT:
                        --playerPos.x;
                        break;
                    // Remeber 0,0 in SDL is left-top. So when the user pressus down,
                    // the y need to increase
                    case SDLK_DOWN:
                        ++playerPos.y;
                        break;
                    case SDLK_UP:
                        --playerPos.y;
                        break;
                    default:
                        break;
                    }
                }
            }

            Render();

            // Add a 16msec delay to make our game run at ~60 fps
            SDL_Delay(16);
        }
    }

    public static void main(String args[]) {
        Game game = new Game();
        if (!game.InitEverything()) {
            System.exit(-1);
        }

        // Initlaize our playe
        game.playerPos.x = 20;
        game.playerPos.y = 20;
        game.playerPos.w = 20;
        game.playerPos.h = 20;

        game.RunGame();
    }
}
