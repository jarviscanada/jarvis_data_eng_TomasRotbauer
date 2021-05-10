#!/bin/bash

#validate arguments
if [ "$#" -eq 0 ]; then
    echo >&2 "Error: parameter(s) missing"
    exit 1
fi

#Setup arguments
action=$1
db_username=$2
db_password=$3

#local variables
USAGE='usage: ./scripts/psql_docker.sh start|stop|create [db_username] [db_password]'
CONTAINER_EXISTS=$(docker container ls -a -f name=jrvs-psql | wc -l)

#start docker if docker server is not running
sudo systemctl status docker || systemctl start docker

#Handle argument action
case $action in
  (create)
    #check if the container `jrvs-psql` is created or not
    if [ "$CONTAINER_EXISTS" -eq 2 ]; then
      echo >&2 error: container is already running
      echo "$USAGE"
      exit 1
    elif [ "$#" -lt 3 ]; then
      echo >&2 error: db_username and/or db_password missing
      echo "$USAGE"
    else
      #get latest postgres image
      docker pull postgres
      #create a new volume if it does not exist
      docker volume create pgdata
      #create a container using psql image with name=jrvs-psql
      docker run --name jrvs-psql -e POSTGRES_PASSWORD="${db_password}" \
        -e POSTGRES_USER="${db_username}" -d -v pgdata:/var/lib/postgresql/data \
        -p 5432:5432 postgres
      exit $?
    fi;;

  (start | stop)
    if [ "$CONTAINER_EXISTS" -ne 2 ]; then
      echo >&2 error: the container does not exist
      echo "$USAGE"
      exit 1
    elif [ "$action" = start ]; then
      #start container
      docker container start jrvs-psql
      exit $?
    else
      #start stop the container
      docker container start jrvs-psql
      docker container stop jrvs-psql
      exit $?
    fi;;

  (*)
    echo ">&2 unknown command \"$action\"";
    echo "$USAGE";
    exit 1;;

esac