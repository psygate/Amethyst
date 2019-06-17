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

import com.psygate.minecraft.spigot.sovereignty.amethyst.configuration.ReinforcementMaterial;
import com.psygate.minecraft.spigot.sovereignty.amethyst.events.PlayerSetReinforcementStateEvent;
import com.psygate.minecraft.spigot.sovereignty.amethyst.listeners.PlayerStateManager;
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.PlayerState;
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.ReinforcementState;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.OptionalLong;

/**
 * Created by psygate on 13.05.2016.
 */
public interface StateSetCommand {
    default void setState(Player player, ReinforcementMaterial material, Group group, ReinforcementState rstate) {
        ReinforcementState oldState = PlayerStateManager.getInstance().get(player.getUniqueId()).getReinforcementState();
        PlayerState state = PlayerStateManager.getInstance().get(player.getUniqueId());
        state.setGroupID(OptionalLong.of(group.getGroupID()));
        state.setUseMaterial(material);
        state.setReinforcementState(rstate);
        player.sendMessage(ChatColor.GREEN + "Reinforcement mode: " + rstate + ((oldState != null) ? ChatColor.GRAY + " (was: " + oldState + ")" : ""));
    }
}
