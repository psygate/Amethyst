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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.psygate.minecraft.spigot.sovereignty.amethyst.Amethyst;
import com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.tables.records.AmethystReinforcedBlockRecord;
import com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.tables.records.AmethystReinforcementDataRecord;
import com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.tables.records.AmethystReinforcementItemsRecord;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.exceptions.AlreadyReinforcedException;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.exceptions.ReinforcementNotFoundException;
import com.psygate.minecraft.spigot.sovereignty.nucleus.Nucleus;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.Tables.*;

/**
 * Created by psygate (https://github.com/psygate) on 26.03.2016.
 */
class ReinforcementCache {
    private final static Logger LOG = Amethyst.getLogger("ReinforcementCache");
    private final Cache<BlockKey, Optional<LReinforcement>> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(20, TimeUnit.MINUTES)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .initialCapacity(100_000)
            .maximumSize(2_000_000)
            .removalListener(new RemovalListener<BlockKey, Optional<LReinforcement>>() {
                @Override
                public void onRemoval(RemovalNotification<BlockKey, Optional<LReinforcement>> removalNotification) {
                    if (removalNotification.getValue().isPresent()) {
                        dismiss(removalNotification.getValue().get());
                    }
                }
            })
            .build();

    private void dismiss(LReinforcement r) {
        switch (r.getState()) {
            case NEW:
                insertNewReinforcement(r);
                break;
            case CLEAN:
                break;
            case DELETE:
                deleteReinforcement(r);
                break;
            case DIRTY:
                updateReinforcement(r);
                break;
        }
    }

    private void updateReinforcement(LReinforcement r) {
        Amethyst.DBI().submit((conf) -> {
            DSLContext ctx = DSL.using(conf);
            ctx.update(AMETHYST_REINFORCEMENT_DATA)
                    .set(new AmethystReinforcementDataRecord(
                                    r.getId(),
                                    new Timestamp(r.creationTime),
                                    r.creator,
                                    r.groupID,
                                    new Timestamp(r.hardeningTime),
                                    r.strength,
                                    r.maxStrength,
                                    r.isPublic,
                                    r.unbreakable
                            )
                    )
                    .where(AMETHYST_REINFORCEMENT_DATA.REINFID.eq(r.getId()))
                    .execute();
            ctx.deleteFrom(AMETHYST_REINFORCED_BLOCK).where(AMETHYST_REINFORCED_BLOCK.REINFID.eq(r.getId())).execute();
            ctx.deleteFrom(AMETHYST_REINFORCEMENT_ITEMS).where(AMETHYST_REINFORCEMENT_ITEMS.REINFID.eq(r.getId())).execute();

            ctx.batchInsert(r.blocks.stream().map(b -> new AmethystReinforcedBlockRecord(r.getId(), b.getX(), b.getY(), b.getZ(), b.getUuid())).collect(Collectors.toList())).execute();
            ctx.batchInsert(r.items.stream().map(i -> new AmethystReinforcementItemsRecord(r.getId(), i.getType(), i.getAmount())).collect(Collectors.toList())).execute();
            r.setState(ReinforcementState.CLEAN);
        });

        r.setState(ReinforcementState.CLEAN);
    }

    private void deleteReinforcement(LReinforcement r) {
        r.setState(ReinforcementState.DELETE);
        Amethyst.DBI().submit((conf) -> {
            DSL.using(conf).deleteFrom(AMETHYST_REINFORCEMENT_DATA).where(AMETHYST_REINFORCEMENT_DATA.REINFID.eq(r.getId())).execute();
        });

        cache.invalidateAll(r.getBlocks());
    }

    public void flush() {
        cache.invalidateAll();
    }

    private void insertNewReinforcement(LReinforcement r) {
        Amethyst.DBI().submit((conf) -> {
            DSLContext ctx = DSL.using(conf);
            Long rid = ctx.insertInto(AMETHYST_REINFORCEMENT_DATA)
                    .set(new AmethystReinforcementDataRecord(
                                    null,
                                    new Timestamp(r.creationTime),
                                    r.creator,
                                    r.groupID,
                                    new Timestamp(r.hardeningTime),
                                    r.strength,
                                    r.maxStrength,
                                    r.isPublic,
                                    r.unbreakable
                            )
                    ).returning(AMETHYST_REINFORCEMENT_DATA.REINFID)
                    .fetchOne().getReinfid();
            r.setId(rid);

            List<AmethystReinforcedBlockRecord> reinforced = ctx.selectFrom(AMETHYST_REINFORCED_BLOCK)
                    .where(r.blocks.stream().map(blockkey ->
                                    AMETHYST_REINFORCED_BLOCK.X.eq(blockkey.getX())
                                            .and(AMETHYST_REINFORCED_BLOCK.Y.eq(blockkey.getY()))
                                            .and(AMETHYST_REINFORCED_BLOCK.Z.eq(blockkey.getZ()))
                                            .and(AMETHYST_REINFORCED_BLOCK.WORLD_UUID.eq(blockkey.getUuid()))
                            ).reduce(DSL.falseCondition(), (a, b) -> a.or(b))
                    ).fetch();

            if (reinforced.size() > 0) {
                System.err.println("ALREADY REINFORCED BLOCK FOUND.\n" + reinforced);
            }
            try {
                ctx.batchInsert(r.blocks.stream().map(b -> new AmethystReinforcedBlockRecord(r.getId(), b.getX(), b.getY(), b.getZ(), b.getUuid())).collect(Collectors.toList())).execute();
                r.setState(ReinforcementState.CLEAN);
            } catch (Exception e) {
                Nucleus.getMetricRegistry().counter("amethyst-single-block-insertion-fallback").inc();
                System.err.println("Failed to batch insert blocks, falling back to single mode. (" + e.getMessage() + ")");
                for (BlockKey block : r.blocks) {
                    try {
                        ctx.executeInsert(new AmethystReinforcedBlockRecord(r.getId(), block.getX(), block.getY(), block.getZ(), block.getUuid()));
//                        r.setState(ReinforcementState.CLEAN);
                    } catch (Exception ex) {
                        System.err.println("Failed to reinforce: " + block + " (" + ex.getMessage() + ")");
                    }
                }
            }
            try {
                ctx.batchInsert(r.items.stream().map(i -> new AmethystReinforcementItemsRecord(r.getId(), i.getType(), i.getAmount())).collect(Collectors.toList())).execute();
            } catch (Exception e) {
                System.err.println("Failed to persist items.");
            }
            r.forceSetState(ReinforcementState.CLEAN);
        });
    }

