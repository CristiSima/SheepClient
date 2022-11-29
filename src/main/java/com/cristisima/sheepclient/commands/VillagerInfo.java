package com.cristisima.sheepclient.commands;

import com.cristisima.sheepclient.SheepClient;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;


import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class VillagerInfo {

    public static void onInitializeClient() {
        SheepClient.LOGGER.info("And here we sneakily insert commands");
        ClientCommandRegistrationCallback.EVENT.register(VillagerInfo::registerCommands);
    }
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        dispatcher.register(literal("villagerInfo")
                .executes(ctx -> showVillagerInfo(ctx.getSource())));
    }
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess)
    {
        register(dispatcher);
    }

    public static int showVillagerInfo(FabricClientCommandSource source)
    {
        source.sendFeedback(Text.literal("YEA BOY"));
//        source.getWorld().getClosestEntity();
        return 1;
    }
}
