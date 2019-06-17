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

package com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement;

import com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.tables.records.AmethystReinforcedBlockRecord;
import com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.tables.records.AmethystReinforcementDataRecord;
import com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.tables.records.AmethystReinforcementItemsRecord;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.exceptions.AlreadyReinforcedException;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by psygate on 17.04.2016.
 */
class LReinforcement implements Reinforcement {
    final ReinforcementCache source;
    private Long id;
    UUID creator;
    long creationTime;
    long hardeningTime;
    int strength;
    int maxStrength;
    boolean isPublic;
    boolean unbreakable;
    Long groupID;
    Set<BlockKey> blocks;
    List<ItemStack> items;

    ReinforcementState state;

    public LReinforcement(AmethystReinforcementDataRecord rec, List<AmethystReinforcedBlockRecord> blocks, List<AmethystReinforcementItemsRecord> items, ReinforcementCache source) {
        this(
                source,
                rec.getReinfid(),
                rec.getCreator(),
                rec.getCreationTime().getTime(),
                rec.getHardeningTime().getTime(),
                rec.getStrength(),
                rec.getMaxStrength(),
                rec.getIsPublic(),
                rec.getUnbreakable(),
                rec.getGroupId(),
                new HashSet<>(blocks.stream().map(b -> new BlockKey(b.getX(), b.getY(), b.getZ(), b.getWorldUuid())).collect(Collectors.toList())),
                new LinkedList<>(items.stream().map(i -> new ItemStack(i.getMaterial(), i.getAmount())).collect(Collectors.toList())),
                ReinforcementState.CLEAN
        );
    }

    public LReinforcement(ReinforcementCache source, Long id, UUID creator, long creationTime, long hardeningTime, int strength, int maxStrength, boolean isPublic, boolean unbreakable, Long groupID, Set<BlockKey> blocks, List<ItemStack> items, ReinforcementState state) {
        this.source = source;
        setId(id);
        this.creator = creator;
        this.creationTime = creationTime;
        this.hardeningTime = hardeningTime;
        this.strength = strength;
        this.maxStrength = maxStrength;
        this.isPublic = isPublic;
        this.unbreakable = unbreakable;
        this.groupID = groupID;
        this.blocks = blocks;
        this.items = items;
        this.state = state;
    }

    public LReinforcement(UUID creator, long creationTime, long hardeningTime, int strength, int maxStrength, boolean isPublic, boolean unbreakable, Long groupID, Set<BlockKey> blocks, List<ItemStack> items, ReinforcementCache source) {
        this(source, null, creator, creationTime, hardeningTime, strength, maxStrength, isPublic, unbreakable, groupID, blocks, items, ReinforcementState.NEW);
    }


    public ReinforcementState getState() {
        return state;
    }

    public void setState(ReinforcementState state) {
        if (this.state == state) {
        } else if (this.state == ReinforcementState.DELETE) {
        } else if (this.state == ReinforcementState.NEW && state != ReinforcementState.DELETE) {
        } else {
            this.state = state;
        }
    }

    public void forceSetState(ReinforcementState state) {
        this.state = state;
    }

    @Override
    public void delete() {
        this.state = ReinforcementState.DELETE;
    }

    public Long getId() {
        return id;
    }

    @Override
    public UUID getCreator() {
        return creator;
    }

    @Override
    public void setCreator(UUID creator) {
        this.creator = creator;
        setState(ReinforcementState.DIRTY);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(long creationTime) {
        setState(ReinforcementState.DIRTY);
        this.creationTime = creationTime;
    }

    @Override
    public long getHardeningTime() {
        return hardeningTime;
    }

    @Override
    public void setHardeningTime(long hardeningTime) {
        this.hardeningTime = hardeningTime;
        setState(ReinforcementState.DIRTY);
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public void setStrength(int strength) {
        this.strength = strength;
        setState(ReinforcementState.DIRTY);
    }

    @Override
    public int getMaxStrength() {
        return maxStrength;
    }

    @Override
    public void setMaxStrength(int maxStrength) {
        this.maxStrength = maxStrength;
        setState(ReinforcementState.DIRTY);
    }

    @Override
    public boolean isPublic() {
        return isPublic;
    }

    @Override
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
        setState(ReinforcementState.DIRTY);
    }

    @Override
    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        setState(ReinforcementState.DIRTY);
    }

    @Override
    public Long getGroupID() {
        return groupID;
    }

    @Override
    public void setGroupID(Long groupID) {
        this.groupID = groupID;
        setState(ReinforcementState.DIRTY);
    }

    @Override
    public void addBlock(BlockKey block) {
        try {
            if (source.isReinforced(block) && source.getReinforcement(block) != this) {
                throw new AlreadyReinforcedException(block);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        blocks.add(block);
        source.put(block, this);
        setState(ReinforcementState.DIRTY);
    }

    public void removeBlock(BlockKey block) {
        if (!blocks.contains(block)) {
            return;
        } else {
            blocks.remove(block);
            source.remove(block, this);
            setState(ReinforcementState.DIRTY);
        }
    }

    @Override
    public void addItem(ItemStack stack) {
        items.add(stack);
        setState(ReinforcementState.DIRTY);
    }

    @Override
    public Set<BlockKey> getBlocks() {
        return blocks;
    }

    public void setBlocks(Set<BlockKey> blocks) {
        setState(ReinforcementState.DIRTY);
        this.blocks = blocks;
    }

    @Override
    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        setState(ReinforcementState.DIRTY);
        this.items = items;
    }

    public List<Location> getLocation() {
        return blocks.stream().map(BlockKey::getLocation).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "LReinforcement{" +
                "id=" + id +
                ", creator=" + creator +
                ", creationTime=" + creationTime +
                ", hardeningTime=" + hardeningTime +
                ", strength=" + strength +
                ", maxStrength=" + maxStrength +
                ", isPublic=" + isPublic +
                ", unbreakable=" + unbreakable +
                ", groupID=" + groupID +
                ", blocks=" + blocks +
                ", items=" + items +
                ", state=" + state +
                '}';
    }

    public void setId(Long id) {
        assert this.id == null : "Reinforcement ID already set! (" + this.id + ") vs (" + id + ")";

        this.id = id;
    }
}