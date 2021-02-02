------------------------------------------
-- Triggers related to appointment page --
------------------------------------------

---
-- trigger related to insert, it reverts changes if
-- the newly added record does not match some constraints
---
create function appointment_page_trigger_function() returns trigger
    language plpgsql as
$$
begin
  -- check if the paid payment <= whole payment if not revert changes
  if new.whole_payment_amount < new.paid_payment_amount then
    raise exception 'paid amount is larger than whole amount in query, %', now();
  end if;
  -- check if next appointment date is after the current appointment date if not revert changes
  if new.next_appointment_date < new.occupied_time_slot_date_ref then
    raise exception 'next appointment date is not after the current appointment date, %', now();
  end if;
  return new;
end;
$$;

-- define insert trigger:
create trigger appointment_page_trigger
  before insert or update
  on AppointmentPageT
  for each row
execute procedure appointment_page_trigger_function();


-- trigger for occupied time

-- -- trigger for occupied time insert
create function occupied_time_slots_trigger_function()
  returns trigger as
$$
begin
  -- check if the date matches weekly schedule dates
  if new.date < new.available_time_slots_ref_from_date or
     new.date > (select to_date from WeeklyScheduleT where from_date = new.available_time_slots_ref_from_date)
  then
    raise exception 'Occupied time date does not match the weekly schedule%', now();
  end if;
  -- check if the date matches the available time in schedules week day
  declare
    occupied_time_day_of_week_as_int    integer;
    occupied_time_day_of_week_as_string char(3);
  begin
    select extract(dow from new.date) into occupied_time_day_of_week_as_int;
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
    if occupied_time_day_of_week_as_string != new.available_time_slots_ref_week_day then
      raise exception 'Occupied time date does not match the day of week field %', now();
    end if;
  end;
  -- check if the date matches the available time in schedules time interval in the day
  declare
    available_time_record record;
  begin
    select duration, begin_time
    from AvailableTimeSlotsT
    where new.available_time_slots_ref_from_date = weekly_schedule_from_date_ref
      and new.available_time_slots_ref_week_day = day_of_week
      and new.available_time_slots_ref_begin_time = begin_time into available_time_record;

    if new.available_time_slots_ref_begin_time > new.begin_time or
       available_time_record.duration + available_time_record.begin_time < new.begin_time + new.duration then
      raise exception 'Occupied time in day does not match with the available time %', now();
    end if;
  end;

end;
$$
  language plpgsql;

create trigger occupied_time_slot_trigger
  before insert or update
  on OccupiedTimeSlotsT
  for each row
execute procedure occupied_time_slots_trigger_function();


create function personal_info_page_trigger_function()
    returns trigger as
$$
begin
    if new.page_no != 1 then
        raise exception 'Personal info page number should be 1 %', now();
    end if;
    return new;
end
$$ language plpgsql;

create trigger personal_info_page_trigger
    before update or insert
    on personalinfopaget
    for each row
execute procedure personal_info_page_trigger_function();

create function page_no_trigger_function()
    returns trigger as
$$
begin
    if new.page_no != 1 and (1 > (select count() from paget where paget.patient_id = new.patient_id and paget.page_no = new.page_no - 1)) then
        raise exception 'Personal info page number should be 1 %', now();
    end if;
    return new;
end
$$ language plpgsql;

create trigger page_no_trigger
    before update or insert
    on paget
    for each row
execute procedure page_no_trigger_function();


create function weekly_schedule_trigger_function()
    returns trigger as
$$
begin
    if (0 < (select count() from weeklyschedulet
                    where (from_date < new.from_date and new.from_date <= to_date)
                            or (from_date <= new.to_date and new.to_date <= to_date))) then
        raise exception 'Weekly schedules should not overlap %', now();
    end if;
    return new;
end
$$ language plpgsql;

create trigger weekly_schedule_trigger
    before update or insert
    on weeklyschedulet
    for each row
execute procedure weekly_schedule_trigger_function();


create function available_time_trigger_function()
    returns trigger as
$$
begin
    if (0 < (select count()
                from availabletimeslotst
                where weekly_schedule_from_date_ref = new.weekly_schedule_from_date_ref
                    and day_of_week = new.day_of_week
                    and (begin_time < new.begin_time and new.begin_time < begin_time + duration
                        or begin_time < new.begin_time+new.duration and new.begin_time+new.duration < begin_time + duration))) then
        raise exception 'Available Times should not overlap %', now();
    end if;
    return new;
end
$$ language plpgsql;

create trigger available_time_trigger
    before update or insert
    on availabletimeslotst
    for each row
execute procedure available_time_trigger_function();


create function delete_page_trigger_function()
    returns trigger as
$$
begin
    if old.page_no == 1 or (1 <= (select count() from paget where paget.patient_id = new.patient_id and paget.page_no = new.page_no + 1)) then
        raise exception 'Personal info page number should be 1 %', now();
    end if;
    return new;
end
$$ language plpgsql;

create trigger delete_page_trigger
    before delete
    on paget
    for each row
execute procedure delete_page_trigger_function();


create function page_uniqueness_trigger_function()
    returns trigger as
$$
begin
    if page_no = 1 then
        raise exception 'Page number 1 is reserved for Personal info page %', now();
    end if;
    if 1 <= (select count() from appointmentpaget where patient_id = new.patient_id and page_no = new.page_no)
        and 1 <= (select count() from medicalimagepaget where patient_id = new.patient_id and page_no = new.page_no)
    then
        raise exception 'There is another page with this number %', now();
    end if;
    return new;
end
$$ language plpgsql;

create trigger appointment_page_uniqueness_trigger
    before insert
    on appointmentpaget
    for each row
execute procedure page_uniqueness_trigger_function();

create trigger medical_image_page_uniqueness_trigger
    before insert
    on medicalimagepaget
    for each row
execute procedure page_uniqueness_trigger_function();

create function update_page_no_trigger_function()
    returns trigger as
$$
begin
    raise exception 'Page number can not be updated %', now();
    return new;
end
$$ language plpgsql;

create trigger appointment_page_update_trigger
    before update
        of page_no
    on appointmentpaget
    for each row
execute procedure update_page_no_trigger_function();

create trigger medical_image_page_update_trigger
    before update
        of page_no
    on medicalimagepaget
    for each row
execute procedure update_page_no_trigger_function();
