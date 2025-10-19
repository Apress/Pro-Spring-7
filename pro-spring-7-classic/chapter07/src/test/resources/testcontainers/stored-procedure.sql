CREATE PROCEDURE `getFirstNameByIdProc`( IN in_id INT, OUT fn_res VARCHAR(60))
BEGIN
    BEGIN
        SELECT first_name INTO fn_res FROM SINGER WHERE id = in_id;
    END;
END;
