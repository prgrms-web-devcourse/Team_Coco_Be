drop table if exists schedule_theme CASCADE;
drop table if exists voting_content_member CASCADE;
drop table if exists checklist CASCADE;
drop table if exists memo CASCADE;
drop table if exists voting_content CASCADE;
drop table if exists voting CASCADE;
drop table if exists daily_schedule_spot CASCADE;
drop table if exists spot CASCADE;
drop table if exists friend CASCADE;
drop table if exists group_permission CASCADE;
drop table if exists permissions CASCADE;
drop table if exists schedule_post_nested_comments CASCADE;
drop table if exists schedule_post_comments CASCADE;
drop table if exists schedule_post_like CASCADE;
drop table if exists schedule_posts CASCADE;
drop table if exists schedule_member CASCADE;
drop table if exists schedule CASCADE;
drop table if exists member CASCADE;
drop table if exists access_group CASCADE;

CREATE TABLE permissions
(
    id   BIGINT NOT NULL,
    name VARCHAR(255),
    primary key (id)
);

CREATE TABLE access_group
(
    id   BIGINT NOT NULL,
    name VARCHAR(255),
    CONSTRAINT access_group_id primary key (id)
);

CREATE TABLE member
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    birth              VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    gender             VARCHAR(255) NOT NULL,
    name               VARCHAR(255) NOT NULL,
    nickname           VARCHAR(255) NOT NULL,
    passwd             VARCHAR(255),
    profile_image      VARCHAR(255),
    group_id           BIGINT       NOT NULL,
    primary key (id),
    FOREIGN KEY (group_id) REFERENCES access_group (id)
);

CREATE TABLE friend
(
    from_id BIGINT NOT NULL,
    to_id   BIGINT NOT NULL,
    FOREIGN KEY (from_id) REFERENCES member (id),
    FOREIGN KEY (to_id) REFERENCES member (id),
    PRIMARY KEY (from_id, to_id)
);


CREATE TABLE group_permission
(
    id            BIGINT NOT NULL,
    group_id      BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    primary key (id),
    FOREIGN KEY (group_id) REFERENCES access_group (id)
);

CREATE TABLE schedule
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    end_date           date         NOT NULL,
    member_id          BIGINT       NOT NULL,
    start_date         date         NOT NULL,
    title              VARCHAR(255) NOT NULL,
    primary key (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE schedule_posts
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    city               VARCHAR(255) NOT NULL,
    content            text         NOT NULL,
    liked              BIGINT       NOT NULL,
    title              VARCHAR(255) NOT NULL,
    views              BIGINT       NOT NULL,
    member             BIGINT,
    schedule           BIGINT,
    primary key (id),
    FOREIGN KEY (member) REFERENCES member (id),
    FOREIGN KEY (schedule) REFERENCES schedule (id)
);

CREATE TABLE checklist
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    checked            boolean DEFAULT false,
    day                INTEGER,
    title              VARCHAR(255) NOT NULL,
    schedule_id        BIGINT,
    primary key (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id)
);

CREATE TABLE spot
(
    id                 BIGINT       NOT NULL,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    address_name       VARCHAR(255),
    latitude           double       NOT NULL,
    longitude          double       NOT NULL,
    phone              VARCHAR(255),
    name               VARCHAR(255) NOT NULL,
    road_address_name  VARCHAR(255),
    primary key (id)
);

CREATE TABLE daily_schedule_spot
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    date_order         INTEGER NOT NULL,
    spot_order         INTEGER NOT NULL,
    spot_id            BIGINT  NOT NULL,
    place_name         VARCHAR(255) NOT NULL,
    schedule_id        BIGINT,
    primary key (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id),
    FOREIGN KEY (spot_id) REFERENCES spot (id)
);

CREATE TABLE memo
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    content            VARCHAR(255) NOT NULL,
    member_id          BIGINT       NOT NULL,
    title              VARCHAR(255) NOT NULL,
    schedule_id        BIGINT,
    primary key (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id)
);

CREATE TABLE schedule_post_comments
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    content            VARCHAR(255) NOT NULL,
    member_id          BIGINT       NOT NULL,
    schedule_post      BIGINT,
    primary key (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (schedule_post) REFERENCES schedule_posts (id)
);

CREATE TABLE schedule_post_like
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    member_id          BIGINT NOT NULL,
    schedule_post_id   BIGINT,
    primary key (id),
    FOREIGN KEY (schedule_post_id) REFERENCES schedule_posts (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);
CREATE TABLE schedule_post_nested_comments
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    content            VARCHAR(255) NOT NULL,
    member_id          BIGINT       NOT NULL,
    comment            BIGINT,
    primary key (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (comment) REFERENCES schedule_post_comments (id)
);

CREATE TABLE schedule_member
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    member_id          BIGINT NOT NULL,
    schedule_id        BIGINT,
    primary key (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id)
);

CREATE TABLE schedule_theme
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    theme              VARCHAR(255),
    schedule_id        BIGINT,
    primary key (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id)
);

CREATE TABLE voting
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    member_id          BIGINT       NOT NULL,
    multiple_flag      boolean,
    title              VARCHAR(255) NOT NULL,
    schedule_id        BIGINT,
    primary key (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (schedule_id) REFERENCES schedule (id)
);

CREATE TABLE voting_content
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    content            VARCHAR(255) NOT NULL,
    voting_id          BIGINT,
    primary key (id),
    FOREIGN KEY (voting_id) REFERENCES voting (id)
);

CREATE TABLE voting_content_member
(
    id                 BIGINT NOT NULL AUTO_INCREMENT,
    created_by         BIGINT,
    created_date       timestamp,
    last_modified_by   BIGINT,
    last_modified_date timestamp,
    member_id          BIGINT NOT NULL,
    voting_content_id  BIGINT,
    primary key (id),
    FOREIGN KEY (member_id) REFERENCES member (id)
);
