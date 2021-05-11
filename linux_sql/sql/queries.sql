--Group hosts by hardware info
SELECT cpu_number, id as host_id, total_mem
FROM public.host_info
ORDER BY cpu_number, total_mem DESC;

--Average memory usage
SELECT usage.host_id,
       info.hostname as host_name,
       (date_trunc('hour', usage.timestamp) +
            date_part('minute', usage.timestamp)::int / 5 *
            interval '5 min') as timestamp,
       AVG (info.total_mem - usage.memory_free) / info.total_mem * 100
           as avg_used_mem_percentage
FROM public.host_info info JOIN public.host_usage usage
    ON info.id = usage.host_id
GROUP BY usage.host_id, host_name, timestamp;