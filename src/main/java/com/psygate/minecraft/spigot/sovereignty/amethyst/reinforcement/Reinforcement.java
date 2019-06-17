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

import java.util.Collection;
import java.util.UUID;

/**
 * Created by psygate (https://github.com/psygate) on 26.03.2016.
 */
public interface Reinforcement {
    UUID getCreator();

    void setCreator(UUID creator);

    long getCreationTime();

    void setCreationTime(long creationTime);

    long getHardeningTime();

    void setHardeningTime(long hardeningTime);

    int getStrength();

    void setStrength(int strength);

    int getMaxStrength();

    void setMaxStrength(int maxStrength);

    boolean isPublic();

    void setPublic(boolean aPublic);

    boolean isUnbreakable();

    void setUnbreakable(boolean unbreakable);

    Long getGroupID();

    void setGroupID(Long groupID);

    void addBlock(BlockKey block);

    void addItem(ItemStack stack);

    Collection<BlockKey> getBlocks();

    Collection<ItemStack> getItems();

    void delete();

}
