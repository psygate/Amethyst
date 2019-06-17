/*
 *     Copyright (C) 2016 psygate (https://github.com/psygate)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 */

package com.psygate.minecraft.spigot.sovereignty.amethyst.commands;

import com.psygate.minecraft.spigot.sovereignty.amethyst.events.PlayerResetReinforcementStateEvent;
import com.psygate.minecraft.spigot.sovereignty.amethyst.listeners.PlayerStateManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusPlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * Created by psygate (https://github.com/psygate) on 09.03.2016.
 */
public class PlayerOffCommand extends NucleusPlayerCommand {
    public PlayerOffCommand() {
        super(0, 1);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        player.sendMessage(ChatColor.YELLOW + "All modes reset.");
        PlayerStateManager.getInstance().invalidate(player.getUniqueId());
    }

    @Override
    protected String[] getName() {
        return new String[]{"reinforcementreset"};
    }
}
