#! /bin/bash

USAGE='bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password'

#validate arguments
if [ "$#" -ne 5 ]; then
    echo >&2 "Illegal number of parameters"
    echo "${USAGE}"
    exit 1
fi

#Setup arguments
psql_host=$1
port=$2
db_name=$3
psql_user=$4
psql_password=$5

#save and parse usage data
#query to obtain host id
id_query="SELECT id FROM host_info WHERE hostname = '$(hostname -f)';"
top_out=$(top -n 1)

#usage data
timestamp=$(date --utc "+%Y-%m-%d %T")
host_id=$(PGPASSWORD="${psql_password}" psql -t -h "${psql_host}" -p "${port}" -d "${db_name}" \
    -U "${psql_user}" -c "${id_query}")
memory_free=$(cat /proc/meminfo | grep -E "^MemFree:" | awk '{print $2}' | xargs)
cpu_idle=$(echo "$top_out" | grep -E "^%Cpu\(s\):" | awk '{print $8}' | xargs)
cpu_kernel=$(echo "$top_out" | grep -E "^%Cpu\(s\):" | awk '{print $4}' | xargs)
disk_io=$(vmstat -d | grep -E "^sda" | awk '{print $10}' | xargs)
disk_available=$(df -BM / | grep -E "^/dev/sda2" | awk '{print $4}' | xargs)

if [ -z "$host_id" ]; then
  echo >&2 "Host was not found in host_info table. No insertions to host_usage were made."
  exit 1
fi

insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, \
    disk_available) VALUES ('${timestamp}', ${host_id}, ${memory_free}, ${cpu_idle}, ${cpu_kernel}, \
    ${disk_io}, '${disk_available}');"

PGPASSWORD="${psql_password}" psql -h "${psql_host}" -p "${port}" -d "${db_name}" -U "${psql_user}" -c "${insert_stmt}"

exit 0