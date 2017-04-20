drop table if exists sequence;
create table sequence (         --创建序列表
seq_name        VARCHAR(50) NOT NULL,
current_val     INT         NOT NULL	DEFAULT 1,  
increment_val   INT         NOT NULL    DEFAULT 1,          
PRIMARY KEY (seq_name)   );

INSERT INTO SEQUENCE VALUES('eg_seq',1,1);--添加序列

create function currval(v_seq_name VARCHAR(50))     --获取当前序列值的函数
returns integer    
begin        
    declare value integer;         
    set value = 0;         
    select current_val into value  from sequence where seq_name = v_seq_name;   
   return value;   
end; 

create function nextval (v_seq_name VARCHAR(50))   --获取序列下一个值的函数
returns integer  
begin  
    update sequence set current_val = current_val + increment_val  where seq_name = v_seq_name;  
    return currval(v_seq_name);  
end;