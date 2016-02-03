DROP INDEX IF EXISTS security_key_valid_idx;

CREATE UNIQUE INDEX security_key_valid_idx ON security_key (dlms_device_id, security_key_type, valid_from)
WHERE valid_to IS NULL;