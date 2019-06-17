-- SCRIPT INFORMATION --
-- Types: mysql mariadb
-- Version: 1
-- Upgrades: 0
-- SCRIPT INFORMATION --

START TRANSACTION;
SET foreign_key_checks = 0;

DROP TABLE IF EXISTS amethyst_reinforcement_data;
DROP TABLE IF EXISTS amethyst_reinforced_block;
DROP TABLE IF EXISTS amethyst_default_group;
DROP TABLE IF EXISTS amethyst_reinforcement_items;

CREATE TABLE amethyst_reinforcement_data (
    reinfid         BIGINT                  NOT NULL            AUTO_INCREMENT,
    creation_time   TIMESTAMP               NOT NULL,
    creator         BINARY(16)              NOT NULL,
    group_id        BIGINT                  NOT NULL,
    hardening_time  TIMESTAMP               NOT NULL,
    strength        INTEGER                 NOT NULL,
    max_strength    INTEGER                 NOT NULL,
    is_public       BOOLEAN                 NOT NULL,
    unbreakable     BOOLEAN                 NOT NULL,
    PRIMARY KEY(reinfid),
    FOREIGN KEY(creator)        REFERENCES  nucleus_usernames(puuid)                ON UPDATE CASCADE,
    FOREIGN KEY(group_id)       REFERENCES  ivory_groups(group_id)                  ON DELETE CASCADE   ON UPDATE CASCADE
);

CREATE TABLE amethyst_reinforcement_items (
    reinfid         BIGINT                    NOT NULL,
    material        INTEGER                 NOT NULL,
    amount          INTEGER                 NOT NULL,
    FOREIGN KEY(reinfid)               REFERENCES amethyst_reinforcement_data(reinfid)      ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY(reinfid)
);

CREATE TABLE amethyst_reinforced_block (
    reinfid         BIGINT                  NOT NULL,
    x               INTEGER                 NOT NULL,
    y               INTEGER                 NOT NULL,
    z               INTEGER                 NOT NULL,
    world_uuid      BINARY(16)              NOT NULL,
    PRIMARY KEY(x,y,z,world_uuid),
    FOREIGN KEY(reinfid)               REFERENCES amethyst_reinforcement_data(reinfid)      ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE amethyst_default_group (
    player                  BINARY(16)      NOT NULL,
    group_id                BIGINT          NOT NULL,
    FOREIGN KEY(player)         REFERENCES  nucleus_usernames(puuid)                ON UPDATE CASCADE,
    FOREIGN KEY(group_id)       REFERENCES  ivory_groups(group_id)                  ON DELETE CASCADE   ON UPDATE CASCADE,
    PRIMARY KEY(player)
);

SET foreign_key_checks = 1;
COMMIT;