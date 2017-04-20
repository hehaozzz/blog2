--创建博客表
CREATE TABLE article
(
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  author INT NOT NULL,
  createtime date,
  description VARCHAR(300),
  content TEXT NOT NULL,
  md TEXT NOT NULL,
  primary key(id)
);
commit;

select * from article;