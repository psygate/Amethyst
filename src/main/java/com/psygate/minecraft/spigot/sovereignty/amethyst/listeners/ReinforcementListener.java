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

package com.psygate.minecraft.spigot.sovereignty.amethyst.listeners;

import com.psygate.minecraft.spigot.sovereignty.amethyst.Amethyst;
import com.psygate.minecraft.spigot.sovereignty.amethyst.events.*;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.Reinforcement;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.ReinforcementManager;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.health.HealthManager;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.proxy.ProxyManager;
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.PlayerState;
import com.psygate.minecraft.spigot.sovereignty.amethyst.state.ReinforcementState;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Group;
import com.psygate.minecraft.spigot.sovereignty.ivory.groups.Rank;
import com.psygate.minecraft.spigot.sovereignty.ivory.managment.GroupManager;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by psygate (https://github.com/psygate) on 08.03.2016.
 */
public class ReinforcementListener implements Listener {

    private static Timestamp now(long delta) {
        return new Timestamp(System.currentTimeMillis() + delta);
    }

    private static Timestamp now() {
        return now(0);
    }

//    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//    public void onPlayerResetReinforcementState(PlayerResetReinforcementStateEvent ev) throws ExecutionException {
//        PlayerStateManager.getInstance().invalidate(ev.getPlayer().getUniqueId());
//    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBypassSetState(PlayerSetBypassModeEvent ev) throws ExecutionException {
        PlayerStateManager.getInstance().get(ev.getPlayer()).setBypassing(ev.isBypass());
        Bukkit.getPlayer(ev.getPlayer()).sendMessage(((ev.isBypass()) ? ChatColor.GREEN : ChatColor.DARK_RED) + "Bypass " + ((ev.isBypass()) ? "on" : "off"));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerSetReinforcementState(PlayerSetReinforcementStateEvent ev) {
        PlayerState state = PlayerStateManager.getInstance().get(ev.getPlayer());
        state.setGroupID(ev.getGroupID());
        state.setReinforcementState(ev.getState());
        state.setUseMaterial(ev.getMaterial());
//        PlayerStateManager.getInstance().put(ev.getPlayer(), new PlayerState(ev.getPlayer(), ev.getState(), ev.getGroupID().isPresent() ? OptionalLong.of(ev.getGroupID().get().getGroupId()) : OptionalLong.empty(), ev.getMaterial()));
        if (ev.getState() != ReinforcementState.NONE) {
            Bukkit.getPlayer(ev.getPlayer()).sendMessage(ChatColor.GREEN + "Now reinforcing with " + ev.getMaterial().getMaterial() + " in " + ev.getState() + " mode.");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerSetInfoState(PlayerSetInfoModeEvent ev) {
        PlayerStateManager.getInstance().get(ev.getPlayer()).setInspecting(ev.isInfo());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInspect(PlayerInteractEvent ev) throws ExecutionException {
        if (ev.getAction() == Action.LEFT_CLICK_BLOCK
                && PlayerStateManager.getInstance().get(ev.getPlayer().getUniqueId()).isInspecting()) {
            ev.setCancelled(true);
            Block block = ev.getClickedBlock();
            BlockKey key = new BlockKey(block);
            ev.getPlayer().sendMessage(ChatColor.YELLOW + "Info mode, block break & reinforcement damage cancelled.");
            if (ev.getPlayer().isOp()) {
                if (isReinforcedThrowUnchecked(key)) {
                    Reinforcement reinf = ReinforcementManager.getInstance().getReinforcement(key);

                    ArrayList<String> out = new ArrayList<>(10);

                    out.add("Blocks: " + reinf.getBlocks().toString());
                    out.add("Health: " + reinf.getStrength() + "/" + reinf.getMaxStrength());
                    out.add("Creation: " + new Date(reinf.getCreationTime()) + " by " + PlayerManager.getInstance().toName(reinf.getCreator()));
                    out.add("Group: " + reinf.getGroupID() + " (" + GroupManager.getInstance().getGroup(reinf.getGroupID()).get().getName());
                    out.add("Flags: Public " + reinf.isPublic() + " Unbreakable " + reinf.isUnbreakable());
                    out.add("Items: " + reinf.getItems());

                    ev.getPlayer().sendMessage(out.toArray(new String[out.size()]));
                } else {
                    ev.getPlayer().sendMessage("Unreinforced.");
                }
            } else {
                if (isReinforcedThrowUnchecked(key)) {
                    Reinforcement rf = ReinforcementManager.getInstance().getReinforcement(key);
                    Optional<? extends Group> group = GroupManager.getInstance().getGroup(rf.getGroupID());
                    if (group.map(g -> g.hasMemberWithRankGE(ev.getPlayer().getUniqueId(), Rank.MODERATOR)).orElseGet(() -> Boolean.FALSE)) {
                        ev.getPlayer().sendMessage(ChatColor.YELLOW + "Reinforced " + block.getType()
                                + ": [" + rf.getStrength() + "/" + rf.getMaxStrength()
                                + "] (" + group.map(Group::getName).orElseGet(() -> "Unknown Group") + ")");
                    } else {
                        ev.getPlayer().sendMessage(ChatColor.YELLOW + "Reinforced. [" + rf.getStrength() + "/" + rf.getMaxStrength() + "]");
                    }
                } else {
                    ev.getPlayer().sendMessage("Unreinforced.");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHopperTransfer(InventoryMoveItemEvent ev) throws ExecutionException {
        OptionalLong sourceid = getGroupID(ev.getSource());
        OptionalLong actorid = getGroupID(ev.getInitiator());
        OptionalLong destinationid = getGroupID(ev.getDestination());

        if (destinationid.isPresent()) {
            if (actorid.isPresent()) {
                if (actorid.getAsLong() != destinationid.getAsLong()) {
                    ev.setCancelled(true);
                    return;
                }
            } else {
                ev.setCancelled(true);
            }
        }

        if (sourceid.isPresent()) {
            if (actorid.isPresent()) {
                if (actorid.getAsLong() != sourceid.getAsLong()) {
                    ev.setCancelled(true);
                    return;
                }
            } else {
                ev.setCancelled(true);
            }
        }

    }

    private OptionalLong getGroupID(Inventory source) {
        if (source.getHolder() != null) {
            InventoryHolder holder = source.getHolder();

            if (holder instanceof BlockState) {
                try {
                    BlockKey key = new BlockKey((BlockState) holder);
                    if (ReinforcementManager.getInstance().isReinforced(key)) {
                        Reinforcement rf = ReinforcementManager.getInstance().getReinforcement(key);
                        return OptionalLong.of(rf.getGroupID());
                    }
                } catch (ExecutionException e) {

                }
            }
        }

        return OptionalLong.empty();
    }

    private Optional<Reinforcement> getReinforcementForBlockstate(BlockState holder) throws ExecutionException {
        if (ReinforcementManager.getInstance().isReinforced(new BlockKey(holder))) {
            return Optional.of(ReinforcementManager.getInstance().getReinforcement(new BlockKey(holder)));
        }
        return Optional.empty();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent ev) throws ExecutionException {
        if (PlayerStateManager.getInstance().get(ev.getPlayer().getUniqueId()).isInspecting()) {
            ev.setCancelled(true);
            return;
        }

        BlockKey key = new BlockKey(ev.getBlock());
        if (isReinforcedThrowUnchecked(key)) {
            Reinforcement rf = ReinforcementManager.getInstance().getReinforcement(key);

            PlayerState state = PlayerStateManager.getInstance().get(ev.getPlayer().getUniqueId());
            boolean bypassing = state.isBypassing() && canBypass(key, ev.getPlayer().getUniqueId());
            int damage = (bypassing) ? rf.getStrength() : 1;

//            Bukkit.broadcastMessage("Hardening: " + new Date(rf.getHardeningTime()) + " Creation: " + new Date(rf.getCreationTime()));
//            Bukkit.broadcastMessage("Hardening: " + rf.getHardeningTime() + " Creation: " + rf.getCreationTime());
//

            if (rf.getHardeningTime() > System.currentTimeMillis()) {
                long completed = System.currentTimeMillis() - rf.getCreationTime();
                long full = rf.getHardeningTime() - rf.getCreationTime();
                double scale = completed / full;

                damage = (int) Math.ceil(rf.getMaxStrength() * (1 - scale));
//                Bukkit.broadcastMessage("Damage amp, not matured: " + damage + " Scale: " + scale);
            }

            PlayerDamageReinforcementEvent pdre =
                    new PlayerDamageReinforcementEvent(ev.getBlock(), ev.getPlayer(), damage, rf.getStrength(), rf.getMaxStrength(), bypassing, rf.getGroupID());

            if (!pdre.call()) {
                if (pdre.getDamage() >= pdre.getHealth()) {
                    if (bypassing && pdre.getHealth() > (pdre.getMaxHealth() / 4 * 3)) {
                        Collection<ItemStack> drop = rf.getItems();
                        drop.forEach(stack -> ev.getPlayer().getLocation().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), stack));
                    }
                    rf.delete();
                } else {
                    rf.setStrength(rf.getStrength() - pdre.getDamage());
                }
                ev.setCancelled(pdre.getDamage() < pdre.getHealth());
            } else {
                ev.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent ev) throws ExecutionException, InterruptedException {
        //HOTFIX, do this properly.
        Block block = ev.getBlock();
        Player player = ev.getPlayer();
        BlockKey key = new BlockKey(block);

        if ((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)) {
            for (BlockFace face : new BlockFace[]{BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH}) {
                Block rel = block.getRelative(face);
                BlockKey relkey = new BlockKey(rel);

//                if (player.isOp()) {
//                    player.sendMessage("Checking: " + rel.getType() + " Face: " + face);
//                }

                if (rel.getType() == block.getType() && ReinforcementManager.getInstance().isReinforced(relkey)) {
                    Reinforcement reinf = ReinforcementManager.getInstance().getReinforcement(relkey);
                    reinf.addBlock(key);
                    player.sendMessage(ChatColor.YELLOW + "Expanded reinforcement.");
                    return;
                }
            }
        }
        //
        PlayerState state = PlayerStateManager.getInstance().get(ev.getPlayer().getUniqueId());
        if (state.getReinforcementState() == ReinforcementState.PLACE || state.getReinforcementState() == ReinforcementState.LOCK) {
            checkAndCreateReinforcement(ev.getPlayer(), ev.getBlockPlaced(), state);
        }
    }

    private void checkAndCreateReinforcement(Player player, Block block, PlayerState state) throws ExecutionException, InterruptedException {
        BlockKey blockKey = new BlockKey(block);
        if (Amethyst.getInstance().getConf().isReinforcible(block.getType())) {
            if (ReinforcementManager.getInstance().isReinforced(blockKey)) {
                if (!canBypass(blockKey, player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + block.getType().name() + " is already reinforced.");
                } else {
                    Reinforcement oldReinforcement = ReinforcementManager.getInstance().getReinforcement(blockKey);
                    Collection<ItemStack> costs = new LinkedList<>();
                    costs.add(new ItemStack(state.getUseMaterial().getMaterial(), state.getUseMaterial().getAmount()));
                    List<Block> ofblocks = new ArrayList<>(oldReinforcement.getBlocks().stream().map(v -> v.getLocation().getBlock()).collect(Collectors.toList()));
                    PlayerCreateReinforcementEvent ev = new PlayerCreateReinforcementEvent(
                            player,
                            block,
                            ofblocks,
                            state.getReinforcementState(),
                            costs,
                            state.getUseMaterial().getHardeningTime(),
                            state.getUseMaterial().getBreaks(),
                            state.getGroupID().getAsLong()
                    );

                    if (!ev.call()) {
                        if (removeItem(player, ev.getCosts())) {
                            if (oldReinforcement.getStrength() > (oldReinforcement.getMaxStrength() / 4 * 3)) {
                                Collection<ItemStack> drop = oldReinforcement.getItems();
                                drop.forEach(stack -> ev.getPlayer().getLocation().getWorld().dropItemNaturally(ev.getPlayer().getLocation(), stack));
                                oldReinforcement.delete();
                            }
                            createReinforcement(blockKey, block, player.getUniqueId(), state, ofblocks, ev.getTarget().getType());
                            player.sendMessage(ChatColor.GREEN + "Reinforcement replaced.");
                        } else {
                            player.sendMessage(ChatColor.RED + state.getUseMaterial().getMaterial().name() + " depleted. Leaving reinforcement mode.");
                            state.setReinforcementState(ReinforcementState.NONE);
                        }
                    }

                }
            } else {
                Collection<Block> reinf = ProxyManager.getInstance().map(block);
                for (Block check : reinf) {
                    if (!Amethyst.getInstance().getConf().isReinforcible(check.getType())) {
                        player.sendMessage(ChatColor.RED + block.getType().name() + " cannot be reinforced, multiblock reinforcement would include a forbidden block (" + check.getType() + ").");
                        return;
                    }
                }
                Collection<ItemStack> costs = new LinkedList<>();

                costs.add(new ItemStack(state.getUseMaterial().getMaterial(), state.getUseMaterial().getAmount()));

                if (anyReinforced(reinf)) {
                    player.sendMessage(ChatColor.RED + block.getType().name() + " cannot be reinforced, a required additional block is already reinforced.");
                } else {
                    PlayerCreateReinforcementEvent ev = new PlayerCreateReinforcementEvent(
                            player,
                            block,
                            reinf,
                            state.getReinforcementState(),
                            costs,
                            state.getUseMaterial().getHardeningTime(),
                            state.getUseMaterial().getBreaks(),
                            state.getGroupID().getAsLong()
                    );

                    if (!ev.call()) {
                        if (removeItem(player, ev.getCosts())) {
//                            BlockKey key = new BlockKey(block);
                            createReinforcement(blockKey, block, player.getUniqueId(), state, reinf, ev.getTarget().getType());
                        } else {
                            player.sendMessage(ChatColor.RED + state.getUseMaterial().getMaterial().name() + " depleted. Leaving reinforcement mode.");
                            state.setReinforcementState(ReinforcementState.NONE);
                        }
                    }
                }
            }
        } else {
            player.sendMessage(ChatColor.RED + block.getType().name() + " cannot be reinforced.");
        }
    }

    private boolean anyReinforced(Collection<Block> reinf) throws ExecutionException {
        for (Block block : reinf) {
            if (ReinforcementManager.getInstance().isReinforced(new BlockKey(block))) {
                return true;
            }
        }

        return false;
    }

    private void createReinforcement(BlockKey key, Block block, UUID uniqueId, PlayerState state, Collection<Block> reinf, Material adjustmentType) throws ExecutionException, InterruptedException {
        HashSet<BlockKey> blocks = new HashSet<>();
        reinf.forEach(b -> blocks.add(new BlockKey(b)));

        //state.getUseMaterial().getBreaks()
        int health = HealthManager.getInstance().getHealthAdjusted(adjustmentType, state.getUseMaterial().getBreaks());

        ReinforcementManager.getInstance().createReinforcement(
                Arrays.asList(state.getUseMaterial().asItemStack()),
                blocks,
                state.getGroupID().getAsLong(),
                false,
                false,
                health,
                health,
                state.getUseMaterial().getHardeningTime() + System.currentTimeMillis(),
                System.currentTimeMillis(),
                uniqueId
        );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBlockPunch(PlayerInteractEvent ev) throws Exception {
        PlayerState state = PlayerStateManager.getInstance().get(ev.getPlayer().getUniqueId());
        if ((state.getReinforcementState() == ReinforcementState.PUNCH || state.getReinforcementState() == ReinforcementState.LOCK)
                && ev.getPlayer().getItemInHand() != null && ev.getPlayer().getItemInHand().getType() == state.getUseMaterial().getMaterial()) {
            checkAndCreateReinforcement(ev.getPlayer(), ev.getClickedBlock(), state);
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDoorOpenClose(PlayerInteractEvent ev) throws ExecutionException {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK && Amethyst.getInstance().getConf().getSettings().isContainer(ev.getClickedBlock().getType())) {
            BlockKey key = new BlockKey(ev.getClickedBlock());
            if (ReinforcementManager.getInstance().isReinforced(key)) {
                Reinforcement rf = ReinforcementManager.getInstance().getReinforcement(key);
                Group group = GroupManager.getInstance().getGroup(rf.getGroupID()).get();

                try {
                    if (ev.getPlayer().isOp() && PlayerStateManager.getInstance().get(ev.getPlayer().getUniqueId()).isBypassing() && !group.hasMember(ev.getPlayer().getUniqueId())) {
                        ev.getPlayer().sendMessage(ChatColor.YELLOW + "Bypassing lock.");
                    } else {
                        if (!group.hasMember(ev.getPlayer().getUniqueId())) {
                            ev.setCancelled(true);
                            if (ev.getPlayer().isOp()) {
                                ev.getPlayer().sendMessage(ChatColor.RED + "Locked with " + group.getName() + "[" + group.getGroupID() + "]");
                            } else {
                                ev.getPlayer().sendMessage(ChatColor.RED + ev.getClickedBlock().getType().name() + " is locked.");
                            }
                        }

                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onRedStoneEvent(BlockRedstoneEvent ev) throws ExecutionException {
        BlockKey key = new BlockKey(ev.getBlock());
        if (ev.getNewCurrent() != ev.getOldCurrent() && ReinforcementManager.getInstance().isReinforced(key)) {
            Long groupID = ReinforcementManager.getInstance().getReinforcement(key).getGroupID();
            boolean allow = ev.getBlock().getWorld().getPlayers().stream()
                    .filter(p -> {
                        try {
                            return GroupManager.getInstance().getGroup(groupID).get().hasMember(p.getUniqueId());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(Player::getLocation)
                    .filter(location -> location.getWorld().equals(ev.getBlock().getWorld()))   // Make sure we don't crash.
                    .mapToDouble(loc -> loc.distanceSquared(ev.getBlock().getLocation()))
                    .anyMatch(p -> p <= Amethyst.getInstance().getConf().getSettings().getRedstoneDistance() * Amethyst.getInstance().getConf().getSettings().getRedstoneDistance());
            if (!allow) {
                ev.setNewCurrent(ev.getOldCurrent());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onExplosion(BlockExplodeEvent ev) throws ExecutionException {
        Iterator<Block> damaged = ev.blockList().iterator();

        while (damaged.hasNext()) {
            if (ReinforcementManager.getInstance().isReinforced(new BlockKey(damaged.next()))) {
                damaged.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent ev) throws ExecutionException {
        if (ReinforcementManager.getInstance().isReinforced(new BlockKey(ev.getBlock()))) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent ev) throws ExecutionException {
        if (ReinforcementManager.getInstance().isReinforced(new BlockKey(ev.getToBlock()))) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityBlockForm(EntityBlockFormEvent ev) throws ExecutionException {
        if (ReinforcementManager.getInstance().isReinforced(new BlockKey(ev.getBlock()))) {
            ev.setCancelled(true);
        }
    }

//    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
//    public void onBlockPhysics(BlockPhysicsEvent ev) throws ExecutionException {
//        if (ev.getBlock().getType() != ev.getChangedType() &&
//                ReinforcementManager.getInstance().isReinforced(new BlockKey(ev.getBlock()))) {
//            System.out.println("No physics for: " + ev.getBlock().getType() + ", " + ev.getBlock().getLocation());
//            ev.get
//            System.out.println(ev.getBlock().getType() + " to " + ev.getChangedType());
//            ev.setCancelled(true);
//        }
//    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent ev) throws ExecutionException {
        if (ReinforcementManager.getInstance().isReinforced(new BlockKey(ev.getBlock()))) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDoorBreak(EntityBreakDoorEvent ev) throws ExecutionException {
        if (ReinforcementManager.getInstance().isReinforced(new BlockKey(ev.getBlock()))) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityChange(EntityChangeBlockEvent ev) throws ExecutionException {
        if (ReinforcementManager.getInstance().isReinforced(new BlockKey(ev.getBlock()))) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent ev) throws ExecutionException {
        Iterator<Block> damaged = ev.blockList().iterator();

        while (damaged.hasNext()) {
            if (ReinforcementManager.getInstance().isReinforced(new BlockKey(damaged.next()))) {
                damaged.remove();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent ev) throws ExecutionException {
        if (ev.getBlocks().stream().filter(b -> isReinforcedThrowUnchecked(new BlockKey(b))).findAny().isPresent()) {
            ev.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent ev) throws ExecutionException {
        if (ev.getBlocks().stream().filter(b -> isReinforcedThrowUnchecked(new BlockKey(b))).findAny().isPresent()) {
            ev.setCancelled(true);
        }
    }


    private boolean removeItem(Player player, Collection<ItemStack> materials) {
        PlayerInventory inv = player.getInventory();
        for (ItemStack material : materials) {
            int count = 0;

            for (ItemStack stack : inv) {
                if (stack != null && stack.getType() == material.getType() && stack.getData().getData() == 0) {
                    count += stack.getAmount();
                }
                if (count >= material.getAmount()) {
                    break;
                }
            }

            if (count < material.getAmount()) {
                return false;
            }
        }

        for (ItemStack material : materials) {
            int cpy = material.getAmount();


            ListIterator<ItemStack> it = inv.iterator();
            while (it.hasNext() && cpy > 0) {
                ItemStack stack = it.next();
                if (stack != null && stack.getType() == material.getType() && stack.getData().getData() == 0) {
                    if (stack.getAmount() < cpy) {
                        it.set(new ItemStack(Material.AIR));
                        cpy -= stack.getAmount();
                    } else if (stack.getAmount() == cpy) {
                        it.set(new ItemStack(Material.AIR));
                        cpy = 0;
                        break;
                    } else if (stack.getAmount() > cpy) {
                        stack.setAmount(stack.getAmount() - cpy);
                        it.set(stack);
                        cpy = 0;
                        break;
                    }
                }
            }
        }
        return true;
    }

    private boolean isReinforcedThrowUnchecked(BlockKey key) {
        try {
            return ReinforcementManager.getInstance().isReinforced(key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean canBypass(BlockKey key, UUID player) throws ExecutionException {
        return GroupManager.getInstance().getGroup(ReinforcementManager.getInstance().getReinforcement(key).getGroupID())
                .get().hasMemberWithRankGE(player, Rank.MODERATOR) || Bukkit.getPlayer(player).isOp();
    }

    public void flush() {
        PlayerStateManager.getInstance().invalidateAll();
    }

    public PlayerState getPlayerState(UUID uuid) throws ExecutionException {
        return PlayerStateManager.getInstance().get(uuid);
    }


}
