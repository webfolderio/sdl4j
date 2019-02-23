#include <jni.h>
#include <jni.h>
#include <SDL.h>

enum ReflectionCacheType {
    global,
    local
};

class String {
    JNIEnv * env_;

    jstring java_str_;

    const char * str_;

public:

    String(const String &) = delete;

    String(String &&) = delete;

    String(JNIEnv * env, jstring from)
        : env_{ env }, java_str_(from), str_{ env->GetStringUTFChars(from, JNI_FALSE) }
    {}

    ~String() {
        env_->ReleaseStringUTFChars(java_str_, str_);
    }

    const char * c_str() const noexcept { return str_; }
};

class JavaClass {

    JNIEnv *env_;

    ReflectionCacheType type_;

    jclass klass_;

public:

    JavaClass(const JavaClass &) = delete;

    JavaClass(JavaClass &&) = delete;

    ~JavaClass() {
        switch (type_) {
        case global: env_->DeleteGlobalRef(klass_); break;
        default: env_->DeleteLocalRef(klass_);
        }
    }

    JavaClass(JNIEnv *env, const char *className) : JavaClass(env, local, className) { }

    JavaClass(JNIEnv *env, ReflectionCacheType type, const char *className) :
        env_(env), type_(type) {
        jclass klass = env_->FindClass(className);
        if (type_ == global) {
            klass_ = (jclass)env_->NewGlobalRef(klass);
            env->DeleteLocalRef(klass);
        }
        else {
            klass_ = klass;
        }
    }

    jclass get() const noexcept {
        return klass_;
    }
};

class JavaMethod {

    JNIEnv *env_;

    ReflectionCacheType type_;

    jmethodID method_;

public:

    JavaMethod(const JavaMethod &) = delete;

    JavaMethod(JavaMethod &&) = delete;

    ~JavaMethod() {
        switch (type_) {
        case global: env_->DeleteGlobalRef((jobject)method_); break;
        default: env_->DeleteLocalRef((jobject)method_);
        }
    }

    JavaMethod(JNIEnv *env, const char *klass,
        const char *name, const char *signature)
        : JavaMethod(env, local, klass, name, signature) { }

    JavaMethod(JNIEnv *env, ReflectionCacheType type,
        const char *klass, const char *name,
        const char *signature) :
        env_(env), type_(type) {
        jclass klass_ = env_->FindClass(klass);
        if (klass_) {
            jmethodID method = env->GetMethodID(klass_, name, signature);
            if (type_ == global) {
                method_ = (jmethodID)env_->NewGlobalRef((jobject)method);
            }
            else {
                method_ = (jmethodID)env_->NewLocalRef((jobject)method);
            }
            env->DeleteLocalRef(klass_);
        }
    }

    jmethodID get() const noexcept {
        return method_;
    }
};

jobject sdl4j_SDL_GetClosestDisplayMode(JNIEnv *env, jclass klass, jint displayIndex, jobject mode, jobject closest) {
    SDL_DisplayMode target_, closest_;
    if (SDL_GetClosestDisplayMode(displayIndex, &target_, &closest_) == nullptr) {
        return nullptr;
    }
}

jint sdl4j_SDL_Init(JNIEnv *env, jclass klass, jlong flags) {
    return SDL_Init(flags);
}

void sdl4j_SDL_Quit(JNIEnv *env, jclass klass) {
    SDL_Quit();
}

void sdl4j_SDL_SetMainReady(JNIEnv *env, jclass klass) {
    SDL_SetMainReady();
}

jobject sdl4j_SDL_CreateWindow(JNIEnv *env, jclass klass, jstring title, jint x, jint y, jint w, jint h, jlong flags) {
    JavaClass retKlass{ env, "io/webfolder/sdl4j/SDL_Window" };
    JavaMethod retConstructor{ env, "io/webfolder/sdl4j/SDL_Window", "<init>", "(J)V" };
    String title_{ env, title };
    SDL_Window* ret_ = SDL_CreateWindow(title_.c_str(), x, y, w, h, flags);
    jobject retObject = env->NewObject(retKlass.get(), retConstructor.get(), (jlong)ret_);
    return retObject;
}

void sdl4j_SDL_QuitSubSystem(JNIEnv *env, jclass klass, jlong flags) {
    SDL_QuitSubSystem(flags);
}

void sdl4j_SDL_EnableScreenSaver(JNIEnv *env, jclass klass) {
    SDL_EnableScreenSaver();
}

void sdl4j_SDL_DisableScreenSaver(JNIEnv *env, jclass klass) {
    SDL_DisableScreenSaver();
}

void sdl4j_SDL_DestroyWindow(JNIEnv *env, jclass klass, jobject window) {
    JavaMethod methodWindow{ env, "io/webfolder/sdl4j/SDL_Window", "getPeer", "()J" };
    jlong peerWindow = (jlong)env->CallObjectMethod(window, methodWindow.get());
    SDL_Window *window_ = (SDL_Window*)peerWindow;
    SDL_DestroyWindow(window_);
}

