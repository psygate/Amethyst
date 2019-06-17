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

package com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.health;

import com.psygate.minecraft.spigot.sovereignty.amethyst.Amethyst;
import org.bukkit.Material;

import java.util.logging.Logger;

/**
 * Created by psygate on 13.05.2016.
 */
public class HealthManager {
    private final static Logger LOG = Amethyst.getLogger(HealthManager.class.getName());
    private static HealthManager instance;

    private HardnessHandler handler;
    private NormalizationSelector sel;
    private MaterialBreakData reference;

    private HealthManager() {
        sel = Amethyst.getInstance().getConf().getSettings().getNormalizationType().getSelector();
        handler = new HardnessHandler();
        reference = handler.get(Amethyst.getInstance().getConf().getSettings().getReferenceMaterial());
    }

    public static HealthManager getInstance() {
        if (instance == null) {
            instance = new HealthManager();
        }

        return instance;
    }

    public int getHealthAdjusted(Material material, int health) {
        if (Amethyst.getInstance().getConf().getSettings().isNormalizeReinforcementStrength()) {
            double matval = sel.select(handler.get(material));
            double norm = sel.select(reference);
            if (matval == 0) {
                matval = 1;
            }
            double actual = norm / matval;
            int val = Math.max(1, (int) Math.ceil(actual * health));
            if (val == Integer.MAX_VALUE || val <= 0 || val > 200000) {
                LOG.warning("Odd reinforcement value found for: " + material + " Base Health: " + health + " Material value: " + matval + " Normalization: " + norm + " Actual: " + val);
                return health;
            }

            LOG.finer("Health for " + material + " is " + val + " from " + health);
            return val;
        } else {
            return health;
        }
    }
}
