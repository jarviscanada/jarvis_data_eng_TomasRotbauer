#! /bin/bash

USAGE="$0 psql_host psql_port db_name psql_user psql_password"

get_hardware_info() {
    local info
    info=$(echo "$lscpu_out"  | grep -E "$1" | awk "{print \$$2}" | xargs)
    echo "${info}"
}

#validate arguments
if [ "$#" -ne 5 ]; then
    echo >&2 "Illegal number of parameters"
    echo "Usage: ${USAGE}"
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
cpu_number="$(get_hardware_info "^CPU\(s\):" 2)"
cpu_architecture="$(get_hardware_info "^Architecture:" 2)"
cpu_model="$(get_hardware_info "^Model:" 2)"
cpu_mhz="$(get_hardware_info "^CPU MHz:" 3)"
l2_cache="$(get_hardware_info "^L2 cache:" 3)"

#Get memory and timestamp
total_mem=$(cat /proc/meminfo | grep -E "MemTotal:" | awk '{print $2}' | xargs)
timestamp=$(date --utc "+%Y-%m-%d %T")

insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, \
    cpu_mhz, l2_cache, total_mem, timestamp) VALUES ('${hostname}', ${cpu_number}, '${cpu_architecture}', \
    '${cpu_model}', ${cpu_mhz}, '${l2_cache}', ${total_mem}, '${timestamp}');"

psql -h "${psql_host}" -p "${port}" -d "${db_name}" -U "${psql_user}" \
    -c "${insert_stmt}"

exit $?