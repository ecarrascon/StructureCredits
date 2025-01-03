package com.eccarrascon.structurecredits.neoforge;



import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class GetConfigDirImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
