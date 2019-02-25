package io.webfolder.sdl4j;

import static java.lang.Integer.MAX_VALUE;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This piece of code was originally from headerphile
 *
 * @see http://headerphile.com/sdl2/sdl2-part-5-collision-detection
 */
public class Game5 {

    private static enum Direction {
        Left, Right
    };

    private static class Enemy {

        private Enemy(SDL_Rect pos_, int speed_, Direction dir_) {
            pos = pos_;
            speed = speed_;
            dir = dir_;
        }

        SDL_Rect pos;
        int speed;
        Direction dir;
    };

    // Window pos
    private int posX = 900;

    private int posY = 300;

    private int sizeX = 300;

    private int sizeY = 400;

    int movementFactor = 15; // Amount of pixels the player move per key press

    int lastEnemyPos = 50;

    SDL_Window window;

    SDL_Renderer renderer;

    SDL_Rect playerPos = new SDL_Rect();

    SDL_Rect topBar = new SDL_Rect();

    SDL_Rect bottomBar = new SDL_Rect();

    private List<Enemy> enemies = new ArrayList<>();

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

        // Set color of renderer to red
        SDL_SetRenderDrawColor(renderer, 255, 0, 0, 255);
    }

    public void Render() {
        // Clear the window and make it all red
        SDL_RenderClear(renderer);

        // Change color to black!
        SDL_SetRenderDrawColor(renderer, 0, 0, 0, 255);

        // Render top and bottom bar
        SDL_RenderFillRect(renderer, bottomBar);
        SDL_RenderFillRect(renderer, topBar);

        // Change color to blue!
        SDL_SetRenderDrawColor(renderer, 0, 0, 255, 255);

        // Render our "player"
        SDL_RenderFillRect(renderer, playerPos);

        // Change color to red!
        SDL_SetRenderDrawColor(renderer, 255, 0, 0, 255);

        for (Enemy p : enemies)
            SDL_RenderFillRect(renderer, p.pos);

        // Change color to green!
        SDL_SetRenderDrawColor(renderer, 255, 255, 255, 255);

        // Render the changes above
        SDL_RenderPresent(renderer);
    }

    public void RunGame() {
        boolean loop = true;

        while (loop) {
            SDL_Event event;
            while ((event = SDL_PollEvent()) != null) {
                if (event.type == SDL_QUIT)
                    loop = false;
                else if (event.type == SDL_KEYDOWN) {
                    switch (event.key.keysym.sym) {
                    case SDLK_RIGHT:
                        playerPos.x += movementFactor;
                        break;
                    case SDLK_LEFT:
                        playerPos.x -= movementFactor;
                        break;
                    // Remeber 0,0 in SDL is left-top. So when the user pressus down,
                    // the y need to increase
                    case SDLK_DOWN:
                        playerPos.y += movementFactor;
                        break;
                    case SDLK_UP:
                        playerPos.y -= movementFactor;
                        break;
                    default:
                        break;
                    }
                }
            }

            MoveEnemies();

            // Check collisions against enemies
            if (CheckEnemyCollisions())
                ResetPlayerPos();

            // Check collusion against bottom bar
            // Since top bar covers the entire width, we only need to check the y value
            // topBar.y refers to top of top bar ( top of the screen )
            // Since 0,0 is the top-right we need to add topBar.h to find the bottom of
            // topBar
            if (playerPos.y < (topBar.y + topBar.h))
                ResetPlayerPos();

            Render();

            // Add a 16msec delay to make our game run at ~60 fps
            SDL_Delay(16);
        }
    }

    public void AddEnemy() {
        Random random = new Random();
        if ((random.nextInt(MAX_VALUE) % 2) == 0) {
            enemies.add(new Enemy(new SDL_Rect((int) random.nextInt(MAX_VALUE) % 300, lastEnemyPos, 20, 20), 1, Direction.Right));
        } else {
            enemies.add(new Enemy(new SDL_Rect((int) random.nextInt(MAX_VALUE) % 300 % 300, lastEnemyPos, 20, 20), 1, Direction.Left));
        }
        lastEnemyPos += 25;
    }

    public void MoveEnemies() {
        for (Enemy p : enemies) {
            if (p.dir == Direction.Right) {
                p.pos.x += p.speed;

                if (p.pos.x >= sizeX)
                    p.pos.x = 0;
            } else {
                p.pos.x -= p.speed;

                if ((p.pos.x + p.pos.w) <= 0)
                    p.pos.x = sizeX - p.pos.w;
            }
        }
    }

    public void ResetPlayerPos() {
        // sizeX / 2 = middle pixel of the screen
        // playerPos.w / 2 = middle of the player
        // So setting player x pos to [middle of screen] - [middle of player] means it
        // will be centerd in the screen.
        playerPos.x = (sizeX / 2) - (playerPos.w / 2);
        playerPos.y = sizeY - bottomBar.h;
    }

    public boolean CheckCollision(SDL_Rect rect1, SDL_Rect rect2) {
        // Find edges of rect1
        int left1 = rect1.x;
        int right1 = rect1.x + rect1.w;
        int top1 = rect1.y;
        int bottom1 = rect1.y + rect1.h;

        // Find edges of rect2
        int left2 = rect2.x;
        int right2 = rect2.x + rect2.w;
        int top2 = rect2.y;
        int bottom2 = rect2.y + rect2.h;

        // Check edges
        if (left1 > right2)// Left 1 is right of right 2
            return false; // No collision

        if (right1 < left2) // Right 1 is left of left 2
            return false; // No collision

        if (top1 > bottom2) // Top 1 is below bottom 2
            return false; // No collision

        if (bottom1 < top2) // Bottom 1 is above top 2
            return false; // No collision

        return true;
    }

    public boolean CheckEnemyCollisions() {
        for (Enemy p : enemies) {
            if (CheckCollision(p.pos, playerPos))
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Game5 game = new Game5();

        if (!game.InitEverything())
            System.exit(-1);

        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        game.AddEnemy();
        
        // Init top and bottom bar
        game.topBar.x = 0;
        game.topBar.y = 0;
        game.topBar.w = game.sizeX;
        game.topBar.h = 20;

        game.bottomBar.x = 0;
        game.bottomBar.y = game.sizeY - 20;
        game.bottomBar.w = game.sizeX;
        game.bottomBar.h = 20;

        // Initlaize our player
        game.playerPos.w = 20;
        game.playerPos.h = 20;
        game.ResetPlayerPos();

        game.RunGame();
    }
}