jint sdl4j_SDL_InitSubSystem(JNIEnv *env, jclass klass, jlong flags) {
    return SDL_InitSubSystem(flags);
}

jstring sdl4j_SDL_GetError(JNIEnv *env, jclass klass) {
    const char *msg = SDL_GetError();
    return env->NewStringUTF(msg);
}

jobject sdl4j_SDL_CreateRenderer(JNIEnv *env, jclass klass, jobject window, jint index, jlong flags) {
    JavaClass retKlass{ env, "io/webfolder/sdl4j/SDL_Renderer" };
    JavaMethod retConstructor{ env, "io/webfolder/sdl4j/SDL_Renderer", "<init>", "(J)V" };
    JavaMethod methodWindow{ env, "io/webfolder/sdl4j/SDL_Window", "getPeer", "()J" };
    jlong peerWindow = (jlong)env->CallObjectMethod(window, methodWindow.get());
    SDL_Window *window_ = (SDL_Window*)peerWindow;
    SDL_Renderer *ret_ = SDL_CreateRenderer(window_, index, flags);
    jobject retObject = env->NewObject(retKlass.get(), retConstructor.get(), (jlong)ret_);
    return retObject;
}

jint sdl4j_SDL_RenderSetLogicalSize(JNIEnv *env, jclass klass, jobject renderer, jint w, jint h) {
    JavaMethod methodRenderer{ env, "io/webfolder/sdl4j/SDL_Renderer", "getPeer", "()J" };
    jlong peerRenderer = (jlong)env->CallObjectMethod(renderer, methodRenderer.get());
    SDL_Renderer *renderer_ = (SDL_Renderer*)peerRenderer;
    return SDL_RenderSetLogicalSize(renderer_, w, h);
}

void sdl4j_SDL_DestroyRenderer(JNIEnv *env, jclass klass, jobject renderer) {
    JavaMethod methodRenderer{ env, "io/webfolder/sdl4j/SDL_Renderer", "getPeer", "()J" };
    jlong peerRenderer = (jlong)env->CallObjectMethod(renderer, methodRenderer.get());
    SDL_Renderer *renderer_ = (SDL_Renderer*)peerRenderer;
    SDL_DestroyRenderer(renderer_);
}

jint sdl4j_SDL_SetRenderDrawColor(JNIEnv *env, jclass klass, jobject renderer, jint r, jint g, jint b, jint a) {
    JavaMethod methodRenderer{ env, "io/webfolder/sdl4j/SDL_Renderer", "getPeer", "()J" };
    jlong peerRenderer = (jlong)env->CallObjectMethod(renderer, methodRenderer.get());
    SDL_Renderer *renderer_ = (SDL_Renderer*)peerRenderer;
    return SDL_SetRenderDrawColor(renderer_, r, g, b, a);
}

jint sdl4j_SDL_RenderClear(JNIEnv *env, jclass klass, jobject renderer) {
    JavaMethod methodRenderer{ env, "io/webfolder/sdl4j/SDL_Renderer", "getPeer", "()J" };
    jlong peerRenderer = (jlong)env->CallObjectMethod(renderer, methodRenderer.get());
    SDL_Renderer *renderer_ = (SDL_Renderer*)peerRenderer;
    return SDL_RenderClear(renderer_);
}

void sdl4j_SDL_Delay(JNIEnv *env, jclass klass, jlong ms) {
    SDL_Delay(ms);
}

jint sdl4j_SDL_RenderFillRect(JNIEnv *env, jclass klass, jobject renderer, jobject rect) {
    JavaMethod methodRenderer{ env, "io/webfolder/sdl4j/SDL_Renderer", "getPeer", "()J" };
    jlong peerRenderer = (jlong)env->CallObjectMethod(renderer, methodRenderer.get());
    SDL_Renderer *renderer_ = (SDL_Renderer*)peerRenderer;
    SDL_Rect rect_;
    JavaMethod methodX{ env, "io/webfolder/sdl4j/SDL_Rect", "getX", "()I" };
    rect_.x = (jint)env->CallObjectMethod(rect, methodX.get());
    JavaMethod methodY{ env, "io/webfolder/sdl4j/SDL_Rect", "getY", "()I" };
    rect_.y = (jint)env->CallObjectMethod(rect, methodY.get());
    JavaMethod methodW{ env, "io/webfolder/sdl4j/SDL_Rect", "getW", "()I" };
    rect_.w = (jint)env->CallObjectMethod(rect, methodW.get());
    JavaMethod methodH{ env, "io/webfolder/sdl4j/SDL_Rect", "getH", "()I" };
    rect_.h = (jint)env->CallObjectMethod(rect, methodH.get());
    return SDL_RenderFillRect(renderer_, &rect_);
}

