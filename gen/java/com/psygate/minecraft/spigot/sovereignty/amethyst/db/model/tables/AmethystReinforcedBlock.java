/**
 * This class is generated by jOOQ
 */
package com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.tables;


import com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.Keys;
import com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.Nucleus;
import com.psygate.minecraft.spigot.sovereignty.amethyst.db.model.tables.records.AmethystReinforcedBlockRecord;
import com.psygate.minecraft.spigot.sovereignty.nucleus.sql.util.UUIDByteConverter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AmethystReinforcedBlock extends TableImpl<AmethystReinforcedBlockRecord> {

	private static final long serialVersionUID = 1105445566;

	/**
	 * The reference instance of <code>nucleus.amethyst_reinforced_block</code>
	 */
	public static final AmethystReinforcedBlock AMETHYST_REINFORCED_BLOCK = new AmethystReinforcedBlock();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<AmethystReinforcedBlockRecord> getRecordType() {
		return AmethystReinforcedBlockRecord.class;
	}

	/**
	 * The column <code>nucleus.amethyst_reinforced_block.reinfid</code>.
	 */
	public final TableField<AmethystReinforcedBlockRecord, Long> REINFID = createField("reinfid", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>nucleus.amethyst_reinforced_block.x</code>.
	 */
	public final TableField<AmethystReinforcedBlockRecord, Integer> X = createField("x", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>nucleus.amethyst_reinforced_block.y</code>.
	 */
	public final TableField<AmethystReinforcedBlockRecord, Integer> Y = createField("y", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>nucleus.amethyst_reinforced_block.z</code>.
	 */
	public final TableField<AmethystReinforcedBlockRecord, Integer> Z = createField("z", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>nucleus.amethyst_reinforced_block.world_uuid</code>.
	 */
	public final TableField<AmethystReinforcedBlockRecord, UUID> WORLD_UUID = createField("world_uuid", org.jooq.impl.SQLDataType.BINARY.length(16).nullable(false), this, "", new UUIDByteConverter());

	/**
	 * Create a <code>nucleus.amethyst_reinforced_block</code> table reference
	 */
	public AmethystReinforcedBlock() {
		this("amethyst_reinforced_block", null);
	}

	/**
	 * Create an aliased <code>nucleus.amethyst_reinforced_block</code> table reference
	 */
	public AmethystReinforcedBlock(String alias) {
		this(alias, AMETHYST_REINFORCED_BLOCK);
	}

	private AmethystReinforcedBlock(String alias, Table<AmethystReinforcedBlockRecord> aliased) {
		this(alias, aliased, null);
	}

	private AmethystReinforcedBlock(String alias, Table<AmethystReinforcedBlockRecord> aliased, Field<?>[] parameters) {
		super(alias, Nucleus.NUCLEUS, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<AmethystReinforcedBlockRecord> getPrimaryKey() {
		return Keys.KEY_AMETHYST_REINFORCED_BLOCK_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<AmethystReinforcedBlockRecord>> getKeys() {
		return Arrays.<UniqueKey<AmethystReinforcedBlockRecord>>asList(Keys.KEY_AMETHYST_REINFORCED_BLOCK_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<AmethystReinforcedBlockRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<AmethystReinforcedBlockRecord, ?>>asList(Keys.AMETHYST_REINFORCED_BLOCK_IBFK_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AmethystReinforcedBlock as(String alias) {
		return new AmethystReinforcedBlock(alias, this);
	}

	/**
	 * Rename this table
	 */
	public AmethystReinforcedBlock rename(String name) {
		return new AmethystReinforcedBlock(name, null);
	}
}