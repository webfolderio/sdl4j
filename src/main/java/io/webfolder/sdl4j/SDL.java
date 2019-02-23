package io.webfolder.sdl4j;

public class SDL {

    static {
        System.load("C:\\eclipse-4.10\\workspace\\sdl4j\\src\\main\\resources\\META-INF\\sdl4j.dll");
    }

    public static final long SDL_INIT_TIMER          = 0x00000001;
    public static final long SDL_INIT_AUDIO          = 0x00000010;
    public static final long SDL_INIT_VIDEO          = 0x00000020; /* SDL_INIT_VIDEO implies SDL_INIT_EVENTS */
    public static final long SDL_INIT_JOYSTICK       = 0x00000200; /* SDL_INIT_JOYSTICK implies SDL_INIT_EVENTS */
    public static final long SDL_INIT_HAPTIC         = 0x00001000;
    public static final long SDL_INIT_GAMECONTROLLER = 0x00002000; /* SDL_INIT_GAMECONTROLLER implies SDL_INIT_JOYSTICK */
    public static final long SDL_INIT_EVENTS         = 0x00004000;
    public static final long SDL_INIT_SENSOR         = 0x00008000;
    public static final long SDL_INIT_NOPARACHUTE    = 0x00100000; /* compatibility; this flag is ignored. */
    public static final long SDL_INIT_EVERYTHING     = SDL_INIT_TIMER          |
                                                       SDL_INIT_AUDIO          |
                                                       SDL_INIT_VIDEO          |
                                                       SDL_INIT_EVENTS         |
                                                       SDL_INIT_JOYSTICK       |
                                                       SDL_INIT_HAPTIC         |
                                                       SDL_INIT_GAMECONTROLLER |
                                                       SDL_INIT_SENSOR;

    public static final int SDL_WINDOWPOS_CENTERED  = 0x2FFF0000;

    public static final long SDL_WINDOW_FULLSCREEN         = 0x00000001; /* fullscreen window */
    public static final long SDL_WINDOW_OPENGL             = 0x00000002; /* window usable with OpenGL context */
    public static final long SDL_WINDOW_SHOWN              = 0x00000004; /* window is visible */
    public static final long SDL_WINDOW_HIDDEN             = 0x00000008; /* window is not visible */
    public static final long SDL_WINDOW_BORDERLESS         = 0x00000010; /* no window decoration */
    public static final long SDL_WINDOW_RESIZABLE          = 0x00000020; /* window can be resized */
    public static final long SDL_WINDOW_MINIMIZED          = 0x00000040; /* window is minimized */
    public static final long SDL_WINDOW_MAXIMIZED          = 0x00000080; /* window is maximized */
    public static final long SDL_WINDOW_INPUT_GRABBED      = 0x00000100; /* window has grabbed input focus */
    public static final long SDL_WINDOW_INPUT_FOCUS        = 0x00000200; /* window has input focus */
    public static final long SDL_WINDOW_MOUSE_FOCUS        = 0x00000400; /* window has mouse focus */
    public static final long SDL_WINDOW_FULLSCREEN_DESKTOP = SDL_WINDOW_FULLSCREEN | 0x00001000;
    public static final long SDL_WINDOW_FOREIGN            = 0x00000800; /* window not created by SDL */
    public static final long SDL_WINDOW_ALLOW_HIGHDPI      = 0x00002000; /* window should be created in high-DPI mode if supported.
                                                                            On macOS NSHighResolutionCapable must be set true in the
                                                                            application's Info.plist for this to have any effect. */

    /* Application events */
    public static final int SDL_QUIT          = 0x100; /* User-requested quit */

    /* Keyboard events */
    public static final int SDL_KEYDOWN       = 0x300;               /* Key pressed */
    public static final int SDL_KEYUP         = SDL_KEYDOWN + 1;     /* Key released */
    public static final int SDL_TEXTEDITING   = SDL_KEYUP + 1;       /* Keyboard text editing (composition) */
    public static final int SDL_TEXTINPUT     = SDL_TEXTEDITING + 1; /* Keyboard text input */
    public static final int SDL_KEYMAPCHANGED = SDL_TEXTINPUT + 1;   /* Keymap changed due to a system event such as an
                                                                        input language or keyboard layout change. */

    public static final int SDLK_RIGHT = 79 | 1 << 30;
    public static final int SDLK_LEFT  = 80 | 1 << 30;
    public static final int SDLK_DOWN  = 81 | 1 << 30;
    public static final int SDLK_UP    = 82 | 1 << 30;

    public static native int SDL_Init(long flags);

    public static native int SDL_InitSubSystem(long flags);

    public static native void SDL_Quit();

    public static native void SDL_QuitSubSystem(long flags);

    public static native void SDL_SetMainReady();

    public static native SDL_Window SDL_CreateWindow(String title, int x, int y, int w, int h, long flags);

    public static native void SDL_DestroyWindow(SDL_Window window);

    public static native void SDL_DisableScreenSaver();

    public static native void SDL_EnableScreenSaver();

    public static native String SDL_GetError();

    public static native SDL_Renderer SDL_CreateRenderer(SDL_Window window, int index, long flags);

    public static native int SDL_RenderSetLogicalSize(SDL_Renderer renderer, int w, int h);

    public static native void SDL_DestroyRenderer(SDL_Renderer renderer);

    public static native int SDL_SetRenderDrawColor(SDL_Renderer renderer, int r, int g, int b, int a);

    public static native int SDL_RenderClear(SDL_Renderer renderer);

    public static native int SDL_RenderFillRect(SDL_Renderer renderer, SDL_Rect rect);

    public static native void SDL_Delay(long ms);

    public static native void SDL_RenderPresent(SDL_Renderer renderer);

    public static native SDL_Event SDL_PollEvent();
}
