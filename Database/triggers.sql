-- appointment page triggers:

-- -- trigger for after insert:
create function insert_appointment_page_trigger_function() returns trigger as
$$
begin
  if new.whole_payment_amount < new.paid_payment_amount then
    delete from AppointmentPageT where patient_id = new.patient_id and page_no = new.page_no;
    raise exception 'paid amount is larger than whole amount in query, %', now();
  end if;
  if new.next_appointment_date < new.occupied_time_slot_date_ref then
    delete from AppointmentPageT where patient_id = new.patient_id and page_no = new.page_no;
    raise exception 'next appointment date is not after the current appointment date, %', now();
  end if;
end;
$$
  language plpgsql;

create trigger insert_appointment_page
  after insert
  on AppointmentPageT
  for each row
execute procedure insert_appointment_page_trigger_function();

-- -- trigger for update
create function update_appointment_page_trigger_function() returns trigger as
$$
begin
  if new.whole_payment_amount < new.paid_payment_amount then
    delete from AppointmentPageT where patient_id = new.patient_id and page_no = new.page_no;
    insert into AppointmentPageT
    values (old.patient_id, old.page_no, old.treatment_summary,
            old.next_appointment_date, old.whole_payment_amount,
            old.paid_payment_amount, old.occupied_time_slot_date_ref,
            old.occupied_time_slot_begin_time_ref);
    raise exception 'paid amount is larger than whole amount in query, %', now();
  end if;
  if new.next_appointment_date < new.occupied_time_slot_date_ref then
    delete from AppointmentPageT where patient_id = new.patient_id and page_no = new.page_no;
    insert into AppointmentPageT
    values (old.patient_id, old.page_no, old.treatment_summary,
            old.next_appointment_date, old.whole_payment_amount,
            old.paid_payment_amount, old.occupied_time_slot_date_ref,
            old.occupied_time_slot_begin_time_ref);
    raise exception 'next appointment date is not after the current appointment date, %', now();
  end if;
end;
$$
  language plpgsql;

create trigger update_appointment_page
  after update
  on AppointmentPageT
  for each row
execute procedure update_appointment_page_trigger_function();

-- trigger for occupied time

-- -- trigger for occupied time insert
create function insert_occupied_time_slots_trigger_function()
  returns trigger as
$$
begin
  -- check if the date matches weekly schedule dates
  if new.date < new.available_time_slots_ref_from_date or
     new.date > (select to_date from WeeklyScheduleT where from_date = new.available_time_slots_ref_from_date)
  then
    delete from OccupiedTimeSlotsT where new.date = date and new.begin_time = begin_time;
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
      delete from OccupiedTimeSlotsT where new.date = date and new.begin_time = begin_time;
      raise exception 'Occupied time date does not match the day of week field %', now();
    end if;
  end;
  -- check if the date matches the available time in schedules time interval in the day
  declare
    available_time_record record;
  begin
    select duration, begin_time
    from AvailableTimeSlotsT
    where now.available_time_slots_ref_from_date = weekly_schedule_from_date_ref
      and now.available_time_slots_ref_week_day = day_of_week
      and now.available_time_slots_ref_begin_time = begin_time into available_time_record;

    if now.available_time_slots_ref_begin_time > now.begin_time or
       available_time_record.duration + available_time_record.begin_time < now.begin_time + now.duration then
      delete from OccupiedTimeSlotsT where new.date = date and new.begin_time = begin_time;
      raise exception 'Occupied time in day does not match with the available time %', now();
    end if;
  end;

end;
$$
  language plpgsql;

create trigger insert_occupied_time_slot
  after insert
  on OccupiedTimeSlotsT
  for each row
execute procedure insert_occupied_time_slots_trigger_function();

create function update_occupied_time_slots_trigger_function()
  returns trigger as
$$
declare
  ret_val record;
begin
  delete from OccupiedTimeSlots as T1
  where T1.date = new.date and T1.begin_time = new.begin_time;
  select insert_occupied_time_slots_trigger_function() into ret_val;
  return ret_val;
end;
$$ language plpgsql;

create trigger update_occupied_time_slot
  after update
  on OccupiedTimeSlotsT
  for each row
execute procedure update_occupied_time_slots_trigger_function();
