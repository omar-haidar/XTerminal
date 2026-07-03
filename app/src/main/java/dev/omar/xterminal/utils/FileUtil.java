package dev.omar.xterminal.utils;

import java.io.File;

import dev.omar.xterminal.App;

public class FileUtil {

    public static File getHomeDir(){
        return makeDirIfNotExists(new File(getFilesDir(),"home"));
    }
    public static File getPrefixDir(){
        return makeDirIfNotExists(new File(getFilesDir(),"usr"));
    }
    public static boolean isFilesDownloaded() {
        return FileUtil.getFile("proot").exists() &&
                FileUtil.getFile("libtalloc.so.2").exists() &&
                FileUtil.getFile("alpine.tar.gz").exists();
    }

    public static File getFilesDir() {
        if (App.getApp() == null) {
            return makeDirIfNotExists(new File("/data/data/dev.omar.xterminal/files"));
        } else {
            return App.getApp().getFilesDir();
        }
    }

    public static File getFile(String name) {
        return new File(getFilesDir(), name);
    }

    public static File localDir() {
        return makeDirIfNotExists(new File(getFilesDir(), "local"));
    }

    public static File localFile(String name) {
        return new File(localDir(), name);
    }

    public static File alpineDir() {
        return makeDirIfNotExists(new File(getFilesDir(), "alpine"));
    }

    public static File alpineHomeDir() {
        return makeDirIfNotExists(new File(alpineDir(), "root"));
    }

    public static File localBinDir() {
        return makeDirIfNotExists(new File(localDir(), "bin"));
    }

    public static File localLibDir() {
        return makeDirIfNotExists(new File(localDir(), "lib"));
    }

    public static File makeDirIfNotExists(File f) {
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }
}
