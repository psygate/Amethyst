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

package com.psygate.minecraft.spigot.sovereignty.amethyst.events;
/**
 * Created by psygate (https://github.com/psygate) on 15.03.2016.
 */

import com.psygate.minecraft.spigot.sovereignty.amethyst.state.ReinforcementState;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.concurrent.Callable;

public class PlayerCreateReinforcementEvent extends Event implements Callable<Boolean>, Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Boolean call() {
        Bukkit.getServer().getPluginManager().callEvent(this);
        return cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    private final Player player;
    private final Block target;
    private Collection<Block> reinforcedBlocks;
    private final ReinforcementState mode;
    private Collection<ItemStack> costs;
    private long hardeningTime;
    private int strength;
    private Long groupID;

    public PlayerCreateReinforcementEvent(Player player, Block key, Collection<Block> reinforcedBlocks, ReinforcementState mode, Collection<ItemStack> costs, long hardeningTime, int strength, long groupID) {
        this.player = player;
        this.target = key;
        this.reinforcedBlocks = reinforcedBlocks;
        this.mode = mode;
        this.costs = costs;
        this.hardeningTime = hardeningTime;
        this.strength = strength;
        this.groupID = groupID;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getTarget() {
        return target;
    }

    public Collection<Block> getReinforcedBlocks() {
        return reinforcedBlocks;
    }

    public void setReinforcedBlocks(Collection<Block> reinforcedBlocks) {
        this.reinforcedBlocks = reinforcedBlocks;
    }

    public ReinforcementState getMode() {
        return mode;
    }

    public Collection<ItemStack> getCosts() {
        return costs;
    }

    public void setCosts(Collection<ItemStack> costs) {
        this.costs = costs;
    }

    public long getHardeningTime() {
        return hardeningTime;
    }

    public void setHardeningTime(long hardeningTime) {
        this.hardeningTime = hardeningTime;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        if (strength <= 0) {
            throw new IllegalArgumentException("Strength cannot be less than or equal to zero.");
        }
        this.strength = strength;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }
}