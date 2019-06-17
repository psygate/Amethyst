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

import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.util.TimeUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Created by psygate (https://github.com/psygate) on 09.03.2016.
 */
public class ReinforcementMaterial {
    private final Material material;
    private final int amount;
    private final int breaks;
    private final boolean fireProof;
    private final boolean fireSupperssion;
    private final long hardeningTime;

    public ReinforcementMaterial(Material material, int amount, int breaks, boolean fireProof, boolean fireSupperssion, long hardeningTime) {
        this.material = material;
        this.amount = amount;
        this.breaks = breaks;
        this.fireProof = fireProof;
        this.fireSupperssion = fireSupperssion;
        this.hardeningTime = hardeningTime;
    }

    public ReinforcementMaterial(Map<String, ?> values) {
        material = Material.valueOf(((String) values.get("material")).toUpperCase());
        amount = (Integer) values.get("amount");
        breaks = (Integer) values.get("breaks");
        fireProof = (Boolean) values.get("fire_proof");
        fireSupperssion = (Boolean) values.get("fire_supression");
        hardeningTime = TimeUtil.parseTimeStringToMillis((String) values.get("hardening_time"));
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public int getBreaks() {
        return breaks;
    }

    public boolean isFireProof() {
        return fireProof;
    }

    public boolean isFireSupperssion() {
        return fireSupperssion;
    }

    public long getHardeningTime() {
        return hardeningTime;
    }

    public ItemStack asItemStack() {
        return new ItemStack(material, amount);
    }

    @Override
    public String toString() {
        return material.name() + "(" + amount + ")[" + breaks + "]";
    }
}
