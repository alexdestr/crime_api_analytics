#!/bin/bash

################################################################################
# Project                                                                      #
################################################################################

if [ "$project_download" = true ]
  then
    if pushd /home/project/
      then
        pushd /home/project/Task1
        git fetch
        echo "Project updated."
        popd
    else
        mkdir project
        pushd /home/project/
        git init
        git clone https://github.com/alexdestr/Task1
        echo "Project downloaded."
        popd
    fi
    popd
fi

if [ "$project_compile" = true ]
  then
    su - postgres << EOF
    export PGPASSWORD=1234
    psql
    \i /home/project/Task1/db/db.sql
    \c datapolice
    psql datapolice
    \i /home/project/Task1/db/db_create.sql
EOF
    pushd /home/project/Task1
    mvn clean package
    mvn package
    echo "Project builded."
    popd
fi

if [ "$project_run" = true ]
  then
    java -jar /home/project/Task1/target/Task1.jar
fi

