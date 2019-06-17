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
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.ReinforcementManager;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.CommandException;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusOPCommand;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

/**
 * Created by psygate on 31.05.2016.
 */
public class AreaReinforceCommand extends NucleusOPCommand {
    public AreaReinforceCommand() {
        super(4, 4);
    }

    @Override
    protected void subOnCommand(Player player, Command command, String s, String[] strings) throws Exception {
        //usage: /areareinforce <groupid> <reinforcement_material> <material> <radius>
        String groupName = strings[0];
        Material type = Material.valueOf(strings[1].toUpperCase());
        Material reinf = Material.valueOf(strings[2].toUpperCase());
        int radius = Integer.parseInt(strings[3]);

        Group g = GroupManager.getInstance().getGroup(groupName).orElseThrow(() -> new CommandException("Group \"" + groupName + "\" not found."));
        ReinforcementMaterial mat = Amethyst.getInstance().getConf().getReinforcementMaterial(type).orElseThrow(() -> new CommandException("Reinforcement material \"" + type + "\" not found."));

        player.sendMessage(ChatColor.YELLOW + "Starting reinforcement process. This might take a while.");
        Block ref = player.getLocation().getBlock();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -radius; y <= radius; y++) {
                    Block rel = ref.getRelative(x, y, z);
                    if (rel.getType() == reinf) {
                        BlockKey key = new BlockKey(rel);
                        if (!ReinforcementManager.getInstance().isReinforced(key)) {
                            HashSet<BlockKey> set = new HashSet<>();
                            set.add(key);
                            ReinforcementManager.getInstance().createReinforcement(
                                    Collections.emptyList(),
                                    set,
                                    g.getGroupID(),
                                    false,
                                    false,
                                    mat.getBreaks(),
                                    mat.getBreaks(),
                                    System.currentTimeMillis() + mat.getHardeningTime(),
                                    System.currentTimeMillis(),
                                    player.getUniqueId()
                            );
                        }
                    }
                }
            }
        }
        player.sendMessage(ChatColor.GREEN + "Reinforcement done.");
    }

    @Override
    protected String[] getName() {
        return new String[]{"areareinforce"};
    }
}
