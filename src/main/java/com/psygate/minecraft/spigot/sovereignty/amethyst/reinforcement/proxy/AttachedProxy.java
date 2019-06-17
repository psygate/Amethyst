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

import com.sun.org.apache.bcel.internal.generic.LADD;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.*;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by psygate (https://github.com/psygate) on 13.03.2016.
 */
public class AttachedProxy implements BlockReinforcementProxy {

    public Collection<Block> map(Block block) {
        Block rel;
        Block floor;

        if (block.getType() == Material.TORCH) {
            Torch torch = (Torch) block.getState().getData();
            rel = block.getRelative(torch.getAttachedFace());
        } else if (block.getType() == Material.REDSTONE_TORCH_ON || block.getType() == Material.REDSTONE_TORCH_OFF) {
            RedstoneTorch torch = (RedstoneTorch) block.getState().getData();
            rel = block.getRelative(torch.getAttachedFace());
        } else if (block.getType() == Material.TRAP_DOOR || block.getType() == Material.IRON_TRAPDOOR) {
            TrapDoor door = (TrapDoor) block.getState().getData();
            rel = block.getRelative(door.getAttachedFace());
        } else if (block.getType() == Material.LEVER) {
            Lever lever = (Lever) block.getState().getData();
            rel = block.getRelative(lever.getAttachedFace());
        } else if (block.getType() == Material.LADDER) {
            Ladder ladder = (Ladder) block.getState().getData();
            rel = block.getRelative(ladder.getAttachedFace());
        } else if (block.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) block.getState().getData();
            rel = block.getRelative(sign.getAttachedFace());
        } else if (block.getType() == Material.SIGN_POST) {
            rel = block.getRelative(BlockFace.DOWN);
        } else {
            throw new IllegalArgumentException();
        }

        return Arrays.asList(block, rel);
    }

    @Override
    public Collection<Material> getTypes() {
        return Arrays.asList(
                Material.TORCH,
                Material.REDSTONE_TORCH_ON,
                Material.REDSTONE_TORCH_OFF,
                Material.TRAP_DOOR,
                Material.IRON_TRAPDOOR,
                Material.LADDER,
                Material.LEVER,
                Material.SIGN_POST,
                Material.WALL_SIGN
        );
    }
}
