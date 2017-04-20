--创建用户表
CREATE TABLE sec_user
(
  user_name VARCHAR(10) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  salt VARCHAR(10) not null,
  user_id int NOT NULL AUTO_INCREMENT,
  primary key(user_id)
);
commit;
select * from sec_user;

--创建用户角色关联表
CREATE TABLE sec_user_role
(
  role_id int NOT NULL,
  user_id int NOT NULL UNIQUE
);
commit;
select * from sec_user_role;

--创建角色表
CREATE TABLE sec_role
(
  role_id int NOT NULL AUTO_INCREMENT,
  role_name VARCHAR(16) NOT NULL,
  primary key(role_id)
);

insert into sec_role(role_name) values('admin');
insert into sec_role(role_name) values('members');
commit;
select * from sec_role;

--创建角色权限关联表
CREATE TABLE sec_role_permission
(
  permission_id int NOT NULL,
  role_id int NOT NULL UNIQUE
);

insert into sec_role_permission(permission_id,role_id) values(1,1);
insert into sec_role_permission(permission_id,role_id) values(1,2);
commit;
select * from sec_role_permission;

--创建权限表
CREATE TABLE sec_permission
(
  permission_id int NOT NULL AUTO_INCREMENT,
  permission_name VARCHAR(16) NOT NULL,
  primary key(permission_id)
);

insert into sec_permission(permission_name) values('高级');
insert into sec_permission(permission_name) values('初级');
commit;
select * from sec_permission;