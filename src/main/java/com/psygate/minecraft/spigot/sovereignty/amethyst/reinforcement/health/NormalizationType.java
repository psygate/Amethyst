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

/**
 * Created by psygate on 30.04.2016.
 */
public enum NormalizationType {
    HAND((d) -> d.getHand()),
    WOOD(d -> d.getWood()),
    STONE(d -> d.getStone()),
    IRON(d -> d.getIron()),
    GOLD(d -> d.getGold()),
    DIAMOND(d -> d.getDiamond()),
    MINIMUM(d -> d.getMinimal());

    private final NormalizationSelector selector;

    NormalizationType(NormalizationSelector selector) {
        this.selector = selector;
    }

    double select(MaterialBreakData data) {
        return selector.select(data);
    }

    public NormalizationSelector getSelector() {
        return selector;
    }
}
