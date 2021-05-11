CREATE TABLE IF NOT EXISTS public.host_info (
    PRIMARY KEY id SERIAL,
    hostname VARCHAR UNIQUE NOT NULL,
    cpu_number SMALLINT NOT NULL,
    cpu_architecture VARCHAR NOT NULL,
    cpu_model VARCHAR NOT NULL,
    cpu_mhz REAL NOT NULL,
    L2_cache INTEGER NOT NULL,
    total_mem INTEGER NOT NULL,
    "timestamp" TIMESTAMP NOT NULL
);