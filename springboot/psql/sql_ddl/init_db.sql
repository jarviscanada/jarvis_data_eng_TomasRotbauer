\c postgres;
DROP DATABASE IF EXISTS jrvstrading;
DROP DATABASE IF EXISTS jrvstrading_test;


CREATE DATABASE jrvstrading;
GRANT ALL PRIVILEGES ON DATABASE jrvstrading TO postgres;

--db for test case
CREATE DATABASE jrvstrading_test;
GRANT ALL PRIVILEGES ON DATABASE jrvstrading_test TO postgres;