    public boolean isReinforced(BlockKey key) throws ExecutionException {
        Optional<LReinforcement> rf = getCached(key);
//        Optional<LReinforcement> rf = cache.get(key, () -> load(key));

        return rf.isPresent() && rf.get().getState() != ReinforcementState.DELETE;
    }

    private Optional<LReinforcement> getCached(BlockKey key) {
        if (cache.getIfPresent(key) == null) {
            load(key);
        }

        try {
            return cache.get(key, () -> Optional.empty());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Reinforcement getReinforcement(BlockKey key) throws ExecutionException {
        if (!isReinforced(key)) {
            throw new ReinforcementNotFoundException(key);
        } else {
            LReinforcement reinforcement = cache.getIfPresent(key).get();
            assert reinforcement.getState() != ReinforcementState.DELETE;
            return cache.getIfPresent(key).get();
        }
    }

    public Reinforcement createReinforcement(UUID creator, long creationTime, long hardeningTime, int strength, int maxStrength, boolean isPublic, boolean unbreakable, Long groupID, Collection<BlockKey> blocks, Collection<ItemStack> items) throws ExecutionException {
        for (BlockKey key : blocks) {
            if (isReinforced(key)) {
                throw new AlreadyReinforcedException(key);
            }
        }
        Map<BlockKey, Optional<LReinforcement>> map = new HashMap<>();

        LReinforcement reinf = new LReinforcement(creator, creationTime, hardeningTime, strength, maxStrength, isPublic, unbreakable, groupID, new HashSet<>(blocks), new LinkedList<>(items), this);

        for (BlockKey key : blocks) {
            map.put(key, Optional.of(reinf));
        }

        cache.putAll(map);
        return reinf;
    }

    //    private Optional<LReinforcement> load(BlockKey key) {
    private void load(BlockKey key) {
        //return
        Amethyst.DBI().submit((conf) -> {
            DSLContext ctx = DSL.using(conf);
            Optional<Long> id = ctx.select(AMETHYST_REINFORCED_BLOCK.REINFID)
                    .from(AMETHYST_REINFORCED_BLOCK)
                    .where(AMETHYST_REINFORCED_BLOCK.WORLD_UUID.eq(key.getUuid()))
                    .and(AMETHYST_REINFORCED_BLOCK.X.eq(key.getX()))
                    .and(AMETHYST_REINFORCED_BLOCK.Y.eq(key.getY()))
                    .and(AMETHYST_REINFORCED_BLOCK.Z.eq(key.getZ()))
                    .fetchOptional(Record1<Long>::value1);
            if (id.isPresent()) {
                AmethystReinforcementDataRecord rec = ctx.selectFrom(AMETHYST_REINFORCEMENT_DATA)
                        .where(AMETHYST_REINFORCEMENT_DATA.REINFID.eq(id.get()))
                        .fetchOne();
                List<AmethystReinforcedBlockRecord> blocks = ctx.selectFrom(AMETHYST_REINFORCED_BLOCK)
                        .where(AMETHYST_REINFORCED_BLOCK.REINFID.eq(id.get()))
                        .fetch();

                List<AmethystReinforcementItemsRecord> items = ctx.selectFrom(AMETHYST_REINFORCEMENT_ITEMS)
                        .where(AMETHYST_REINFORCEMENT_ITEMS.REINFID.eq(id.get()))
                        .fetch();
                LReinforcement reinf = new LReinforcement(rec, blocks, items, this);

                reinf.setState(ReinforcementState.CLEAN);
                for (BlockKey reinfKey : reinf.getBlocks()) {
                    cache.put(reinfKey, Optional.of(reinf));
                }
                LinkedList<BlockKey> remove = new LinkedList<>();
                for (BlockKey reinfKey : reinf.getBlocks()) {
                    if (!Amethyst.getInstance().getConf().isReinforcible(reinfKey.getLocation().getBlock().getType())) {
                        LOG.warning("Found illegal reinforcement on " + reinf.getId() + " @" + reinfKey);
                        remove.add(reinfKey);
                    }
                }

                if (!remove.isEmpty()) {
                    for (BlockKey reinfKey : remove) {
                        reinf.removeBlock(reinfKey);
                    }
                }

            } else {
//                return Optional.empty();
            }
        });
    }

    public void put(BlockKey block, LReinforcement lReinforcement) {
        cache.put(block, Optional.of(lReinforcement));
    }

    public void remove(BlockKey block, LReinforcement lReinforcement) {
        cache.put(block, Optional.empty());
    }
}
