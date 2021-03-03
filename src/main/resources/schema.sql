USE taules;

CREATE TABLE guild
(
    guild_id bigint       NOT NULL,
    name     NVARCHAR(32) NOT NULL,
    PRIMARY KEY (guild_id)
);

CREATE TABLE user
(
    user_id bigint       NOT NULL,
    name    nvarchar(32) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE guild_user
(
    id           int          NOT NULL AUTO_INCREMENT,
    guild        bigint       NOT NULL,
    user         bigint       NOT NULL,
    display_name nvarchar(32) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (guild) REFERENCES guild (guild_id),
    CONSTRAINT FOREIGN KEY (user) REFERENCES user (user_id)
);

CREATE TABLE message_log
(
    id         int      NOT NULL AUTO_INCREMENT,
    guild_user int      NOT NULL,
    time_sent  datetime NOT NULL DEFAULT (NOW()),
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (guild_user) REFERENCES guild_user (id)
);

CREATE TABLE call_log
(
    id          int      NOT NULL AUTO_INCREMENT,
    guild_user  int      NOT NULL,
    time_joined datetime NOT NULL DEFAULT (NOW()),
    time_left   datetime,
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (guild_user) REFERENCES guild_user (id)
);