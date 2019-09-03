create table role
(
  id   int auto_increment
    primary key,
  name varchar(20) null,
  age  int         null,
  note varchar(50) null
);

create table user
(
  id    int auto_increment
    primary key,
  phone varchar(20) null,
  name  varchar(20) null,
  age   int         null
);

create table student
(
  id    int auto_increment,
  name  varchar(20) null,
  level varchar(20) null,
  constraint student_id_uindex
    unique (id)
);

alter table student
  add primary key (id);