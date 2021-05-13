#! /bin/bash

USAGE='./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password'

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

#save and parse hardware specification
lscpu_out=$(lscpu)
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | grep -E "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | grep -E "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | grep -E "^Model:" | awk '{print $2}' | xargs)
cpu_mhz=$(echo "$lscpu_out"  | grep -E "^CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out"  | grep -E "^L2 cache:" | awk '{print $3}' | xargs)
total_mem=$(cat /proc/meminfo | grep -E "MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date --utc "+%Y-%m-%d %T")

insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, \
    cpu_mhz, l2_cache, total_mem, timestamp) VALUES ('${hostname}', ${cpu_number}, '${cpu_architecture}', \
    '${cpu_model}', ${cpu_mhz}, '${l2_cache}', ${total_mem}, '${timestamp}');"

PGPASSWORD="${psql_password}" psql -h "${psql_host}" -p "${port}" -d "${db_name}" -U "${psql_user}" \
    -c "${insert_stmt}"

exit 0