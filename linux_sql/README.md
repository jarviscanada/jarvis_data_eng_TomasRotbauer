# Linux Cluster Monitoring Agent
## Introduction
In this project, I developed a minimum viable product (MVP) for 
demonstrating how hardware usage data could automatically be collected 
from Linux machines within a cluster and stored into a database. The 
applications of this project are numerous, and it may particularly prove 
useful to network administrators who may wish to understand how well a 
particular distributed system suits their specific processing and memory 
demands. The design works primarily with Linux bash scripts, which collect
all the necessary hardware usage data. These scripts are activated 
periodically by Linux's `crontab` command, and the data gets stored into 
a database managed with PostgreSQL. Another very important technology 
that was used is Docker, which allows the containerization of PostgreSQL.
This means that the database management system can run without actual 
installation on a host, making the design portable to any Linux machine.
This is especially useful for clusters. The project itself was developed
with Git version control, and stored in this GitHub repository. The 
Gitflow branching model was applied to facilitate continuous software 
development practices.

## Quick Start
Starting a PSQL instance using _psql_docker.sh_:<br />
`bash scripts/psql_docker.sh [start | stop | create] [db_username] [db_pasword]`

Creating the data tables in PSQL with _ddl.sql_:<br />
`psql -h psql_host -U psql_user -d db_name -f sql/ddl.sql`

Insert host hardware information into the host_info database table with _host_info.sh_:<br />
`bash scripts/host_info.sh psql_host psql_port db_name psql_user psql_password`

Insert host hardware usage information into the host_usage database table with _host_usage.sh_:<br />
`bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password`

Crontab setup script for usage data collection every minute:
```
crontab -e
# Insert the following line:
* * * * * bash /path/to/linux_sql/scripts/host_usage.sh psql_host port db_name psql_user psql_password &> /tmp/host_usage.log
```