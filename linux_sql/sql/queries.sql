--Group hosts by hardware info
SELECT cpu_number, id AS host_id, total_mem
FROM public.host_info
ORDER BY cpu_number, total_mem DESC;

--Average memory usage
SELECT usage.host_id,
    info.hostname AS host_name,
    (date_trunc('hour', usage.timestamp) +
        date_part('minute', usage.timestamp)::int / 5 *
        interval '5 min') AS time_stamp,
    AVG((info.total_mem - usage.memory_free)::real / info.total_mem * 100)
       AS avg_used_mem_percentage
FROM public.host_info info JOIN public.host_usage usage
    ON info.id = usage.host_id
GROUP BY usage.host_id, host_name, time_stamp
ORDER BY host_id, time_stamp;

--Detect host failure
SELECT host_id,
    (date_trunc('hour', timestamp) +
        date_part('minute', timestamp)::int / 5 *
        interval '5 min') AS time_stamp,
    COUNT(*) AS num_data_points
FROM host_usage
GROUP BY host_id, time_stamp
HAVING COUNT(*) < 3
ORDER BY host_id, time_stamp;