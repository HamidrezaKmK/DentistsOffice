create view WeeklyScheduleV as select * from weeklyschedulet;
create view AvailableTimeSlotsV as select * from availabletimeslotst;
create view OccupiedTimeSlotsV as select * from occupiedtimeslotst;
create view ReferralOccupiedTimeSlotsV as select * from referraloccupiedtimeslotst;

create function do_not_change()
    returns trigger
as
$$
begin
    raise exception 'Cannot modify table.
Contact the system administrator if you want to make this change.';
end;
$$
    language plpgsql;

create trigger weekly_schedule_view_trigger
    before insert or update or delete
    on WeeklyScheduleV
execute procedure do_not_change();

create trigger available_time_slots_view_trigger
    before insert or update or delete
    on WeeklyScheduleV
execute procedure do_not_change();

create trigger occupied_time_slots_view_trigger
    before delete
    on OccupiedTimeSlotsV
execute procedure do_not_change();

create trigger referral_view_trigger
    before update or delete
    on ReferralOccupiedTimeSlotsV
execute procedure do_not_change();


create function available_time_in_date(d date)
    returns table(begin_time time, duration time) as
$$
declare
    occupied_time_day_of_week_as_int    integer;
    occupied_time_day_of_week_as_string char(3);
begin
    select extract(dow from d) into occupied_time_day_of_week_as_int;
    case occupied_time_day_of_week_as_int
        when 0 then
            occupied_time_day_of_week_as_string := 'SUN';
        when 1 then
            occupied_time_day_of_week_as_string := 'MON';
        when 2 then
            occupied_time_day_of_week_as_string := 'TUE';
        when 3 then
            occupied_time_day_of_week_as_string := 'WED';
        when 4 then
            occupied_time_day_of_week_as_string := 'THU';
        when 5 then
            occupied_time_day_of_week_as_string := 'FRI';
        when 6 then
            occupied_time_day_of_week_as_string := 'SAT';
        end case;
    return query
        select T1.begin_time, T1.duration from WeeklyScheduleV natural join AvailableTimeSlotsV as T1
        where from_date <= d
          and d <= to_date
          and occupied_time_day_of_week_as_string = day_of_week;
end;
$$
    language plpgsql;

create function occupied_time_in_date(d date)
    returns table(begin_time time, duration time) as
$$
begin
    return query
        select OccupiedTimeSlotsV.begin_time, OccupiedTimeSlotsV.duration
        from OccupiedTimeSlotsV
        where OccupiedTimeSlotsV.date <= d;
end;
$$
    language plpgsql;


create function reserve_time(d date, btime time, dur time, rsn varchar(500), pid integer)
    returns integer as
$$
declare
    occupied_time_day_of_week_as_int    integer;
    occupied_time_day_of_week_as_string char(3);
    available record;
begin
    select extract(dow from d) into occupied_time_day_of_week_as_int;
    case occupied_time_day_of_week_as_int
        when 0 then
            occupied_time_day_of_week_as_string := 'SUN';
        when 1 then
            occupied_time_day_of_week_as_string := 'MON';
        when 2 then
            occupied_time_day_of_week_as_string := 'TUE';
        when 3 then
            occupied_time_day_of_week_as_string := 'WED';
        when 4 then
            occupied_time_day_of_week_as_string := 'THU';
        when 5 then
            occupied_time_day_of_week_as_string := 'FRI';
        when 6 then
            occupied_time_day_of_week_as_string := 'SAT';
        end case;
    select T1.weekly_schedule_from_date_ref, T1.day_of_week, T1.begin_time from WeeklyScheduleV natural join AvailableTimeSlotsV as T1
    where from_date <= d
      and d <= to_date
      and occupied_time_day_of_week_as_string = day_of_week
      and T1.begin_time <= btime and btime < T1.begin_time + T1.duration::interval
      and btime + dur::interval < T1.begin_time + T1.duration::interval into available;
    insert into OccupiedTimeSlotsV values (d, btime, dur,
                                           available.weekly_schedule_from_date_ref,
                                           available.day_of_week,
                                           available.begin_time);
    insert into ReferralOccupiedTimeSlotsV values (d, btime, rsn, pid);
    return 1;
end;
$$
    language plpgsql;




