package com.swill.ultimate;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwillUltimateMod implements ModInitializer {
    public static final String MOD_ID = "swill_ultimate";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("[SWILL] Ultimate Client Mod loaded for Fabric 1.21.10");
    }
}