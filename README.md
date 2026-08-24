This branch is for 1.21.11.

# Inventory Manager

This mod lets you add several rules for setting limits on what items players can have, and when.
All core functions are done server-side except the configuration GUI. (More on that below)

## Features

You can set an individual blacklist for each of the following rules:
- Item blocker - completely blocks an item from the players inventory
- Ender chest blocker - completely blocks an item from the player's enderchest
- Item limiter - limits the amount of an item the player can have
- Armor blocker - blocks an item from being placed in any of the player's armor slots
- Hotbar blocker - blocks an item from being placed in the player's hotbar, item will be moved to the player's inventory or dropped if no empty space
Each of these rules have independently changeable messages to show when an item was moved/dropped in/from their inventory.

There is also a combat system that lets you set a cooldown and anyone who is hit will be considered in combat until the cooldown runs out. There can be a customized message to show how much time is left in the cooldown in ticks or seconds.
You can set rules for what items you cannot have during combat:
- Combat Armor blocker - blocks an item from the player's armor slots if they have been attacked and are within cooldown
- Combat Hotbar blocker - blocks an item from the player's hotbar if they have been attacked and are within cooldown

![Banner](https://cdn.modrinth.com/data/cached_images/5e9f09bbf474088b45814edcbb4ec78c35883f81.png)

## Configuration

This mod has an embedded version of my own configuration library which allows for rich data types and nice GUI's.

An operator with the mod installed on their client can run `/inventory_manager_config` to open a GUI for editing the mod's config.

You can also go to `config/inventory_manager/config.json` to edit and use `info.txt` in the same directory as a reference if not using the GUI.
