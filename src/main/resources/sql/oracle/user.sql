--创建用户表
CREATE SEQUENCE sec_user_seq
INCREMENT BY 1 
START WITH 1 
maxvalue 999999999999
NOCYCLE
NOCACHE; 
commit;

CREATE TABLE sec_user
(
  user_name VARCHAR2(10) NOT NULL UNIQUE,
  password VARCHAR2(16) NOT NULL,
  user_id number(18) NOT NULL,
  primary key(user_id)
);
commit;
alter table sec_user add (salt VARCHAR2(10) not null);
alter table sec_user modify (password varchar2(100));
select * from sec_user;

--创建用户角色关联表
CREATE TABLE sec_user_role
(
  role_id number(18) NOT NULL,
  user_id number(18) NOT NULL UNIQUE
);
commit;
select * from sec_user_role;

--创建角色表
CREATE SEQUENCE sec_role_seq
INCREMENT BY 1 
START WITH 1 
maxvalue 999999999999
NOCYCLE
NOCACHE; 
commit;

CREATE TABLE sec_role
(
  role_id number(18) NOT NULL,
  role_name VARCHAR2(16) NOT NULL,
  primary key(role_id)
);

insert into sec_role(role_id,role_name) values(sec_role_seq.nextval,'超级管理员');
insert into sec_role(role_id,role_name) values(sec_role_seq.nextval,'普通会员');
commit;
select * from sec_role;

--创建角色权限关联表
CREATE TABLE sec_role_permission
(
  permission_id number(18) NOT NULL,
  role_id number(18) NOT NULL UNIQUE
);

insert into sec_role_permission(permission_id,role_id) values(1,1);
insert into sec_role_permission(permission_id,role_id) values(1,2);
commit;
select * from sec_role_permission;

--创建权限表
CREATE SEQUENCE sec_permission_seq
INCREMENT BY 1 
START WITH 1 
maxvalue 999999999999
NOCYCLE
NOCACHE; 
commit;

CREATE TABLE sec_permission
(
  permission_id number(18) NOT NULL,
  permission_name VARCHAR2(16) NOT NULL,
  primary key(permission_id)
);

insert into sec_permission(permission_id,permission_name) values(sec_permission_seq.nextval,'高级');
insert into sec_permission(permission_id,permission_name) values(sec_permission_seq.nextval,'初级');
commit;
select * from sec_permission;