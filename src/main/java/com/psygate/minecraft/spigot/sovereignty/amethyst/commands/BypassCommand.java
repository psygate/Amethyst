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

import com.psygate.minecraft.spigot.sovereignty.amethyst.events.PlayerSetBypassModeEvent;
import com.psygate.minecraft.spigot.sovereignty.amethyst.listeners.PlayerStateManager;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.CommandException;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusPlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by psygate (https://github.com/psygate) on 09.03.2016.
 */
public class BypassCommand extends NucleusPlayerCommand implements YesNoCommand {

    public BypassCommand() {
        super(0, 1);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] args) throws Exception {
        boolean newMode = getModeToSet(player.getUniqueId(), args);

        PlayerStateManager.getInstance().get(player.getUniqueId()).setBypassing(newMode);
        player.sendMessage("Bypass mode now " + ((newMode) ? ChatColor.GREEN : ChatColor.RED) + asString(newMode));
    }

    private boolean getModeToSet(UUID uuid, String[] args) {
        if (args.length == 0) {
            return !PlayerStateManager.getInstance().get(uuid).isBypassing();
        } else {
            return mapString(args[0]).asBooleanValue();
        }
    }

    @Override
    protected String[] getName() {
        return new String[]{"bypass"};
    }
}
