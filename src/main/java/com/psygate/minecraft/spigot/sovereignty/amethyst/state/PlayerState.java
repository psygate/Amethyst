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

package com.psygate.minecraft.spigot.sovereignty.amethyst.state;

import com.psygate.minecraft.spigot.sovereignty.amethyst.configuration.ReinforcementMaterial;

import java.util.OptionalLong;
import java.util.UUID;

/**
 * Created by psygate (https://github.com/psygate) on 08.03.2016.
 */
public class PlayerState {
    private UUID playerUUID;
    private ReinforcementState reinforcementState;
    private long creationTime = System.currentTimeMillis();
    private boolean bypassing = true;
    private OptionalLong groupID;
    private ReinforcementMaterial useMaterial;
    private boolean inspecting = false;

    public PlayerState(UUID playerUUID, ReinforcementState reinforcementState, OptionalLong groupID, ReinforcementMaterial useMaterial) {
        this.playerUUID = playerUUID;
        this.reinforcementState = reinforcementState;
        this.groupID = groupID;
        this.useMaterial = useMaterial;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public ReinforcementState getReinforcementState() {
        return reinforcementState;
    }

    public void setReinforcementState(ReinforcementState reinforcementState) {
        this.reinforcementState = reinforcementState;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isBypassing() {
        return bypassing;
    }

    public void setBypassing(boolean bypassing) {
        this.bypassing = bypassing;
    }

    public OptionalLong getGroupID() {
        return groupID;
    }

    public void setGroupID(OptionalLong groupID) {
        this.groupID = groupID;
    }

    public ReinforcementMaterial getUseMaterial() {
        return useMaterial;
    }

    public void setUseMaterial(ReinforcementMaterial useMaterial) {
        this.useMaterial = useMaterial;
    }

    public boolean isInspecting() {
        return inspecting;
    }

    public void setInspecting(boolean inspecting) {
        this.inspecting = inspecting;
    }
}
