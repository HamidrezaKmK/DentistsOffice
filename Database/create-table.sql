create domain Gender as varchar(6) check(
	VALUE ~ 'MALE'
or	VALUE ~ 'FEMALE'
);

create domain EducationalDegree as varchar(50) check(
	VALUE ~ 'High-School-Diploma' or
    VALUE ~ 'Associate-Degree' or
    VALUE ~ 'Bachelors-Degree' or
    VALUE ~ 'Masters-Degree' or
    VALUE ~ 'Doctoral-Degree' or
    VALUE ~ 'Other'
);

create table if not exists PatientT(
	PatientID varchar(50) primary key,
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    age integer not null,
    gender Gender not null,
    occupation varchar(50),
    reference varchar(50),
    education EducationalDegree,
    homeAddress varchar(100),
    workAddress varchar(100)
);

