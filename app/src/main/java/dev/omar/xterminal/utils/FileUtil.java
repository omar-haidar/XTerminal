package dev.omar.xterminal.utils;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dev.omar.xterminal.App;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileUtil {

    public static void downloadFile(String url,File out) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Failrd to download file : " + response.code());
        }
        InputStream inputStream = response.body().byteStream();
        FileOutputStream outputStream = new FileOutputStream(out);
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    @NonNull
    @Contract(" -> new")
    public static File getHomeDir(){
        return makeDirIfNotExists(new File(getFilesDir(),"home"));
    }
    @NonNull
    @Contract(" -> new")
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

    @NonNull
    @Contract("_ -> new")
    public static File getFile(String name) {
        return new File(getFilesDir(), name);
    }

    @NonNull
    @Contract(" -> new")
    public static File localDir() {
        return makeDirIfNotExists(new File(getFilesDir(), "local"));
    }

    @NonNull
    @Contract("_ -> new")
    public static File localFile(String name) {
        return new File(localDir(), name);
    }

    @NonNull
    @Contract(" -> new")
    public static File alpineDir() {
        return makeDirIfNotExists(new File(getFilesDir(), "alpine"));
    }

    @NonNull
    @Contract(" -> new")
    public static File alpineHomeDir() {
        return makeDirIfNotExists(new File(alpineDir(), "root"));
    }

    @NonNull
    @Contract(" -> new")
    public static File localBinDir() {
        return makeDirIfNotExists(new File(localDir(), "bin"));
    }

    @NonNull
    @Contract(" -> new")
    public static File localLibDir() {
        return makeDirIfNotExists(new File(localDir(), "lib"));
    }

    @NonNull
    @Contract("_ -> param1")
    public static File makeDirIfNotExists(@NonNull File f) {
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }
}
