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

package com.psygate.minecraft.spigot.sovereignty.amethyst.configuration;

import com.google.common.cache.CacheBuilder;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.util.TimeUtil;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.TimeUnit;

/**
 * Created by psygate on 13.05.2016.
 */
public class PlayerStateCacheSettings {
    private final long expireAfterAccess;
    private final long expireAfterWrite;
    private final int minimumSize;
    private final int maximumSize;

    public PlayerStateCacheSettings(ConfigurationSection conf) {
        expireAfterAccess = TimeUtil.parseTimeStringToMillis(conf.getString("expire_after_access"));
        expireAfterWrite = TimeUtil.parseTimeStringToMillis(conf.getString("expire_after_write"));
        minimumSize = conf.getInt("minimum_size");
        maximumSize = conf.getInt("maximum_size");
        /*

playerstatecache:
  expire_after_access: 10m
  expire_after_write: 10m
  minimum_size: 100
  maximum_size: 1000
         */
    }

    @SuppressWarnings("unchecked")
    public <K, V> CacheBuilder<K, V> getCacheBuilder() {
        return (CacheBuilder<K, V>) CacheBuilder.newBuilder().maximumSize(maximumSize)
                .initialCapacity(minimumSize)
                .expireAfterAccess(expireAfterAccess, TimeUnit.MILLISECONDS)
                .expireAfterWrite(expireAfterWrite, TimeUnit.MILLISECONDS);
    }
}
