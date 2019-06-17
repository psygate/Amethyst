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

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * Created by psygate (https://github.com/psygate) on 09.03.2016.
 */
public class Configuration {
    private final Map<Material, ReinforcementMaterial> reinforcementMaterials = new HashMap<>();
    private final Set<Material> reinforcible = new HashSet<>(Arrays.asList(Material.values()));
    private final Settings settings;
    private final PlayerStateCacheSettings playerCacheSettings;

    public Configuration(FileConfiguration conf) {
        List<Map<?, ?>> reinfmatsec = conf.getMapList("reinforcements");
        for (Map<?, ?> map : reinfmatsec) {
            ReinforcementMaterial mat = new ReinforcementMaterial((Map<String, ?>) map);
            reinforcementMaterials.put(mat.getMaterial(), mat);
        }

        for (String s : conf.getStringList("non-reinforcible")) {
            reinforcible.remove(Material.valueOf(s.toUpperCase()));
        }

        settings = new Settings(conf.getConfigurationSection("settings"));
        playerCacheSettings = new PlayerStateCacheSettings(conf.getConfigurationSection("playerstatecache"));
    }

    public Map<Material, ReinforcementMaterial> getReinforcementMaterials() {
        return reinforcementMaterials;
    }

    public Optional<ReinforcementMaterial> getReinforcementMaterial(Material type) {
        return Optional.ofNullable(reinforcementMaterials.get(type));
    }

    public boolean isReinforcible(Material mat) {
        return reinforcible.contains(mat);
    }

    public Set<Material> getReinforcible() {
        return reinforcible;
    }

    public Settings getSettings() {
        return settings;
    }

    public PlayerStateCacheSettings getPlayerCacheSettings() {
        return playerCacheSettings;
    }
}
