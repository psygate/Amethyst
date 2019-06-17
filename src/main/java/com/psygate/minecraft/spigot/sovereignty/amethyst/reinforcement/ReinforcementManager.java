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

package com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement;

import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by psygate (https://github.com/psygate) on 08.03.2016.
 */
public class ReinforcementManager {
    private static ReinforcementManager instance;
    private ReinforcementCache cache = new ReinforcementCache();

    private ReinforcementManager() {

    }

    public Reinforcement getReinforcement(BlockKey key) throws ExecutionException {
        return cache.getReinforcement(key);
    }

    public Reinforcement createReinforcement(List<ItemStack> items, Set<BlockKey> blocks, Long groupID, boolean unbreakable, boolean isPublic, int maxStrength, int strength, long hardeningTime, long creationTime, UUID creator) throws ExecutionException, InterruptedException {
        return cache.createReinforcement(creator, creationTime, hardeningTime, strength, maxStrength, isPublic, unbreakable, groupID, blocks, items);
    }

    public boolean isReinforced(BlockKey key) throws ExecutionException {
        return cache.isReinforced(key);
    }

    public void flush() {
        cache.flush();
    }

    public static ReinforcementManager getInstance() {
        if (instance == null) {
            instance = new ReinforcementManager();
        }

        return instance;
    }
}
