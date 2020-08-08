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
    options=$(getopt --long fullInstall: --long gitSetup: --long psqlSetup: --long javaSetup: --long mavenSetup: --long projectDownload: --long projectCompile: --long projectRun: -- "$@")
    [ $? -eq 0 ] || {
        echo "Incorrect option provided"
        exit 1
    }
    eval set -- "$options"
    while true; do
        case "$1" in
        --fullInstall)
            shift;
            git_setup=$1
            postgresql_setup=$1
            java_setup=$1
            maven_setup=$1
            project_download=$1
            project_compile=$1
            ;;
        --gitSetup)
            shift;
            git_setup=$1
            echo $git_setup
            ;;
        --psqlSetup)
            shift;
            postgresql_setup=$1
            ;;
        --javaSetup)
            shift;
            java_setup=$1
            ;;
        --mavenSetup)
            shift;
            maven_setup=$1
            ;;
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
}

################################################################################
# Variables                                                                    #
################################################################################

git_setup=false
postgresql_setup=false
java_setup=false
maven_setup=false
project_download=false
project_compile=false
project_run=false

################################################################################
################################################################################
# Main program                                                                 #
################################################################################
################################################################################

args $0 "$@"

if pushd /home/; then
   mkdir -p logs
   cd logs
   touch -c log
   touch -c error_log
else
   error_exit "$LINENO: An error has occured. Cannot change directory! Aborting."
fi
popd

exec 2>error_log # Redirect error output into to file
exec 3>log # Redirect log output into to file

yum -y update >&3
yum install -y wget >&3

if [ "$git_setup" = true ]
  then
    if which git | grep "git" >&3
      then
        echo "Git already installed"
    else
      yum install -y git >&3
    fi
fi

if [ "$postgresql_setup" = true ]
  then
    if which pgsql | grep "pgsql" >&3
      then
        echo "Postgresql already installed"
    else
      yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-6                                                                                                                                  -x86_64/pgdg-redhat-repo-latest.noarch.rpm >&3
      yum install -y postgresql12 >&3
      yum install -y postgresql12-server >&3
      service postgresql-12 initdb >&3
      chkconfig postgresql-12 on >&3
      service postgresql-12 start >&3
    fi
fi

if [ "$java_setup" = true ]
  then
    if which java | grep "java" >&3
      then
        echo "Java already installed"
    else
    yum install java-1.8.0-openjdk-devel
    fi
fi

if [ "$maven_setup" = true ]
  then
    if which mvn | grep "mvn" >&3
      then
        echo "Maven already installed"
    else
      if pushd /opt
        then
          wget https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz >&3
          tar xzf apache-maven-3.6.3-bin.tar.gz >&3
          ln -s apache-maven-3.6.3 maven >&3
          echo -n > /etc/profile.d/maven.sh >&3
          echo "export M2_HOME=/opt/maven" >> /etc/profile.d/maven.sh
          echo "export PATH=${M2_HOME}/bin:${PATH}" >> /etc/profile.d/maven.sh
          source /etc/profile.d/maven.sh >&3
          rm -f apache-maven-3.6.3-bin.tar.gz >&3
      else
        error_exit "$LINENO: An arror has occurred."
      fi
    fi
    popd
fi

export MAVEN_OPTS="-Xmx512m"


