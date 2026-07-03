package dev.omar.xterminal.utils;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.io.File;

import dev.omar.xterminal.App;

public final class Environment {

    private Environment(){

    }
    public static void init(App app){

    }


    @NonNull
    @Contract("_ -> param1")
    public static File makeDirIfNotExists(@NonNull File f) {
        if (!f.exists()) {
            //noinspection ResultOfMethodCallIgnored
            f.mkdirs();
        }
        return f;
    }
}
