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

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by psygate on 30.04.2016.
 */
public class MaterialBreakData {
    private static final Map<String, String> aliases = new HashMap<>();

    static {
        aliases.put("BLOCK_OF_COAL", Material.COAL_BLOCK.name());
        aliases.put("BLOCK_OF_DIAMOND", Material.DIAMOND_BLOCK.name());
        aliases.put("BLOCK_OF_GOLD", Material.GOLD_BLOCK.name());
        aliases.put("BLOCK_OF_IRON", Material.IRON_BLOCK.name());
        aliases.put("BLOCK_OF_EMERALD", Material.EMERALD_BLOCK.name());
        aliases.put("BLOCK_OF_REDSTONE", Material.REDSTONE_BLOCK.name());
        aliases.put("MONSTER_SPAWNER", Material.MOB_SPAWNER.name());
        aliases.put("IRON_BARS", Material.IRON_FENCE.name());
        aliases.put("COBWEB", Material.WEB.name());
        aliases.put("END_STONE", Material.ENDER_STONE.name());
        aliases.put("LAPIS_LAZULI_BLOCK", Material.LAPIS_BLOCK.name());
        aliases.put("LAPIS_LAZULI_ORE", Material.LAPIS_ORE.name());
        aliases.put("NETHER_QUARTZ_ORE", Material.QUARTZ_ORE.name());
        aliases.put("WOODEN_TRAPDOOR", Material.TRAP_DOOR.name());
        aliases.put("CRAFTING_TABLE", Material.WORKBENCH.name());
        aliases.put("BRICKS", Material.BRICK.name());
        aliases.put("COBBLESTONE_WALL", Material.COBBLE_WALL.name());
        aliases.put("MOSS_STONE", Material.MOSSY_COBBLESTONE.name());
        aliases.put("NETHER_BRICK_FENCE", Material.NETHER_FENCE.name());
        aliases.put("STONE_SLAB", Material.STEP.name());
        aliases.put("WOOD_PLANKS", Material.WOOD.name());
        aliases.put("WOOD", Material.LOG.name());
        aliases.put("WOODEN_SLABS", Material.WOOD_STEP.name());
        aliases.put("WOODEN_STAIRS", Material.WOOD_STAIRS.name());
        aliases.put("ANDESITE", Material.STONE.name());
        aliases.put("DARK_PRISMARINE", Material.PRISMARINE.name());
        aliases.put("MELON", Material.MELON_BLOCK.name());
        aliases.put("DIORITE", Material.STONE.name());
        aliases.put("GRANITE", Material.STONE.name());
        aliases.put("PRISMARINE_BRICKS", Material.PRISMARINE.name());
        aliases.put("STONE_BRICK", Material.SMOOTH_BRICK.name());
        aliases.put("STONE_BRICK_STAIRS", Material.SMOOTH_STAIRS.name());
        aliases.put("HARDENED_CLAY", Material.HARD_CLAY.name());
        aliases.put("JACK_O'LANTERN", Material.JACK_O_LANTERN.name());
        aliases.put("MOB_HEAD", Material.SKULL.name());
        aliases.put("BLOCK_OF_QUARTZ", Material.QUARTZ_BLOCK.name());
        aliases.put("FARMLAND", Material.SOIL.name());
        aliases.put("GRASS_BLOCK", Material.GRASS.name());
        aliases.put("GRASS", Material.LONG_GRASS.name());
        aliases.put("MYCELIUM", Material.MYCEL.name());
        aliases.put("LILY_PAD", Material.WATER_LILY.name());
        aliases.put("WET_SPONGE", Material.SPONGE.name());
        aliases.put("BUTTON", Material.STONE_BUTTON.name());
        aliases.put("COARSE_DIRT", Material.DIRT.name());
        aliases.put("HAY_BALE", Material.HAY_BLOCK.name());
        aliases.put("PISTON", Material.PISTON_BASE.name());
        aliases.put("STONE_PRESSURE_PLATE", Material.STONE_PLATE.name());
        aliases.put("WEIGHTED_PRESSURE_PLATE", Material.IRON_PLATE.name());
        aliases.put("WOODEN_PRESSURE_PLATE", Material.WOOD_PLATE.name());
        aliases.put("GLASS_PANE", Material.THIN_GLASS.name());
        aliases.put("REDSTONE_LAMP", Material.REDSTONE_LAMP_ON.name());
        aliases.put("DAYLIGHT_SENSOR", Material.DAYLIGHT_DETECTOR.name());
        aliases.put("HUGE_MUSHROOMS", Material.HUGE_MUSHROOM_1.name());
        aliases.put("VINES", Material.VINE.name());
        aliases.put("SNOW_(LAYER)", Material.SNOW.name());
        aliases.put("CARROTS", Material.CARROT.name());
        aliases.put("FLOWERS", Material.YELLOW_FLOWER.name());
        aliases.put("MUSHROOMS", Material.BROWN_MUSHROOM.name());
        aliases.put("NETHER_WART", Material.NETHER_WARTS.name());
        aliases.put("POTATOES", Material.POTATO.name());
        aliases.put("REDSTONE_REPEATER", Material.DIODE_BLOCK_ON.name());
        aliases.put("REDSTONE_TORCH", Material.REDSTONE_TORCH_ON.name());
        aliases.put("SAPLINGS", Material.SAPLING.name());
        aliases.put("BED", Material.BED_BLOCK.name());
    }

