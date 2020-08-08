#!/bin/bash

################################################################################
# Functions                                                                    #
################################################################################

function error_exit {
  echo "${PROGNAME}: &{1:-"Unknown Error"}" 1>&2
  exit 1
}

function args()
{
    options=$(getopt --long projectDownload: --long projectCompile: --long projectRun: -- "$@")
    [[ $? -eq 0 ]] || {
        echo "Incorrect option provided"
        exit 1
    }
    eval set -- "$options"
    while true; do
        case "$1" in
        --projectDownload)
            shift;
            project_download=$1
            ;;
        --projectCompile)
            shift;
            project_compile=$1
            ;;
        --projectRun)
            shift;
             project_run=$1
            ;;
        --)
            shift
            break
            ;;
        esac
        shift
    done

}

################################################################################
# Variables                                                                    #
################################################################################

project_download=false
project_compile=false
project_run=false

################################################################################
# Project                                                                      #
################################################################################

args $0 "$@"

if [[ "$project_download" = true ]]
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

if [[ "$project_compile" = true ]]
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

if [[ "$project_run" = true ]]
  then
    java -jar /home/project/Task1/target/Task1.jar
fi

