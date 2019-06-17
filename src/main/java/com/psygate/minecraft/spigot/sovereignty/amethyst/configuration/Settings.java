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

import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.health.NormalizationType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by psygate (https://github.com/psygate) on 13.03.2016.
 */
public class Settings {
    private double redstoneDistance;
    private boolean normalizeReinforcementStrength;
    private Set<Material> container = new HashSet<>();
    private NormalizationType normalizationType;
    private Material referenceMaterial;

    public Settings(ConfigurationSection conf) {
        this.redstoneDistance = conf.getDouble("redstone_distance");
        conf.getStringList("accessibles").stream().map(Material::valueOf).forEach(container::add);
        this.normalizeReinforcementStrength = conf.getBoolean("normalize_reinforcement_strengths");
        this.normalizationType = NormalizationType.valueOf(conf.getString("normalize_to").trim().toUpperCase());
        this.referenceMaterial = Material.valueOf(conf.getString("normalization_reference_material").trim().toUpperCase());
    }

    public double getRedstoneDistance() {
        return redstoneDistance;
    }

    public void setRedstoneDistance(double redstoneDistance) {
        this.redstoneDistance = redstoneDistance;
    }

    public boolean isContainer(Material type) {
        return container.contains(type);
    }

    public boolean isNormalizeReinforcementStrength() {
        return normalizeReinforcementStrength;
    }

    public NormalizationType getNormalizationType() {
        return normalizationType;
    }

    public Material getReferenceMaterial() {
        return referenceMaterial;
    }
}
