--创建临时表空间
create temporary tablespace sb_temp   
  tempfile '/orcl/app/oracle/product/12.2.0.1.0/db_1/oradata/simpleblog/sb_temp01.dbf'   
  size 32m   
  autoextend on   
  next 32m maxsize 2048m   
  extent management local; 
  
--创建永久表空间
create tablespace sb_data   
  logging   
  datafile '/orcl/app/oracle/product/12.2.0.1.0/db_1/oradata/simpleblog/sb_data01.dbf'   
  size 32m   
  autoextend on   
  next 32m maxsize 2048m   
  extent management local;
  
--创建用户并授权
create user hehao identified by hehao  
  default tablespace sb_data   
  temporary tablespace sb_temp;  
  grant connect,resource to hehao;
  
  ALTER  USER  hehao  QUOTA  UNLIMITED  ON  sb_data;