    private Material material;
    private double hardness;
    private double hand;
    private double wood;
    private double stone;
    private double gold;
    private double iron;
    private double diamond;
    private double shears;
    private double sword;
    private double minimal;

    public MaterialBreakData(String csv) {
        String[] csvdata = csv.split(",");

        material = parseMaterial(csvdata[0]);
        hardness = tryParse(csvdata[1]);
        hand = tryParse(csvdata[3]);
        wood = tryParse(csvdata[4]);
        stone = tryParse(csvdata[5]);
        iron = tryParse(csvdata[6]);
        diamond = tryParse(csvdata[7]);
        gold = tryParse(csvdata[8]);
        shears = tryParse(csvdata[9]);
        sword = tryParse(csvdata[10]);
        minimal = selectMinimum(hand, wood, stone, iron, diamond, gold, shears, sword);
    }

    private static double selectMinimum(double... values) {
        double val = values[0];
        for (int i = 1; i < values.length; i++) {
            val = Math.min(val, values[i]);
        }

        return val;
    }

    public MaterialBreakData(Material mat, MaterialBreakData materialBreakData) {
        this.material = mat;
        this.hardness = materialBreakData.hardness;
        hand = materialBreakData.hardness;
        wood = materialBreakData.wood;
        stone = materialBreakData.stone;
        iron = materialBreakData.iron;
        diamond = materialBreakData.diamond;
        gold = materialBreakData.gold;
        shears = materialBreakData.shears;
        sword = materialBreakData.sword;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public double getHardness() {
        return hardness;
    }

    public void setHardness(double hardness) {
        this.hardness = hardness;
    }

    public double getHand() {
        return hand;
    }

    public void setHand(double hand) {
        this.hand = hand;
    }

    public double getWood() {
        return wood;
    }

    public void setWood(double wood) {
        this.wood = wood;
    }

    public double getStone() {
        return stone;
    }

    public void setStone(double stone) {
        this.stone = stone;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public double getIron() {
        return iron;
    }

    public void setIron(double iron) {
        this.iron = iron;
    }

    public double getDiamond() {
        return diamond;
    }

    public void setDiamond(double diamond) {
        this.diamond = diamond;
    }

    public double getShears() {
        return shears;
    }

    public void setShears(double shears) {
        this.shears = shears;
    }

    public double getSword() {
        return sword;
    }

    public void setSword(double sword) {
        this.sword = sword;
    }

    public double getMinimal() {
        return minimal;
    }

    public void setMinimal(double minimal) {
        this.minimal = minimal;
    }

    private static Material parseMaterial(String mat) {
        String name = mat.toUpperCase().trim().replaceAll("[ ]+", "_");
        String normalizedName = aliases.getOrDefault(name, name);
        return Material.valueOf(normalizedName);
    }

    private double tryParse(String value) {
        if (value.trim().toLowerCase().equals("inf")) {
            return Double.POSITIVE_INFINITY;
        } else if (value.trim().toLowerCase().equals("-")) {
            throw new IllegalArgumentException("Unable to parse: " + value);
        }

        return Double.parseDouble(value);
    }

    private double tryParse(String value, double defaultValue) {
        if (value.trim().toLowerCase().equals("inf")) {
            return Double.POSITIVE_INFINITY;
        } else if (value.trim().toLowerCase().equals("-")) {
            return defaultValue;
        }

        return Double.parseDouble(value);
    }

    public MaterialBreakData copy(Material mat) {
        return new MaterialBreakData(mat, this);
    }
}
