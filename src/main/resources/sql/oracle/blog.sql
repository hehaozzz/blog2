--创建博客表
CREATE SEQUENCE article_seq
INCREMENT BY 1 
START WITH 1 
maxvalue 999999999999
NOCYCLE
NOCACHE; 
commit;

CREATE TABLE article
(
  id number(18) NOT NULL,
  title VARCHAR2(30) NOT NULL,
  author number(18) NOT NULL,
  createtime date,
  description VARCHAR2(100),
  content clob NOT NULL,
  primary key(id)
);
alter table article add (md clob not null);
alter table article modify (title varchar2(100));
alter table article modify (description varchar2(300));
commit;

select * from article;