void sdl4j_SDL_RenderPresent(JNIEnv *env, jclass klass, jobject renderer) {
    JavaMethod methodRenderer{ env, "io/webfolder/sdl4j/SDL_Renderer", "getPeer", "()J" };
    jlong peerRenderer = (jlong)env->CallObjectMethod(renderer, methodRenderer.get());
    SDL_Renderer *renderer_ = (SDL_Renderer*)peerRenderer;
    SDL_RenderPresent(renderer_);
}

jobject sdl4j_SDL_PollEvent(JNIEnv *env, jclass klass) {
    SDL_Event event_;
    int ret = SDL_PollEvent(&event_);
    JavaClass *eventKlass = nullptr;
    JavaMethod *eventConstructor = nullptr;
    jobject event = nullptr;
    if (ret == 1) {
        switch (event_.type) {
            case SDL_KEYUP:
            case SDL_KEYDOWN:
                eventKlass = new JavaClass{ env, "io/webfolder/sdl4j/SDL_KeyboardEvent" };
                eventConstructor = new JavaMethod{ env, "io/webfolder/sdl4j/SDL_KeyboardEvent", "<init>", "(II)V" };
                event = env->NewObject(eventKlass->get(), eventConstructor->get(), event_.type, event_.key.keysym.sym);
            break;
            default:
                eventKlass = new JavaClass{ env, "io/webfolder/sdl4j/SDL_Event" };
                eventConstructor = new JavaMethod{ env, "io/webfolder/sdl4j/SDL_Event", "<init>", "(I)V" };
                event = env->NewObject(eventKlass->get(), eventConstructor->get(), event_.type);
        }
    }
    if (eventKlass) {
        delete eventKlass;
    }
    if (eventConstructor) {
        delete eventConstructor;
    }
    return event;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env;
    jclass klass;
    JNINativeMethod methods[] = {
      { "SDL_Init", "(J)I", (void*)sdl4j_SDL_Init },
      { "SDL_Quit", "()V", (void*)sdl4j_SDL_Quit },
      { "SDL_SetMainReady", "()V", (void*)sdl4j_SDL_SetMainReady },
      { "SDL_CreateWindow", "(Ljava/lang/String;IIIIJ)Lio/webfolder/sdl4j/SDL_Window;", (void*)sdl4j_SDL_CreateWindow },
      { "SDL_QuitSubSystem", "(J)V", (void*)sdl4j_SDL_QuitSubSystem },
      { "SDL_EnableScreenSaver", "()V", (void*)sdl4j_SDL_EnableScreenSaver },
      { "SDL_DisableScreenSaver", "()V", (void*)sdl4j_SDL_DisableScreenSaver },
      { "SDL_DestroyWindow", "(Lio/webfolder/sdl4j/SDL_Window;)V", (void*)sdl4j_SDL_DestroyWindow },
      { "SDL_InitSubSystem", "(J)I", (void*)sdl4j_SDL_InitSubSystem },
      { "SDL_GetError", "()Ljava/lang/String;", (void*)sdl4j_SDL_GetError },
      { "SDL_CreateRenderer", "(Lio/webfolder/sdl4j/SDL_Window;IJ)Lio/webfolder/sdl4j/SDL_Renderer;", (void*) sdl4j_SDL_CreateRenderer },
      { "SDL_RenderSetLogicalSize", "(Lio/webfolder/sdl4j/SDL_Renderer;II)I", (void*) sdl4j_SDL_RenderSetLogicalSize },
      { "SDL_DestroyRenderer", "(Lio/webfolder/sdl4j/SDL_Renderer;)V", (void*) sdl4j_SDL_DestroyRenderer },
      { "SDL_SetRenderDrawColor", "(Lio/webfolder/sdl4j/SDL_Renderer;IIII)I", (void*) sdl4j_SDL_SetRenderDrawColor },
      { "SDL_RenderClear", "(Lio/webfolder/sdl4j/SDL_Renderer;)I", (void*) sdl4j_SDL_RenderClear },
      { "SDL_RenderFillRect", "(Lio/webfolder/sdl4j/SDL_Renderer;Lio/webfolder/sdl4j/SDL_Rect;)I", (void*)sdl4j_SDL_RenderFillRect },
      { "SDL_Delay", "(J)V", (void*)sdl4j_SDL_Delay },
      { "SDL_RenderPresent", "(Lio/webfolder/sdl4j/SDL_Renderer;)V", (void*)sdl4j_SDL_RenderPresent },
      { "SDL_PollEvent", "()Lio/webfolder/sdl4j/SDL_Event;", (void*)sdl4j_SDL_PollEvent }
    };
    if (vm->GetEnv((void **)&env, JNI_VERSION_1_8) != JNI_OK) {
        return -1;
    }
    klass = env->FindClass("io/webfolder/sdl4j/SDL");
    env->RegisterNatives(klass, methods, sizeof(methods) / sizeof(methods[0]));
    return JNI_VERSION_1_8;
}
