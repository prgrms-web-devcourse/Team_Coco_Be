drop table if exists access_group CASCADE;
drop table if exists checklist CASCADE;
drop table if exists daily_schedule_spot CASCADE;
drop table if exists group_permission CASCADE;
drop table if exists member CASCADE;
drop table if exists memo CASCADE;
drop table if exists permissions CASCADE;
drop table if exists schedule CASCADE;
drop table if exists schedule_post_comments CASCADE;
drop table if exists schedule_post_like CASCADE;
drop table if exists schedule_post_nested_comments CASCADE;
drop table if exists schedule_posts CASCADE;
drop table if exists schedule_member CASCADE;
drop table if exists schedule_theme CASCADE;
drop table if exists spot CASCADE;
drop table if exists voting CASCADE;
drop table if exists voting_content CASCADE;
drop table if exists voting_content_member CASCADE;


create table member
(
    id                 bigint       not null,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    birth              varchar(255) not null,
    email              varchar(255) not null,
    gender             varchar(255) not null,
    name               varchar(255) not null,
    nickname           varchar(255) not null,
    passwd             varchar(255),
    profile_image      varchar(255),
    group_id           bigint       not null,
    primary key (id)
);
create table group_permission
(
    id            bigint not null,
    group_id      bigint not null,
    permission_id bigint not null,
    primary key (id)
);
create table permissions
(
    id   bigint not null,
    name varchar(255),
    primary key (id)
);
create table access_group
(
    id   bigint not null,
    name varchar(255),
    CONSTRAINT access_group_id primary key (id)
);
create table checklist
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    checked            boolean default false,
    day                integer,
    title              varchar(255) not null,
    schedule_id        bigint,
    primary key (id)
);
create table daily_schedule_spot
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    trip_date          integer not null,
    trip_order         integer not null,
    spot_id            bigint  not null,
    schedule_id        bigint,
    primary key (id)
);
create table memo
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    content            varchar(255) not null,
    member_id          bigint       not null,
    title              varchar(255) not null,
    schedule_id        bigint,
    primary key (id)
);
create table schedule
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    end_date           date         not null,
    member_id          bigint       not null,
    start_date         date         not null,
    title              varchar(255) not null,
    primary key (id)
);
create table schedule_post_comments
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    content            varchar(255) not null,
    member_id          bigint       not null,
    schedule_post      bigint,
    primary key (id)
);
create table schedule_post_like
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    member_id          bigint not null,
    schedule_post_id   bigint,
    primary key (id)
);
create table schedule_post_nested_comments
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    content            varchar(255) not null,
    member_id          bigint       not null,
    comment            bigint,
    primary key (id)
);
create table schedule_posts
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    city               varchar(255) not null,
    content            text         not null,
    liked              bigint       not null,
    title              varchar(255) not null,
    views              bigint       not null,
    member             bigint,
    schedule           bigint,
    primary key (id)
);
create table schedule_member
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    member_id          bigint not null,
    schedule_id        bigint,
    primary key (id)
);
create table schedule_theme
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    theme              varchar(255),
    schedule_id        bigint,
    primary key (id)
);
create table spot
(
    id                 bigint       not null,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    address_name       varchar(255),
    latitude           double       not null,
    longitude          double       not null,
    phone              varchar(255),
    name               varchar(255) not null,
    road_address_name  varchar(255),
    primary key (id)
);
create table voting
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    member_id          bigint       not null,
    multiple_flag      boolean,
    title              varchar(255) not null,
    schedule_id        bigint,
    primary key (id)
);
create table voting_content
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    content            varchar(255) not null,
    voting_id          bigint,
    primary key (id)
);
create table voting_content_member
(
    id                 bigint generated by default as identity,
    created_by         bigint,
    created_date       timestamp,
    last_modified_by   bigint,
    last_modified_date timestamp,
    member_id          bigint not null,
    voting_content_id  bigint,
    primary key (id)
);
-- 외래키 constraint 제약 이름 : f_[테이블 이름]_[외래키 이름]
alter table group_permission
    add constraint f_group_permission_group_id
        foreign key (group_id)
            references access_group;
alter table group_permission
    add constraint f_group_permission_permission_id
        foreign key (permission_id)
            references permissions;
alter table member
    add constraint  f_member_permission_id
        foreign key (group_id)
            references access_group;

alter table checklist
    add constraint f_checklist_schedule_id
        foreign key (schedule_id)
            references schedule;
alter table daily_schedule_spot
    add constraint f_daily_schedule_spot_schedule_id
        foreign key (schedule_id)
            references schedule;
alter table memo
    add constraint f_memo_schedule_id
        foreign key (schedule_id)
            references schedule;
alter table schedule_post_comments
    add constraint f_schedule_post_comments_schedule_post
        foreign key (schedule_post)
            references schedule_posts;
alter table schedule_post_like
    add constraint f_schedule_post_like_schedule_post_id
        foreign key (schedule_post_id)
            references schedule_posts;
alter table schedule_post_nested_comments
    add constraint f_schedule_post_nested_comments_comment
        foreign key (comment)
            references schedule_post_comments;
alter table schedule_posts
    add constraint f_schedule_post_member
        foreign key (member)
            references member;
alter table schedule_posts
    add constraint f_schedule_posts_schedule
        foreign key (schedule)
            references schedule;
alter table schedule_member
    add constraint f_schedule_member_schedule_id
        foreign key (schedule_id)
            references schedule;
alter table schedule_theme
    add constraint f_schedule_theme_schedule_id
        foreign key (schedule_id)
            references schedule;
alter table voting
    add constraint f_voting_schedule_id
        foreign key (schedule_id)
            references schedule;
alter table voting_content
    add constraint f_voting_content_voting_id
        foreign key (voting_id)
            references voting;
alter table voting_content_member
    add constraint f_voting_content_member_voting_content_id
        foreign key (voting_content_id)
            references voting_content;