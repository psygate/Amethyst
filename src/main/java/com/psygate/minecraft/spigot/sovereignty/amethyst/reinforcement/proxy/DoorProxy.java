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

package com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.proxy;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by psygate (https://github.com/psygate) on 13.03.2016.
 */
public class DoorProxy implements BlockReinforcementProxy {

    @Override
    public Collection<Block> map(Block block) {
        ArrayList<Block> blocks = new ArrayList<>(3);

        Block rel;


        if (block.getRelative(BlockFace.UP).getType() == block.getType()) {
            rel = block.getRelative(BlockFace.UP);
        } else {
            rel = block;
        }

        for (int i = 0; i < 3; i++) {
            blocks.add(rel.getRelative(BlockFace.DOWN, i));
        }

        return blocks;
    }

    @Override
    public Collection<Material> getTypes() {
        return Arrays.asList(
                Material.ACACIA_DOOR,
                Material.BIRCH_DOOR,
                Material.DARK_OAK_DOOR,
                Material.IRON_DOOR,
                Material.JUNGLE_DOOR,
                Material.SPRUCE_DOOR,
                Material.WOODEN_DOOR,
                Material.WOOD_DOOR
        );
    }
}
