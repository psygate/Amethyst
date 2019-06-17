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

package com.psygate.minecraft.spigot.sovereignty.amethyst;

import com.psygate.minecraft.spigot.sovereignty.amethyst.configuration.Configuration;
import com.psygate.minecraft.spigot.sovereignty.amethyst.listeners.ReinforcementListener;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.Reinforcement;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.ReinforcementManager;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.health.HealthManager;
import com.psygate.minecraft.spigot.sovereignty.amethyst.reinforcement.proxy.ProxyManager;
import com.psygate.minecraft.spigot.sovereignty.gaia.events.PlantGrowthEvent;
import com.psygate.minecraft.spigot.sovereignty.nucleus.Nucleus;
import com.psygate.minecraft.spigot.sovereignty.nucleus.managment.NucleusPlugin;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.DatabaseInterface;
import com.psygate.minecraft.spigot.sovereignty.nucleus.util.mc.BlockKey;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.*;

/**
 * Created by psygate (https://github.com/psygate) on 08.03.2016.
 */
public class Amethyst extends JavaPlugin implements NucleusPlugin {
    private final static Logger LOG = Logger.getLogger(Amethyst.class.getName());
    private static Amethyst instance;
    private DatabaseInterface dbi;
    private Configuration conf;
    private ReinforcementListener reinforcementListener = new ReinforcementListener();

    static {
        LOG.setUseParentHandlers(false);
        LOG.setLevel(Level.ALL);
        List<Handler> handlers = Arrays.asList(LOG.getHandlers());

        if (handlers.stream().noneMatch(h -> h instanceof FileHandler)) {
            try {
                File logdir = new File("logs/nucleus_logs/amethyst/");
                if (!logdir.exists()) {
                    logdir.mkdirs();
                }
                FileHandler fh = new FileHandler(
                        "logs/nucleus_logs/amethyst/amethyst.%u.%g.log",
                        8 * 1024 * 1024,
                        12,
                        true
                );
                fh.setLevel(Level.ALL);
                fh.setEncoding("UTF-8");
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                LOG.addHandler(fh);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Logger getLogger(String name) {
        Logger log = Logger.getLogger(name);
        log.setParent(LOG);
        log.setUseParentHandlers(true);
        log.setLevel(Level.ALL);
        return log;
    }

    @Override
    public void onEnable() {
        try {
            instance = this;
            Nucleus.getInstance().register(this);
            saveDefaultConfig();
            conf = new Configuration(getConfig());
            ProxyManager.getInstance();
            HealthManager.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Override
    public void onDisable() {
        ReinforcementManager.getInstance().flush();
        reinforcementListener.flush();
    }

    public static Amethyst getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Plugin is not initialized.");
        }
        return instance;
    }

    public ReinforcementListener getReinforcementListener() {
        return reinforcementListener;
    }

    public Configuration getConf() {
        return conf;
    }

    public static DatabaseInterface DBI() {
        return getInstance().dbi;
    }

    @Override
    public int getWantedDBVersion() {
        return 0;
    }

    @Override
    public void fail() {
        LOG.severe("Amethyst unable to load.");
        Bukkit.shutdown();
    }

    @Override
    public void setLogger(Logger logger) {

    }

    @Override
    public Logger getSubLogger(String logname) {
        return null;
    }

    @Override
    public Logger getPluginLogger() {
        return null;
    }

    @Override
    public List<Listener> getListeners() {
        if (getServer().getPluginManager().getPlugin("gaia") != null) {
            Listener gaialistener = new Listener() {
                @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
                public void onGaiaGrowth(PlantGrowthEvent ev) throws ExecutionException {
                    BlockKey sourcekey = new BlockKey(ev.getSource());
                    if (ReinforcementManager.getInstance().isReinforced(sourcekey)) {
                        Reinforcement rf = ReinforcementManager.getInstance().getReinforcement(sourcekey);
                        for (BlockState state : ev.getGrow()) {
                            BlockKey statekey = new BlockKey(state);
                            if (getConf().isReinforcible(state.getType()) && !ReinforcementManager.getInstance().isReinforced(statekey)) {
                                rf.addBlock(statekey);
                            }
                        }
                    }
                }
            };
            return Arrays.asList(reinforcementListener, gaialistener);

        } else {
            return Arrays.asList(reinforcementListener);
        }
    }

    @Override
    public void setDatabaseInterface(DatabaseInterface databaseInterface) {
        this.dbi = databaseInterface;
    }
}
