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
 * Created by psygate (https://github.com/psygate) on 09.03.2016.
 */

import com.psygate.minecraft.spigot.sovereignty.amethyst.configuration.ReinforcementMaterial;
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.ReinforcementState;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.OptionalLong;
import java.util.UUID;
import java.util.concurrent.Callable;

public class PlayerSetReinforcementStateEvent extends Event implements Callable<Boolean>, Cancellable {
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

    private ReinforcementState state;
    private ReinforcementMaterial material;
    private OptionalLong groupRecord;
    private UUID player;

    public PlayerSetReinforcementStateEvent(ReinforcementState state, ReinforcementMaterial material, OptionalLong groupRecord, UUID player) {
        this.state = state;
        this.material = material;
        this.groupRecord = groupRecord;
        this.player = player;
    }

    public ReinforcementState getState() {
        return state;
    }

    public void setState(ReinforcementState state) {
        this.state = state;
    }

    public ReinforcementMaterial getMaterial() {
        return material;
    }

    public void setMaterial(ReinforcementMaterial material) {
        this.material = material;
    }

    public OptionalLong getGroupID() {
        return groupRecord;
    }

//    public void setGroupRecord(Optional<IvoryGroupsRecord> groupRecord) {
//        this.groupRecord = groupRecord;
//    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }
}