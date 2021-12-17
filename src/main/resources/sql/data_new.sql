INSERT INTO permissions(id, name)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN')
;

INSERT INTO access_group(id, name)
VALUES (1, 'USER_GROUP'),
       (2, 'ADMIN_GROUP')
;

-- USER_GROUP (ROLE_USER)
-- ADMIN_GROUP (ROLE_USER, ROLE_ADMIN)
INSERT INTO group_permission(id, group_id, permission_id)
VALUES (1, 1, 1),
       (2, 2, 1),
       (3, 2, 2)
;

-- 이메일 : taid@test.com, 패스워드 : taid123
INSERT INTO member(id, created_date, last_modified_date, birth, email, gender, group_id, name, nickname, passwd, profile_image)
values (1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '1999-11-11', 'taid@test.com', 'MALE', 1, '엄진환', 'Taid', '$2b$10$pOvH32HB1Am/1N9CtJpkl.hTx7FzeKuIDavoZlZ8SIGtt5CBPKtmS', 'none');

-- 이메일 : henry@test.com, 패스워드 : henry123
INSERT INTO member(id, created_date, last_modified_date, birth, email, gender, group_id, name, nickname, passwd, profile_image)
values (2, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '1999-11-11', 'henry@test.com', 'MALE', 1, '이태현', 'Henry', '$2b$10$b8HRGV3OSmhWoAE7176ue.jEEjxQQ/B5hGnBDGlPCi7o7ed9KNCiy', 'none');

-- 이메일 : teru@test.com, 패스워드 : teru123
INSERT INTO member(id, created_date, last_modified_date, birth, email, gender, group_id, name, nickname, passwd, profile_image)
values (3, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '1999-11-11', 'teru@test.com', 'MALE', 1, '김휘년', 'Teru', '$2b$10$Zhie1OC14uG2w6WRq927T.uqkui.gBUKuARcsS8uU9BBzvjNq3bMu', 'none');

-- 이메일 : baek@test.com, 패스워드 : baek123
INSERT INTO member(id, created_date, last_modified_date, birth, email, gender, group_id, name, nickname, passwd, profile_image)
values (4, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '1999-11-11', 'baek@test.com', 'MALE', 1, '김성백', 'Baek', '$2b$10$r4JFRPbG8szhp2uOSwMp0eJmVlAm5b3ijJAR5LA/WJ4zqLs/cTdga', 'none');

-- 이메일 : jay@test.com, 패스워드 : jay123
INSERT INTO member(id, created_date, last_modified_date, birth, email, gender, group_id, name, nickname, passwd, profile_image)
values (5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '1999-11-11', 'jay@test.com', 'FEMALE', 1, '박지영', 'Jay', '$2b$10$i6Cq0VpfJp6HrTDgMdYVQumlI6hRJ4EyBcuvfbGOL3PkdV/6RIUB6', 'none');

-- 이메일 : mini@test.com, 패스워드 : mini123
INSERT INTO member(id, created_date, last_modified_date, birth, email, gender, group_id, name, nickname, passwd, profile_image)
values (6, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '1999-11-11', 'mini@test.com', 'FEMALE', 1, '김민정', 'Mini', '$2b$10$pt57S1oP3tsCr5TMhxp35eFu97uGCS7z.Lc5er8Iu9vFt198Vc9WC', 'none');
