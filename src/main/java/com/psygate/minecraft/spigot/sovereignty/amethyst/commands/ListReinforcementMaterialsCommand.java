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

import com.psygate.minecraft.spigot.sovereignty.amethyst.Amethyst;
import com.psygate.minecraft.spigot.sovereignty.amethyst.configuration.ReinforcementMaterial;
import com.psygate.minecraft.spigot.sovereignty.nucleus.commands.util.NucleusCommand;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.util.TimeUtil;
import com.psygate.text.Alignment;
import com.psygate.text.TextTable;
import com.psygate.text.TextTableSettings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * Created by psygate (https://github.com/psygate) on 09.03.2016.
 */
public class ListReinforcementMaterialsCommand extends NucleusCommand {
    private Optional<String[]> output = Optional.empty();

    public ListReinforcementMaterialsCommand() {
        super(0, 0);
    }

    @Override
    protected void subOnCommand(CommandSender commandSender, Command command, String s, String[] strings) throws Exception {
        if (!output.isPresent()) {
            renderOutput();
        }

        commandSender.sendMessage(output.get());
    }

    private void renderOutput() {
        TextTable tt = TextTableSettings.newBuilder()
                .setAlignment(Alignment.CENTER)
                .setColumnSeperator('|')
                .setExpandHeaders(true)
                .setPad(' ')
                .setRenderBorder(true)
                .setRenderColumnNames(true)
                .setRenderTextSeperator(true)
                .addColumn("Material")
                .addColumn("Breaks")
//                .addColumn("fireproof")
//                .addColumn("firesup")
                .addColumn("hard/time")
                .build();

        for (ReinforcementMaterial mat : Amethyst.getInstance().getConf().getReinforcementMaterials().values()) {
            tt.add(mat.getMaterial().name())
                    .add(Integer.toString(mat.getBreaks()))
                    .add(Boolean.toString(mat.isFireProof()))
                    .add(Boolean.toString(mat.isFireSupperssion()))
                    .add(TimeUtil.prettyfyTime(mat.getHardeningTime()));
        }

        output = Optional.of(tt.render());
    }


    @Override
    protected String[] getName() {
        return new String[]{"listreinforcementmaterials"};
    }
}
