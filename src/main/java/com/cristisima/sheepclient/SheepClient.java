package com.cristisima.sheepclient;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cristisima.sheepclient.commands.VillagerInfo;

public class SheepClient implements ModInitializer {

    public  static String MOD_ID ="sheep_client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize()
    {
        LOGGER.info("The best hacker in action");
        VillagerInfo.onInitializeClient();
    }
}
