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

package com.psygate.minecraft.spigot.sovereignty.amethyst.listeners;

import com.google.common.cache.*;
import com.psygate.minecraft.spigot.sovereignty.amethyst.Amethyst;
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.PlayerState;
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.ReinforcementState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.OptionalLong;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by psygate on 13.05.2016.
 */
public class PlayerStateManager {
    private static PlayerStateManager instance;
    private LoadingCache<UUID, PlayerState> stateCache =
            Amethyst.getInstance().getConf().getPlayerCacheSettings().getCacheBuilder()
                    .removalListener(new RemovalListener<UUID, PlayerState>() {
                        @Override
                        public void onRemoval(RemovalNotification<UUID, PlayerState> removalNotification) {
                            if (Amethyst.getInstance().isEnabled()) {
                                Bukkit.getScheduler().runTask(Amethyst.getInstance(), () -> {
                                    Player player = Bukkit.getPlayer(removalNotification.getKey());

                                    if (player != null && removalNotification.getValue().getReinforcementState() != ReinforcementState.NONE) {
                                        player.sendMessage(ChatColor.YELLOW + "Reinforcement state reset.");
                                    }
                                });
                            }
                        }
                    })
                    .build(new CacheLoader<UUID, PlayerState>() {
                        @Override
                        public PlayerState load(UUID uuid) throws Exception {
                            return new PlayerState(uuid, ReinforcementState.NONE, OptionalLong.empty(), null);
                        }
                    });

    private PlayerStateManager() {

    }

    public PlayerState get(UUID player) {
        return stateCache.getUnchecked(player);
    }

    public void invalidateAll() {
        stateCache.invalidateAll();
    }

    public static PlayerStateManager getInstance() {
        if (instance == null) {
            instance = new PlayerStateManager();
        }

        return instance;
    }

    public void invalidate(UUID uniqueId) {
        if (stateCache.getIfPresent(uniqueId) != null) {
            stateCache.invalidate(uniqueId);
        }
    }
}
