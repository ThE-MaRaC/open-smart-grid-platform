DO
$$
BEGIN

IF NOT EXISTS (SELECT 1 FROM device_function_mapping WHERE "function" = 'GET_PROFILE_GENERIC_DATA') THEN
	insert into device_function_mapping (function_group, "function") values ('OWNER', 'GET_PROFILE_GENERIC_DATA'); 
END IF;

END;
$$
