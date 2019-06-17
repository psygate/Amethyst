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

package com.psygate.minecraft.spigot.sovereignty.amethyst.commands;

import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.CommandException;
import org.bukkit.ChatColor;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by psygate on 13.05.2016.
 */
public interface YesNoCommand {
    enum Output {
        TRUE(true), FALSE(false);
        private final boolean booleanValue;

        Output(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }

        public boolean asBooleanValue() {
            return this.booleanValue;
        }
    }

    default Output mapString(String str) {
        if (str.equalsIgnoreCase("true")
                || str.equalsIgnoreCase("yes")
                || str.equalsIgnoreCase("+")
                || str.equalsIgnoreCase("t")
                || str.equalsIgnoreCase("on")) {
            return Output.TRUE;
        } else {
            return Output.FALSE;
        }
    }

    default String asString(boolean value) {
        return (value) ? "on" : "off";
    }
}
