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
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.PlayerState;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusPlayerCommand;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.player.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by psygate (https://github.com/psygate) on 09.03.2016.
 */
public class DebugPlayerCommand extends NucleusPlayerCommand {

    public DebugPlayerCommand() {
        super(1, 1);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        try {
            UUID uuid = PlayerManager.getInstance().toUUID(strings[0]);
            PlayerState state = Amethyst.getInstance().getReinforcementListener().getPlayerState(uuid);

            player.sendMessage(new String[]{
                    "State for " + strings[0],
                    "Group: " + state.getGroupID().orElseGet(() -> -1),
                    "Bypassing: " + state.isBypassing(),
                    "Info/Inspecting: " + state.isInspecting(),
                    "Material: " + state.getUseMaterial()
            });

        } catch (Exception e) {
            player.sendMessage("Couldn't find player: " + strings[0]);
        }
    }

    @Override
    protected String[] getName() {
        return new String[]{"debugplayer"};
    }
}
