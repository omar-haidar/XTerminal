package dev.omar.xterminal.utils;

import android.os.Build;

import androidx.annotation.Nullable;

import dev.omar.xterminal.models.AbiUrl;

public enum AbiUrls {
    X86_64(new AbiUrl(
            "x86_64",
            "https://raw.githubusercontent.com/Xed-Editor/Karbon-PackagesX/main/x86_64/libtalloc.so.2",
            "https://raw.githubusercontent.com/Xed-Editor/Karbon-PackagesX/main/x86_64/proot",
            "https://dl-cdn.alpinelinux.org/alpine/v3.21/releases/x86_64/alpine-minirootfs-3.21.0-x86_64.tar.gz")
    ),
    ARMEABI_V7A((new AbiUrl(
            "armeabi-v7a",
            "https://raw.githubusercontent.com/Xed-Editor/Karbon-PackagesX/main/arm/libtalloc.so.2",
            "https://raw.githubusercontent.com/Xed-Editor/Karbon-PackagesX/main/arm/proot",
            "https://dl-cdn.alpinelinux.org/alpine/v3.21/releases/armhf/alpine-minirootfs-3.21.0-armhf.tar.gz")
    )),
    ARM64_V8A((new AbiUrl(
            "arm64-v8a",
            "https://raw.githubusercontent.com/Xed-Editor/Karbon-PackagesX/main/aarch64/libtalloc.so.2",
            "https://raw.githubusercontent.com/Xed-Editor/Karbon-PackagesX/main/aarch64/proot",
            "https://dl-cdn.alpinelinux.org/alpine/v3.21/releases/aarch64/alpine-minirootfs-3.21.0-aarch64.tar.gz")
    ));

    private AbiUrl abiUrl;

    AbiUrls(AbiUrl abiUrl) {
        this.abiUrl = abiUrl;
    }

    public AbiUrl getAbiUrl() {
        return abiUrl;
    }

    @Nullable
    public static AbiUrl getSupportedAbi(){
        String[] abis = Build.SUPPORTED_ABIS;
        for (String abi : abis) {
            for (AbiUrls supportedAbi : AbiUrls.values()) {
                if (supportedAbi.getAbiUrl().getAbi().equals(abi)) {
                    return supportedAbi.getAbiUrl();
                }
            }
        }
        return null;
    }

}
