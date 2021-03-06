#! /bin/bash

USAGE="$0 psql_host psql_port db_name psql_user psql_password"

get_host_id() {
    host_id=$(PGPASSWORD="${psql_password}" psql -t -h "${psql_host}" -p "${port}" -d "${db_name}" \
        -U "${psql_user}" -c "${id_query}")
}

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
top_out="$(TERM=xterm top -n 1 -b)"

#usage data
timestamp=$(date --utc "+%Y-%m-%d %T")
get_host_id
memory_free=$(cat /proc/meminfo | grep -E "^MemFree:" | awk '{print $2}' | xargs)
cpu_idle=$(echo "$top_out" | grep -E "^%Cpu\(s\):" | awk '{print $8}' | xargs)
cpu_kernel=$(echo "$top_out" | grep -E "^%Cpu\(s\):" | awk '{print $4}' | xargs)
disk_io=$(vmstat -d | grep -E "^sda" | awk '{print $10}' | xargs)
disk_available=$(df -BM / | grep -E "^/dev/sda2" | awk '{print $4}' | xargs)

#if host not in host_info table yet, must add first.
if [ -z "$host_id" ]; then
    /home/centos/dev/jarvis_data_eng_Tomas/linux_sql/scripts/host_info.sh \
        "${psql_host}" "${port}" "${db_name}" "${psql_user}" "${psql_password}";
    get_host_id;
fi

insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, \
    disk_available) VALUES ('${timestamp}', ${host_id}, ${memory_free}, ${cpu_idle}, ${cpu_kernel}, \
    ${disk_io}, '${disk_available}');"

psql -h "${psql_host}" -p "${port}" -d "${db_name}" -U "${psql_user}" -c "${insert_stmt}"

exit $?