--Connect to host_agent database
\c host_agent;

CREATE TABLE IF NOT EXISTS public.host_info (
    id               SERIAL PRIMARY KEY,
    hostname         VARCHAR UNIQUE NOT NULL,
    cpu_number       SMALLINT NOT NULL,
    cpu_architecture VARCHAR NOT NULL,
    cpu_model        VARCHAR NOT NULL,
    cpu_mhz          REAL NOT NULL,
    L2_cache         INTEGER NOT NULL,
    total_mem        INTEGER NOT NULL,
    "timestamp"      TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS public.host_usage (
    "timestamp"    TIMESTAMP NOT NULL,
    host_id        SERIAL NOT NULL,
    memory_free    INTEGER NOT NULL,
    cpu_idle       SMALLINT NOT NULL,
    cpu_kernel     SMALLINT NOT NULL,
    disk_io        SMALLINT NOT NULL,
    disk_available INTEGER NOT NULL,

    FOREIGN KEY (host_id) REFERENCES
        public.host_info (id)
);