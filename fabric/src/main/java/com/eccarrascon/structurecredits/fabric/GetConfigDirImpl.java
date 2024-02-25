package com.eccarrascon.structurecredits.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class GetConfigDirImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
