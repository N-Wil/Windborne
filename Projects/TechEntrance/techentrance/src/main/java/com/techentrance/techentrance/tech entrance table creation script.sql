use tech_entrance;

create table Jobs(
job_id int not null auto_increment,
link varchar(300) not null,
primary key(job_id)
);

create table user_jobs(
user_id int not null,
job_id varchar(50)
);

create table Clients(
id int not null auto_increment,
first_name varchar(30) not null,
last_name varchar(30) not null,
email varchar(50) not null unique,
phone varchar(10) unique,
looking_for varchar(100) not null,
gpa decimal(3,2),
city varchar(50),
state varchar(50),
pword varchar(50) not null,
primary key(id)
);

create table user_skill(
user_id varchar(45) not null,
skill_id varchar(45) not null
);