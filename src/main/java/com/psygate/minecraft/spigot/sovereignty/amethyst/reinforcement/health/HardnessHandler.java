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


import com.psygate.collections.Pair;
import org.bukkit.Material;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.Material.*;

/**
 * Created by psygate on 30.04.2016.
 */
class HardnessHandler {
    private final static Set<String> skip = new HashSet<>(
            Arrays.asList(
                    "Barrier",
                    "Bedrock",
                    "Command Block",
                    "Air",
                    "Water",
                    "Lava",
                    "Portal",
                    "End Portal",
                    "Player Head",
                    "Monster Head",
                    "Block,Hardness,Tool,Hand,Wooden,Stone,Iron,Diamond,Golden,Shears,Sword"
            )
    );

    private final static Map<Material, Material[]> expands = new HashMap<>(Arrays.stream(
            new Material[][]{
                    new Material[]{RAILS, ACTIVATOR_RAIL, DETECTOR_RAIL, POWERED_RAIL},
                    new Material[]{WOODEN_DOOR, ACACIA_DOOR, DARK_OAK_DOOR, SPRUCE_DOOR, JUNGLE_DOOR, BIRCH_DOOR, WOOD_DOOR},
                    new Material[]{FENCE_GATE, ACACIA_FENCE_GATE, BIRCH_FENCE_GATE, DARK_OAK_FENCE_GATE, JUNGLE_FENCE_GATE, SPRUCE_FENCE_GATE},
                    new Material[]{FENCE, ACACIA_FENCE, BIRCH_FENCE, DARK_OAK_FENCE, JUNGLE_FENCE, SPRUCE_FENCE},
                    new Material[]{WOOD_STAIRS, ACACIA_STAIRS, BIRCH_WOOD_STAIRS, DARK_OAK_STAIRS, JUNGLE_WOOD_STAIRS, SPRUCE_WOOD_STAIRS},
                    new Material[]{BED_BLOCK, BED},
                    new Material[]{CAKE, CAKE_BLOCK},
                    new Material[]{WHEAT, CROPS},
                    new Material[]{DAYLIGHT_DETECTOR, DAYLIGHT_DETECTOR_INVERTED},
                    new Material[]{DIODE, DIODE_BLOCK_ON, DIODE_BLOCK_OFF},
                    new Material[]{DIODE_BLOCK_ON, DIODE, DIODE_BLOCK_OFF},
                    new Material[]{FURNACE, BURNING_FURNACE},
                    new Material[]{REDSTONE_ORE, GLOWING_REDSTONE_ORE},
                    new Material[]{LEAVES, LEAVES_2},
                    new Material[]{LOG, LOG_2},
                    new Material[]{LONG_GRASS, DOUBLE_PLANT},
                    new Material[]{STEP, DOUBLE_STEP, DOUBLE_STONE_SLAB2},
                    new Material[]{REDSTONE_COMPARATOR, REDSTONE_COMPARATOR_OFF, REDSTONE_COMPARATOR_ON},
                    new Material[]{REDSTONE_LAMP_ON, REDSTONE_LAMP_OFF},
                    new Material[]{REDSTONE_TORCH_ON, REDSTONE_TORCH_OFF},
                    new Material[]{SIGN, SIGN_POST, WALL_SIGN},
                    new Material[]{NETHER_WARTS, NETHER_STALK},
                    new Material[]{PISTON_BASE, PISTON_STICKY_BASE},
                    new Material[]{SNOW, SNOW_BLOCK},
                    new Material[]{REDSTONE_WIRE, STRING},
                    new Material[]{BANNER, WALL_BANNER},
                    new Material[]{MELON_BLOCK, MELON},
                    new Material[]{BROWN_MUSHROOM, RED_MUSHROOM}
            }
    ).map(v -> new Pair<>(v[0], Arrays.copyOfRange(v, 1, v.length)))
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())));

    private final static HashSet<Material> ignored = new HashSet<>(
            Arrays.asList(
                    ACACIA_DOOR_ITEM,
                    AIR,
                    APPLE,
                    ARMOR_STAND,
                    ARROW,
                    BAKED_POTATO,
                    BARRIER,
                    BEDROCK,
                    BIRCH_DOOR_ITEM,
                    BLAZE_POWDER,
                    BLAZE_ROD,
                    BOAT,
                    BONE,
                    BOOK,
                    BOOK_AND_QUILL,
                    BOW,
                    BOWL,
                    BREAD,
                    BREWING_STAND_ITEM,
                    BUCKET,
                    CARROT_ITEM,
                    CARROT_STICK,
                    CAULDRON_ITEM,
                    CHAINMAIL_BOOTS,
                    CHAINMAIL_CHESTPLATE,
                    CHAINMAIL_HELMET,
                    CHAINMAIL_LEGGINGS,
                    CLAY_BALL,
                    CLAY_BRICK,
                    COAL,
                    COMMAND,
                    COMMAND_MINECART,
                    COMPASS,
                    COOKED_BEEF,
                    COOKED_CHICKEN,
                    COOKED_FISH,
                    COOKED_MUTTON,
                    COOKED_RABBIT,
                    COOKIE,
                    DARK_OAK_DOOR_ITEM,
                    DIAMOND,
                    DIAMOND_AXE,
                    DIAMOND_BARDING,
                    DIAMOND_BOOTS,
                    DIAMOND_CHESTPLATE,
                    DIAMOND_HELMET,
                    DIAMOND_HOE,
                    DIAMOND_LEGGINGS,
                    DIAMOND_PICKAXE,
                    DIAMOND_SPADE,
                    DIAMOND_SWORD,
                    DIODE,
                    EGG,
                    EMERALD,
                    EMPTY_MAP,
                    ENCHANTED_BOOK,
                    ENDER_PEARL,
                    ENDER_PORTAL,
                    ENDER_PORTAL_FRAME,
                    EXP_BOTTLE,
                    EXPLOSIVE_MINECART,
                    EYE_OF_ENDER,
                    FEATHER,
                    FERMENTED_SPIDER_EYE,
                    FIREBALL,
                    FIREWORK,
                    FIREWORK_CHARGE,
                    FISHING_ROD,
                    FLINT,
                    FLINT_AND_STEEL,
                    FLOWER_POT_ITEM,
                    GHAST_TEAR,
                    GLASS_BOTTLE,
                    GLOWSTONE_DUST,
                    GOLD_AXE,
                    GOLD_BARDING,
                    GOLD_BOOTS,
                    GOLD_CHESTPLATE,
                    GOLD_HELMET,
                    GOLD_HOE,
                    GOLD_INGOT,
                    GOLD_LEGGINGS,
                    GOLD_NUGGET,
                    GOLD_PICKAXE,
                    GOLD_PLATE,
                    GOLD_RECORD,
                    GOLD_SPADE,
                    GOLD_SWORD,
                    GOLDEN_APPLE,
                    GOLDEN_CARROT,
                    GREEN_RECORD,
                    GRILLED_PORK,
                    HOPPER_MINECART,
                    HUGE_MUSHROOM_2,
                    INK_SACK,
                    IRON_AXE,
                    IRON_BARDING,
                    IRON_BOOTS,
                    IRON_CHESTPLATE,
                    IRON_DOOR_BLOCK,
                    IRON_HELMET,
                    IRON_HOE,
                    IRON_INGOT,
                    IRON_LEGGINGS,
                    IRON_PICKAXE,
                    IRON_SPADE,
                    IRON_SWORD,
                    ITEM_FRAME,
                    JUNGLE_DOOR_ITEM,
                    LAVA,
                    LAVA_BUCKET,
                    LEASH,
                    LEATHER,
                    LEATHER_BOOTS,
                    LEATHER_CHESTPLATE,
                    LEATHER_HELMET,
                    LEATHER_LEGGINGS,
                    MAGMA_CREAM,
                    MAP,
                    MELON_SEEDS,
                    MILK_BUCKET,
                    MINECART,
                    MONSTER_EGGS,
                    MUSHROOM_SOUP,
                    MUTTON,
                    NAME_TAG,
                    NETHER_BRICK_ITEM,
                    NETHER_STAR,
                    PAINTING,
                    PAPER,
                    PISTON_EXTENSION,
                    PISTON_MOVING_PIECE,
                    POISONOUS_POTATO,
                    PORK,
                    PORTAL,
                    POTATO_ITEM,
                    POTION,
                    POWERED_MINECART,
                    POWERED_RAIL,
                    PRISMARINE_CRYSTALS,
                    PRISMARINE_SHARD,
                    PUMPKIN_PIE,
                    PUMPKIN_SEEDS,
                    QUARTZ,
                    RABBIT,
                    RABBIT_FOOT,
                    RABBIT_HIDE,
                    RABBIT_STEW,
                    RAW_BEEF,
                    RAW_CHICKEN,
                    RAW_FISH,
                    RECORD_10,
                    RECORD_11,
                    RECORD_12,
                    RECORD_3,
                    RECORD_4,
                    RECORD_5,
                    RECORD_6,
                    RECORD_7,
                    RECORD_8,
                    RECORD_9,
                    RED_ROSE,
                    REDSTONE,
                    ROTTEN_FLESH,
                    SADDLE,
                    SEEDS,
                    SHEARS,
                    SKULL_ITEM,
                    SLIME_BALL,
                    SNOW_BALL,
                    SPECKLED_MELON,
                    SPIDER_EYE,
                    SPRUCE_DOOR_ITEM,
                    STANDING_BANNER,
                    STATIONARY_LAVA,
                    STATIONARY_WATER,
                    STICK,
                    STONE_AXE,
                    STONE_HOE,
                    STONE_PICKAXE,
                    STONE_SLAB2,
                    STONE_SPADE,
                    STONE_SWORD,
                    STORAGE_MINECART,
                    SUGAR,
                    SUGAR_CANE_BLOCK,
                    SULPHUR,
                    WATCH,
                    WATER,
                    WATER_BUCKET,
                    WOOD_AXE,
                    WOOD_BUTTON,
                    WOOD_DOOR,
                    WOOD_DOUBLE_STEP,
                    WOOD_HOE,
                    WOOD_PICKAXE,
                    WOOD_SPADE,
                    WOOD_SWORD,
                    WRITTEN_BOOK
            )
    );

    private final Map<Material, MaterialBreakData> breakData = new HashMap<>();

    public HardnessHandler() {
        try (InputStream in = HardnessHandler.class.getResourceAsStream("/break-times.csv")) {
            if (in == null) {
                throw new IllegalStateException("Cannot find break-times.csv");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            byte[] data = new byte[4096];

            for (; ; ) {
                int len = in.read(data);
                if (len == -1) {
                    break;
                }

                out.write(data, 0, len);
            }

            String csvdata = new String(out.toByteArray(), Charset.forName("UTF-8"));
            process(csvdata);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MaterialBreakData get(Material material) {
        if (!breakData.containsKey(material)) {
            throw new IllegalArgumentException("Material not defined: " + material);
        }

        return breakData.get(material);
    }

    private void process(String csvdata) {
        String[] lines = csvdata.replaceAll("\n\r", "\n").replaceAll("\r\n", "\n").replace("\r", "\n").split("\n");
        int linenum = 0;
        for (String line : lines) {
            linenum++;
            if (linenum == 1) {
                continue;
            } else if (skip.stream().filter(v -> line.startsWith(v)).findAny().isPresent()) {
                System.out.println("Skipping: " + line);
                continue;
            }

            try {
                MaterialBreakData data = new MaterialBreakData(line);
                if (expands.containsKey(data.getMaterial())) {
                    System.out.println("Expanding: " + data.getMaterial());
                    for (Material mat : expands.get(data.getMaterial())) {
                        breakData.put(mat, data.copy(mat));
                    }
                }

                breakData.put(data.getMaterial(), data);
            } catch (IllegalArgumentException e) {
                System.err.println("aliases.put(\"" + e.getMessage().replace("No enum constant org.bukkit.Material.", "") + "\", Material..name())");
            } catch (Exception e) {
                System.err.println("Exception at line " + linenum + " (" + line + ")");
                throw e;
            }
        }

        for (Material mat : Arrays.stream(Material.values()).sorted((a, b) -> a.name().compareToIgnoreCase(b.name())).collect(Collectors.toList())) {
            if (ignored.contains(mat)) {
            } else if (!breakData.containsKey(mat)) {
                System.err.println("Not defined: " + mat.name());
            }
        }
    }
}
