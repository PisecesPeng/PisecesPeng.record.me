-- []:需输入相关参数

-- 获取db中的databases
SELECT USERNAME FROM SYS.DBA_USERS;

-- 获取database中的tables
SELECT TABLE_NAME,COMMENTS FROM SYS.DBA_TAB_COMMENTS WHERE OWNER = UPPER([database]);

-- 获取table中的columns
SELECT atc.column_name,atc.data_type,acc.comments FROM ALL_COL_COMMENTS acc JOIN ALL_TAB_COLUMNS atc ON atc.owner = acc.owner AND acc.column_name = atc.column_name AND acc.table_name = atc.table_name WHERE atc.OWNER = UPPER([database]) AND acc.table_name = UPPER([table]);

