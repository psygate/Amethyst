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
import org.bukkit.material.Bed;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by psygate (https://github.com/psygate) on 14.03.2016.
 */
public class DoubleBlockProxy implements BlockReinforcementProxy {
    private final static BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};

    @Override
    public Collection<Block> map(Block block) {
        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
            for (BlockFace face : faces) {
                if (block.getRelative(face).getType() == block.getType()) {
                    return Arrays.asList(
                            block,
                            block.getRelative(face)
                    );
                }
            }
        } else if (block.getType() == Material.BED || block.getType() == Material.BED_BLOCK) {
            Bed bed = (Bed) block.getState().getData();
            return Arrays.asList(
                    block,
                    block.getRelative(bed.getFacing())
            );
        }
        return Arrays.asList(block);
    }

    @Override
    public Collection<Material> getTypes() {
        return Arrays.asList(
                Material.CHEST,
                Material.TRAPPED_CHEST,
                Material.BED,
                Material.BED_BLOCK
        );
    }
}
