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

import com.psygate.minecraft.spigot.sovereignty.amethyst.Amethyst;
import com.psygate.minecraft.spigot.sovereignty.amethyst.configuration.ReinforcementMaterial;
import com.psygate.minecraft.spigot.sovereignty.amethyst.events.PlayerSetReinforcementStateEvent;
import com.psygate.minecraft.spigot.sovereignty.amethyst.listeners.PlayerStateManager;
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.PlayerState;
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.ReinforcementState;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.CommandException;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusPlayerCommand;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.player.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.UUID;

/**
 * Created by psygate (https://github.com/psygate) on 09.03.2016.
 */
public class PlayerLockCommand extends NucleusPlayerCommand implements GroupCommand, StateSetCommand {
    public PlayerLockCommand() {
        super(0, 1);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        if (PlayerStateManager.getInstance().get(player.getUniqueId()).getReinforcementState() == ReinforcementState.LOCK && strings.length == 0) {
            PlayerStateManager.getInstance().get(player.getUniqueId()).setReinforcementState(ReinforcementState.NONE);
            player.sendMessage(ChatColor.GREEN + "Reinforcement mode LOCK now " + ChatColor.RED + "off.");
        } else {
            Group groupID = null;
            if (strings.length == 0) {
                groupID = getOrCreateDefaultGroup("sov_" + player.getName(), player.getUniqueId());
            } else {
                groupID = getGroup(strings[0], player.getUniqueId());
            }

            Optional<ReinforcementMaterial> material = Amethyst.getInstance().getConf().getReinforcementMaterial(player.getItemInHand().getType());

            if (!material.isPresent()) {
                throw new CommandException(player.getItemInHand().getType() + " is not a valid reinforcement material.");
            } else {
                player.sendMessage(ChatColor.YELLOW + groupID.getName() + " group chosen. Reinforcing with " + groupID.getName());
                setState(player, material.get(), groupID, ReinforcementState.LOCK);
            }
        }
    }


    @Override
    protected String[] getName() {
        return new String[]{"lock"};
    }
}
