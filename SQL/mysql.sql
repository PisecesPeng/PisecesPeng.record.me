-- []:需输入相关参数

-- 获取db中的databases
SHOW DATABASES;

-- 获取database中的tables
SELECT table_name,table_comment FROM information_schema.tables WHERE table_schema = [database] AND table_type = 'base table';

-- 获取table中的columns
SELECT column_name,data_type,column_comment FROM information_schema.columns WHERE table_schema = [database] AND table_name = [table];
