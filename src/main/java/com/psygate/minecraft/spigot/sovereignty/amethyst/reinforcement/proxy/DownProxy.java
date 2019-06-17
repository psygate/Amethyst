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

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by psygate (https://github.com/psygate) on 14.03.2016.
 */
public class DownProxy implements BlockReinforcementProxy {
    @Override
    public Collection<Block> map(Block block) {
        return Arrays.asList(
                block,
                block.getRelative(BlockFace.DOWN)
        );
    }

    @Override
    public Collection<Material> getTypes() {
        return Arrays.asList(
                Material.RAILS,
                Material.ACTIVATOR_RAIL,
                Material.DETECTOR_RAIL,
                Material.POWERED_RAIL,
                Material.REDSTONE,
                Material.REDSTONE_COMPARATOR,
                Material.REDSTONE_COMPARATOR_OFF,
                Material.REDSTONE_COMPARATOR_ON,
                Material.REDSTONE_WIRE,
                Material.CROPS,
                Material.CARROT,
                Material.POTATO,
                Material.NETHER_WARTS,
                Material.SAPLING,
                Material.FLOWER_POT,
                Material.YELLOW_FLOWER,
                Material.RED_ROSE,
                Material.CACTUS,
                Material.ANVIL,
                Material.DIODE_BLOCK_ON,
                Material.DIODE_BLOCK_OFF,
                Material.SIGN_POST,
                Material.SUGAR_CANE
        );
    }
}
