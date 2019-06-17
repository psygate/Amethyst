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

package com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.proxy;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

/**
 * Created by psygate (https://github.com/psygate) on 13.03.2016.
 */
public class ProxyManager {
    private static ProxyManager instance = null;

    private Map<Material, BlockReinforcementProxy> proxymap = new HashMap<>();

    private ProxyManager() {
        DoorProxy proxy = new DoorProxy();
        proxy.getTypes().forEach(type -> proxymap.put(type, proxy));
        AttachedProxy tproxy = new AttachedProxy();
        tproxy.getTypes().forEach(type -> proxymap.put(type, tproxy));
        DownProxy dp = new DownProxy();
        dp.getTypes().forEach(type -> proxymap.put(type, dp));
        DoubleBlockProxy cp = new DoubleBlockProxy();
        cp.getTypes().forEach(type -> proxymap.put(type, cp));

        SingleBlockProxy sbp = new SingleBlockProxy();

        for (Material material : Material.values()) {
            proxymap.putIfAbsent(material, sbp);
        }
    }

    public static ProxyManager getInstance() {
        if (instance == null) {
            instance = new ProxyManager();
        }

        return instance;
    }

    public Collection<Block> map(Block block) {
        return proxymap.get(block.getType()).map(block);
    }
}
