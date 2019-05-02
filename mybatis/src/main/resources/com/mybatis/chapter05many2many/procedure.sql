drop procedure if exists print_role_name;
create procedure print_role_name(in role_id int,out result varchar(20))
  comment 'procedure for mybatis'
  begin
    declare str varchar(20);
    set str = (select name from role where id = role_id);
    set result = concat('hello ',str);
  end;

drop procedure if exists proc_cursor_collection;
create procedure proc_cursor_collection(in start int,in end int,out roles text)
  begin
    declare done int default 0;
    declare role_name varchar(20);
    declare role_cursor cursor for select role.name from role where id between start and end;
    # 缺少下面一行将报错 No data - zero rows fetched, selected, or processed
    declare continue handler for not found set done=1;
    open role_cursor;
    #使用loop end loop会死循环，出现data too long for roles
    repeat
      fetch role_cursor into role_name;
      #下面两行选其一
      #set roles = concat_ws(',',roles,role_name);
      select concat_ws(',',roles,role_name) into roles;
    until done end repeat;
    close role_cursor;
  end;