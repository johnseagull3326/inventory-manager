package johnseagull.inventoryManager;

import johnseagull.inventoryManager.Figs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import johnseagull.figManagerMC.FigManagerMC;
import johnseagull.inventoryManager.accessor.LivingEntityAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

public class InventoryManager implements ModInitializer {
    //the figs...
    public static String figManagerName = "inventory_manager";
    public static String projectVersion = "1.2";

    @Override
    public void onInitialize() {
        //the figs are inevitable
        FigManagerMC e = new FigManagerMC();
        e.init(figManagerName, projectVersion, Figs.instance);


        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            //nothing can stop the figs
            Figs f = (Figs) FigManagerMC.FIGS;

            if (f.enable.value) {
                for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                    if (player.isCreative()) {
                        //item dupes when in creative sooo....
                        continue;
                    }
                    Inventory inv = player.getInventory();
                    for (Map.Entry<String, String> s : f.limitedItems.value.entrySet()) {
                        String itemIdStr = s.getKey();
                        int max = 0;
                        try {
                            max = Integer.parseInt(s.getValue());
                        } catch (NumberFormatException ex) {
                            continue;
                        }

                        Item item = BuiltInRegistries.ITEM.getValue(Identifier.tryParse(itemIdStr));
                        if (item == Items.AIR) continue;

                        int tc = 0;
                        for (int i = 0; i < inv.getContainerSize(); i++) {
                            ItemStack stack = inv.getItem(i);
                            if (stack.is(item)) {
                                tc += stack.getCount();
                            }
                        }

                        if (tc > max) {
                            int r = tc - max;

                            for (int i = inv.getContainerSize() - 1; i >= 0; i--) {
                                if (r <= 0) break;

                                ItemStack stack = inv.getItem(i);
                                if (stack.is(item)) {
                                    int count = stack.getCount();
                                    if (count <= r) {
                                        r -= count;
                                        inv.setItem(i, ItemStack.EMPTY);
                                    } else {
                                        stack.setCount(count - r);
                                        r = 0;
                                    }
                                }
                            }

                            ItemStack dropStack = new ItemStack(item, tc - max);
                            player.drop(dropStack, false);

                            if (f.doMessage.value) {
                                player.sendSystemMessage(Component.literal("You can only have " + max + " of this item!"), true);
                            }
                        }
                    }

                    for (int i = 0; i < inv.getContainerSize(); i++) {
                        ItemStack stack = inv.getItem(i);
                        String itemKey = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
                        try {
                            if (hasItemInside(stack, Items.AIR, inv) == 2) {
                                if (inv.getItem(0) != stack) {
                                    if (f.doMessage.value) {
                                        player.sendSystemMessage(Component.literal("Could not verify deep nested item - returning...").withStyle(ChatFormatting.RED), true);
                                    }
                                    inv.setItem(0, stack.copy());
                                }
                            }
                        } catch (NullPointerException ignore) {

                        }
                        for (String s : f.blockedItems.value) {
                            ItemStack newstack = inv.getItem(i);
                            if (newstack.isEmpty()) continue;
                            if (s.contains("[")) {
                                String[] parts = s.split("\\[", 2);
                                String baseConfigId = parts[0];
                                String componentData = parts[1].replace("[", "").replace("]", "");

                                if (itemKey.equals(baseConfigId)) {
                                    String stackComponentString = stack.getComponents().toString().toLowerCase();
                                    if (stackComponentString.contains(componentData.toLowerCase())) {
                                        //*yoink*
                                        ItemStack dropStack = stack.copy();
                                        inv.setItem(i, ItemStack.EMPTY);
                                        player.drop(dropStack, false);
                                        if(f.doMessage.value) {
                                            player.sendSystemMessage(Component.literal(f.blockedItemMessage.value), true);
                                        }
                                    }
                                }
                            } else {
                                if (itemKey.equals(s)) {
                                    //*yoink*
                                    ItemStack dropStack = stack.copy();
                                    inv.setItem(i, ItemStack.EMPTY);
                                    player.drop(dropStack, false);
                                    if(f.doMessage.value) {
                                        player.sendSystemMessage(Component.literal(f.blockedItemMessage.value), true);
                                    }
                                }
                            }
                        }
                    }

                    for (int i = 0; i < 9; i++) {
                        moveItem(inv, i, player, 0,"general/hotbar",f.hotbarItems.value);
                    }
                    moveItem(inv, 40, player, 0, "general/hotbar",f.hotbarItems.value);

                    moveItem(inv, 36, player, 0, "general/armor",f.armorItems.value);
                    moveItem(inv, 37, player, 0, "general/armor",f.armorItems.value);
                    moveItem(inv, 38, player, 0, "general/armor",f.armorItems.value);
                    moveItem(inv, 39, player, 0, "general/armor",f.armorItems.value);
                    
                    //This is the TSA (type safety administration): what in the world is this and why does it work?
                    int time = ((LivingEntityAccessor) player).getCombatTime();
                    ((LivingEntityAccessor) player).setCombatTime(time + 1);
                    if (time <= f.cooldown.value) {
                        // *deja vu*
                        for (int i = 0; i < 9; i++) {
                            moveItem(inv, i, player, time,"combat/hotbar",f.combatHotbarItems.value);
                        }
                        moveItem(inv, 40, player, time, "combat/hotbar",f.combatHotbarItems.value);

                        moveItem(inv, 36, player, time, "combat/armor",f.combatArmorItems.value);
                        moveItem(inv, 37, player, time, "combat/armor",f.combatArmorItems.value);
                        moveItem(inv, 38, player, time, "combat/armor",f.combatArmorItems.value);
                        moveItem(inv, 39, player, time, "combat/armor",f.combatArmorItems.value);

                        if (f.cooldown.value - time == 0 && f.doMessage.value) {
                            //cooldown over
                            player.sendSystemMessage(Component.literal(f.cooldownOverMessage.value), true);
                        }
                    }

                    PlayerEnderChestContainer enderchest = player.getEnderChestInventory();
                    //some idiot decided to nest 3 for loops, who could it be
                    for (String s : f.enderItems.value) {
                        Item itemToBlock = BuiltInRegistries.ITEM.getValue(Identifier.tryParse(s));
                        for (int i = 0; i < enderchest.getContainerSize(); i++) {
                            ItemStack stack = enderchest.getItem(i);
                            Inventory inventory = player.getInventory();
                            if (stack.is(itemToBlock) || hasItemInside(stack, itemToBlock, inventory) == 1) {
                                enderchest.setItem(i, ItemStack.EMPTY);
                                boolean returned = false;
                                for (int j = 0; j < 36; ++j) {
                                    ItemStack stack1 = inventory.getItem(j);
                                    if (!returned && stack1.is(ItemStack.EMPTY.getItem())) {
                                        inventory.setItem(j, stack.copy());
                                        returned = true;
                                        if (f.doMessage.value) {
                                            //:D
                                            if (player.getPlainTextName().equals("TheCreeper3326")) {
                                                player.sendSystemMessage(Component.literal("Why did you do that, you knew you couldn't! :D"), false);
                                            } else {
                                                player.sendSystemMessage(Component.literal("You cannot have that item in your enderchest!"), true);
                                            }
                                        }
                                    }
                                    if (!returned) {
                                        //mic drop .. i mean item drop
                                        player.drop(stack, false);
                                        if (f.doMessage.value) {
                                            player.sendSystemMessage(Component.literal("You did not have enough empty inventory space, so the item was dropped."), true);
                                        }
                                    }
                                }
                            }
                            //in the very rare case i mess something up
                            if (hasItemInside(stack, Items.AIR, inventory) == 2) {
                                if (enderchest.getItem(0) != stack) {
                                    if (f.doMessage.value) {
                                        player.sendSystemMessage(Component.literal("Could not verify deep nested item - returning...").withStyle(ChatFormatting.RED), true);
                                    }
                                    enderchest.setItem(0, stack.copy());

                                    //why so many closing braces :|
                                }
                            }
                        }
                    }

                }
            }
        });
    }

    //rando helper meths
    //great names i know
    //also shoutout to unused nullables
    private int hasItemInside(ItemStack stack, Item item, Inventory inventory) {

        if (stack.has(DataComponents.CONTAINER)) {
            var shulker = stack.get(DataComponents.CONTAINER);
            if (shulker == null) return 3;
            for (ItemStack shulkerItem : shulker.nonEmptyItems()) {
                if (shulkerItem.is(item) || hasItemInside(shulkerItem, item, inventory) == 1) {
                    return 1;
                }
                if (shulkerItem.has(DataComponents.BUNDLE_CONTENTS)) {
                    for (ItemStack shulkerItem2 : shulkerItem.get(DataComponents.BUNDLE_CONTENTS).items()) {
                        if (shulkerItem2.has(DataComponents.BUNDLE_CONTENTS)) {
                            for (ItemStack shulkerItem3 : shulkerItem2.get(DataComponents.BUNDLE_CONTENTS).items()) {
                                if (shulkerItem3.has(DataComponents.BUNDLE_CONTENTS)) {
                                    inventory.setItem(0, stack);
                                    return 2;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (stack.has(DataComponents.BUNDLE_CONTENTS)) {
            var bundle = stack.get(DataComponents.BUNDLE_CONTENTS);
            if (bundle == null) return 3;
            for (ItemStack bundleItem : bundle.items()) {
                if (bundleItem.getComponents().get(DataComponents.CUSTOM_NAME).getString().contains("minecraft:bundle")) {
                    return 2;
                }
                if (bundleItem.is(item) || hasItemInside(bundleItem, item,inventory) == 1) {
                    return 1;
                }
            }
            if (stack.getComponents().get(DataComponents.CUSTOM_NAME).getString().contains("minecraft:bundle")) {
                return 2;
            }
        }
        return 0;
    }

    private void moveItem(Inventory inv, int i, ServerPlayer player, int time, String type, List<String> itemList) {
        ItemStack stack = inv.getItem(i);
        if (stack.isEmpty()) {
            //yeet
            return;
        }
        boolean returned = false;
        //the return of the figs
        Figs f = (Figs) FigManagerMC.FIGS;
        for (String s : itemList) {
            if (BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals(s)) {
                for (int j = 9; j < 36; ++j) {
                    ItemStack stack1 = inv.getItem(j);
                    if (stack1.is(ItemStack.EMPTY.getItem()) && !returned) {
                        stack1 = stack.copy();
                        inv.setItem(j, stack1);
                        inv.setItem(i, ItemStack.EMPTY);
                        returned = true;
                        if (type.equals("combat/hotbar")) {
                            String msg = f.combatHotbarMessage.value.replace("%T", String.valueOf(f.cooldown.value - time)).replace("%S", String.valueOf((Math.round((float) (f.cooldown.value - time) / 20))));
                            player.sendSystemMessage(Component.literal(msg), false);
                        }
                        if (type.equals("combat/armor")) {
                            String msg = f.combatArmorMessage.value.replace("%T", String.valueOf(f.cooldown.value - time)).replace("%S", String.valueOf((Math.round((float) (f.cooldown.value - time) / 20))));
                            player.sendSystemMessage(Component.literal(msg), false);
                        }
                        if (type.equals("general/hotbar")) {
                            player.sendSystemMessage(Component.literal(f.hotbarMessage.value), false);
                        }
                        if (type.equals("general/armor")) {
                            player.sendSystemMessage(Component.literal(f.armorMessage.value), false);
                        }
                    }
                }
                if (!returned) {

                    ItemStack dropStack = stack.copy(); 
                    inv.setItem(i, ItemStack.EMPTY);
                    player.drop(dropStack, false);
                }
            }
        }
    }
}
