-- patients who owe money
select occupied_time_slot_date_ref as date_of_appointment, patient_id, first_name, last_name, whole_payment_amount - paid_payment_amount as debt
	from AppointmentPageT natural join PatientT
	where whole_payment_amount > paid_payment_amount;


select date, begin_time, begin_time + duration::interval as end_time, patient_id, reason
	from ReferralOccupiedTimeSlotsT natural join OccupiedTimeSlotsT;
