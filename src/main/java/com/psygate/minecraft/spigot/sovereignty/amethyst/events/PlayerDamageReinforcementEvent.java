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

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.concurrent.Callable;

public class PlayerDamageReinforcementEvent extends Event implements Callable<Boolean>, Cancellable {
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

    private final Block target;
    private final Player player;
    private final boolean bypassingReinforcement;
    private int damage;
    private int health;
    private int maxHealth;
    private long groupID;

    public PlayerDamageReinforcementEvent(Block target, Player player, int damage, int health, int maxHealth, boolean bypassingReinforcement, long groupID) {
        this.target = target;
        this.player = player;
        this.damage = damage;
        this.health = health;
        this.maxHealth = maxHealth;
        this.bypassingReinforcement = bypassingReinforcement;
        this.groupID = groupID;
    }

    public Block getTarget() {
        return target;
    }

    public Player getPlayer() {
        return player;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public boolean isBypassingReinforcement() {
        return bypassingReinforcement;
    }

    public long getGroupID() {
        return groupID;
    }
}