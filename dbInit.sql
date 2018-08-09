create table test.User
(
	id bigint auto_increment,
	userName varchar(100) null,
	passWord varchar(100) null,
	constraint User_id_uindex
		unique (id)
)
comment '用户表'
;

alter table test.User
	add primary key (id)
;

