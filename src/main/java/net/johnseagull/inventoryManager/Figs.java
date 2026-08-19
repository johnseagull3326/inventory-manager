package net.johnseagull.inventoryManager;



import net.johnseagull.figManager.Fig.*;
import net.johnseagull.figManager.FigGroup;
import net.johnseagull.figManagerMC.DividerFig;
import net.minecraft.ChatFormatting;

import java.util.List;


public class Figs {
    public static Figs instance = new Figs();
    public BooleanFig enable = new BooleanFig("Enable Mod","Enable the mod",true);
    public BooleanFig doMessage = new  BooleanFig("Send Messages","Send messages to the player when an blocked item is detected. Check each section for per-category messages",true);


    public DividerFig itemBlocker = new DividerFig("Item Blocker", ChatFormatting.WHITE,true,false,false);
    public ListFig blockedItems = new ListFig("Blocked Items","Items that cannot be placed into a user's enderchest",1024,4,"Item registry name (example : minecraft:potion");
    public StringFig blockedItemMessage = new StringFig("Message","Message to show when a banned item is removed from the player's inventory","You cannot have the item! It was dropped on the ground.",80);

    public DividerFig other = new DividerFig("General", ChatFormatting.WHITE,true,false,false);
    public FigGroup generalItems = new FigGroup(List.of("armorItems","hotbarItems"),2,false,1f);
    public ListFig armorItems = new ListFig("Blocked armor items","Prevents certain items from being equipped in any of the four armor slots",1024,4,"Item registry name (example : minecraft:netherite_helmet)");
    public StringFig armorMessage = new StringFig("Armor Message","Message to show when an item was removed from a player's armor slots","You cannot have that item equipped.",80);
    public ListFig hotbarItems = new ListFig("Blocked hotbar items", "Prevents certain items from being placed in the hotbar",1024,4,"Item registry name (example : minecraft:ender_pearl)");
    public StringFig hotbarMessage = new StringFig("Hotbar Messaeg","Message to show when an item was removed from a player's hotbar","You cannot have that item in your hotbar",80);


    public DividerFig combat = new DividerFig("Combat", ChatFormatting.WHITE,true,false,false);
    public DividerFig combatNote = new DividerFig("For messages: %S will represent the amound of cooldown in seconds","%T will represent the amount of cooldown in ticks", ChatFormatting.GRAY,false,true,false);


    public FigGroup cooldownStuff = new FigGroup(List.of("cooldown","cooldownOverMessage"),2,false,1f);
    public IntFig cooldown = new IntFig("Cooldown","Amount of time a player will have to wait before they can use combat-banned items",1200,0,Integer.MAX_VALUE);
    public StringFig cooldownOverMessage = new StringFig("Restore message", "Message to appear when cooldown ends", "You can now use combat-banned items",64);

    public StringFig combatHotbarMessage = new StringFig("Hotbar Message", "Message to appear when players have blocked items in their hotbar.","You cannot have certain items in your hotbar in combat. Usable in %S seconds",80);
    public StringFig combatArmorMessage = new StringFig("Armor Message", "Message to appear when players have blocked items in their armor slots.","You cannot have certain items equipped in combat. Usable in %S seconds",80);

    public FigGroup combatItems = new FigGroup(List.of("combatArmorItems","combatHotbarItems"),2,false,1f);
    public ListFig combatHotbarItems = new ListFig("Combat Armor Items", "Items that cannot be equipped during combat",1024,4,"Item registry name (example : minecraft:totem_of_undying)");
    public ListFig combatArmorItems = new ListFig("Combat Hotbar Items", "Items that cannot be in the hotbar/offhand during combat",1024,4,"Item registry name (example : minecraft:totem_of_undying)");

    public DividerFig enderChest = new DividerFig("Ender chest", ChatFormatting.WHITE,true,false,false);
    public ListFig enderItems = new ListFig("Ender Chest Items","Items that cannot be placed into a user's enderchest",1024,4,"Item registry name (example : minecraft:dragon_egg");

    public DividerFig limiter = new DividerFig("Limiter", ChatFormatting.WHITE,true,false,false);
    public MapFig limitedItems = new MapFig("Item Limits","Sets a limit on the amount of a particular item a player can have. Additional items will be dropped on the ground.",128,4,"int","Item registry name (example : minecraft:totem_of_undying)","Integer : maximum amount of that item a player can have");
}
