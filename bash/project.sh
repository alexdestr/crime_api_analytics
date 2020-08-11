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
    options=$(getopt --long projectDownload: --long projectCompile: --long projectRun: --long Dtype: --long DdownloadData: --long DstartDate: --long DendDate: --long Drow: --long Dlng: --long Dlat: -- "$@")
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
        --Dtype)
            shift;
            Dtype=$1
            if [[ ! ${Dtype} ]]
            then
            Dtype=-Dtype=${Dtype}
            fi
            ;;
        --DdownloadData=$1)
            shift;
            DdownloadData=$1
            if [[ ! ${DdownloadData} ]]
            then
            DdownloadData=-DdownloadData=${DdownloadData}
            fi
            ;;
        --DstartDate)
            shift;
            DstartDate=$1
            if [[ ! ${DstartDate} ]]
            then
            DstartDate=-DstartDate=${DstartDate}
            fi
            ;;
        --DendDate)
            shift;
            DendDate=$1
            if [[ ! ${DendDate} ]]
            then
            DendDate=-DendDate=${DendDate}
            fi
            ;;
        --Drow)
            shift;
            Drow=$1
            if [[ ! ${Drow} ]]
            then
            Drow=-Drow=${Drow}
            fi
            ;;
        --Dlng)
            shift;
            Dlng=$1
            if [[ ! ${Dlng} ]]
            then
            Dlng=-Dlng-${Dlng}
            fi
            ;;
        --Dlat)
            shift;
            Dlat=$1
            if [[ ! ${Dlat} ]]
            then
            Dlat=-Dlat=${Dlat}
            fi
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

exec 2>error_log # Redirect error output into to file
exec 3>log # Redirect log output into to file

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
    pushd /home/project/Task1
    java -jar /home/project/Task1/target/Task1.jar ${Dtype} ${DdownloadData} ${DstartDate} ${DendDate} ${Drow} ${Dlng} ${Dlat}
    popd
fi

