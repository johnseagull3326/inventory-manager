package net.johnseagull.inventoryManager.client;

import net.johnseagull.inventoryManager.Figs;
import net.fabricmc.api.ClientModInitializer;
import net.johnseagull.figManagerClient.FigManagerClient;

public class InventoryManagerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FigManagerClient g = new FigManagerClient();
        g.init(Figs.instance,0.7f);
    }
}
