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

package com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.exceptions;

import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;

/**
 * Created by psygate (https://github.com/psygate) on 26.03.2016.
 */
public class AlreadyReinforcedException extends RuntimeException {
    private final BlockKey key;

    public AlreadyReinforcedException(BlockKey key) {
        this("Reinforcement not found for " + key, key);
    }

    public AlreadyReinforcedException(String message, BlockKey key) {
        super(message);
        this.key = key;
    }

    public AlreadyReinforcedException(String message, Throwable cause, BlockKey key) {
        super(message, cause);
        this.key = key;
    }

    public AlreadyReinforcedException(Throwable cause, BlockKey key) {
        this("Reinforcement not found for " + key, cause, key);

    }

    public AlreadyReinforcedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, BlockKey key) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.key = key;
    }
}
