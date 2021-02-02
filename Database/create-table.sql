create domain GENDER as varchar(6) check (
    VALUE ~ 'MALE'
    or VALUE ~ 'FEMALE'
  );

create domain EDUCATIONAL_DEGREE as varchar(50) check (
    VALUE ~ 'High-School-Diploma' or
    VALUE ~ 'Associate-Degree' or
    VALUE ~ 'Bachelors-Degree' or
    VALUE ~ 'Masters-Degree' or
    VALUE ~ 'Doctoral-Degree' or
    VALUE ~ 'Other'
  );

create table if not exists PatientT
(
  Patient_ID  integer primary key,
  first_name  varchar(50) not null,
  last_name   varchar(50) not null,
  age         integer     not null,
  gender      GENDER      not null,
  occupation  varchar(50),
  reference   varchar(50),
  education   EDUCATIONAL_DEGREE,
  homeAddress varchar(100),
  workAddress varchar(100)
);


create domain PHONE as char(11) check (
  VALUE ~ '0\d{10}'
  );

create table if not exists PatientPhonesT
(
  patient_id   integer,
  phone_number PHONE,
  primary key (patient_id, phone_number),
  constraint fk_patient
    foreign key (patient_id)
      references patientt (patient_id)
      on delete cascade
      on update cascade
);

create table FileT
(
  patient_id    integer primary key,
  creation_date date,
  constraint fk_patient
    foreign key (patient_id)
      references patientt (patient_id)
      on delete cascade
      on update cascade
);

create table pageT
(
  patient_id    integer,
  page_no       integer,
  creation_date date,
  primary key (patient_id, page_no),
  constraint fk_patient
    foreign key (patient_id)
      references patientt (patient_id)
      on delete cascade
      on update cascade
);

create domain MEDICAL_IMAGE_TYPE as varchar(50)
  check (
      VALUE ~ 'CT-Scan' or
      VALUE ~ 'OPG' or
      VALUE ~ 'Radiographic-Image'
    );


create table MedicalImagePageT
(
  patient_id      integer,
  page_no         integer,
  content_address varchar(100)       not null,
  image_type      MEDICAL_IMAGE_TYPE not null,
  reason          varchar(500),
  primary key (patient_id, page_no),
  constraint fk_paget
    foreign key (patient_id, page_no)
      references paget (patient_id, page_no)
      on delete cascade
      on update cascade
);

create domain WEEK_DAY as char(3) check (
    VALUE ~ 'SAT' or
    VALUE ~ 'SUN' or
    VALUE ~ 'MON' or
    VALUE ~ 'TUE' or
    VALUE ~ 'WED' or
    VALUE ~ 'THU' or
    VALUE ~ 'FRI'
  );

create table WeeklyScheduleT
(
  from_date date primary key,
  to_date   date not null unique
);

create table AvailableTimeSlotsT
(
  weekly_schedule_from_date_ref date,
  day_of_week                   WEEK_DAY not null,
  begin_time                    time     not null,
  duration                      time     not null,
  primary key (weekly_schedule_from_date_ref, day_of_week, begin_time),
  constraint fk_weekly_schedule
    foreign key (weekly_schedule_from_date_ref)
      references WeeklyScheduleT (from_date)
      on delete cascade
      on update cascade
);

create table OccupiedTimeSlotsT
(
  date                                date,
  begin_time                          time,
  duration                            time     not null,
  available_time_slots_ref_from_date  date     not null,
  available_time_slots_ref_week_day   WEEK_DAY not null,
  available_time_slots_ref_begin_time time     not null,
  primary key (date, begin_time),
  constraint fk_AvailableTimeSlotsT
    foreign key (available_time_slots_ref_from_date, available_time_slots_ref_week_day,
                 available_time_slots_ref_begin_time)
      references AvailableTimeSlotsT (weekly_schedule_from_date_ref, day_of_week, begin_time)
      on delete cascade
      on update cascade
);

create table AppointmentPageT
(
  patient_id                        integer,
  page_no                           integer,
  treatment_summary                 varchar(500),
  next_appointment_date             date,
  whole_payment_amount              integer,
  paid_payment_amount               integer,
  occupied_time_slot_date_ref       date,
  occupied_time_slot_begin_time_ref time,
  primary key (patient_id, page_no),

  constraint fk_occupiedtimeslotst
    foreign key (occupied_time_slot_date_ref, occupied_time_slot_begin_time_ref)
      references OccupiedTimeSlotsT (date, begin_time)
      on delete cascade
      on update cascade,

  constraint fk_paget
    foreign key (patient_id, page_no)
      references paget (patient_id, page_no)
      on delete cascade
      on update cascade
);

create table PersonalInfoPageT
(
  patient_id              integer,
  page_no                 integer,
  general_medical_records varchar(500),
  dental_records          varchar(500),
  sensitive_medicine      varchar(500),
  does_smoke              boolean,
  signature_image_address varchar(100),
  primary key (patient_id, page_no),

  constraint fk_PageT
    foreign key (patient_id, page_no)
      references paget (patient_id, page_no)
      on delete cascade
      on update cascade
);

create table ReferralOccupiedTimeSlotsT
(
  date       date,
  begin_time time,
  reason     varchar(500),
  patient_id integer not null,
  primary key (date, begin_time),
  constraint fk_patient
    foreign key (patient_id)
      references PatientT (patient_id)
      on delete cascade
      on update cascade,
  constraint fk_OccupiedTimeSlotsT
    foreign key (date, begin_time)
      references OccupiedTimeSlotsT (date, begin_time)
      on delete cascade
      on update cascade
);
