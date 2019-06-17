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

import java.util.Optional;
import java.util.UUID;

/**
 * Created by psygate on 13.05.2016.
 */
public interface GroupCommand {
    default Group getGroup(String name, UUID player) {
        Optional<? extends Group> groupopt = GroupManager.getInstance().
                getGroup(name);
        try {
            if (!groupopt.isPresent()) {
                throw new CommandException("Group not found.");
            } else {
                boolean member = groupopt.get().hasMemberWithRankGE(player, Rank.MODERATOR);
                if (!member) {
                    throw new CommandException("Insufficient permissions.");
                }
                return groupopt.get();
            }
        } catch (Exception e) {
            if (e instanceof CommandException) {
                throw (CommandException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    default Group getOrCreateDefaultGroup(String name, UUID player) {
        Optional<? extends Group> groupopt = GroupManager.getInstance().
                getGroup(name);

        try {
            if (!groupopt.isPresent()) {
                Optional<? extends Group> newgroupopt = GroupManager.getInstance().createGroup(name, player);
                if (!newgroupopt.isPresent()) {
                    throw new CommandException("Group creation failed, please reissue command.");
                } else {
                    return newgroupopt.get();
                }
            } else {

                boolean member = groupopt.get().hasMemberWithRankGE(player, Rank.MODERATOR);
                if (!member) {
                    throw new CommandException("Insufficient permissions to reinforce with this group.");
                }

                return groupopt.get();
            }
        } catch (Exception e) {
            if (e instanceof CommandException) {
                throw (CommandException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }
}
