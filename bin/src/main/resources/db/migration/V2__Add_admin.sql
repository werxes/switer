insert into usr (id, username, password, active) values (1, 'admin', '$2a$08$aiGeQsVo1SeCZViFxTIaLeSaPrBsOk99b271R2L5nMC3x2/KUMq5C', true);
insert into user_role (user_id, roles) values (1, 'USER' ), (1, 'ADMIN');
