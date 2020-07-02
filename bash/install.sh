#!/bin/bash

#set -e

################################################################################
# Other functions                                                              #
################################################################################

function error_exit {
  echo "${PROGNAME}: &{1:-"Unknown Error"}" 1>&2
  exit 1
}

################################################################################
# Help                                                                         #
################################################################################

function display_help() {
   # Display Help
   echo "-----------------------------------"
   echo "Syntax: scriptTemplate [1 | 2 | 3 | 4]"
   echo "options:"
   echo "1. Default install."
   echo "2. Extended install."
   echo "3. Run project."
   echo "4. Print software version and exit."
   echo "-----------------------------------"
}

function display_extended_install() {
   # Display Help
   echo "-----------------------------------"
   echo "Choose option:"
   echo "1. Install git"
   echo "2. Install postgresql"
   echo "3. Install java"
   echo "4. Install maven"
   echo "5. Download/Update project"
   echo "6. Compile project"
   echo "-----------------------------------"
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

display_help
read user_input1

case $user_input1 in
   1)
     git_setup=true
     postgresql_setup=true
     java_setup=true
     maven_setup=true
     project_download=true
     project_compile=true
   ;;
   2)
     display_extended_install
     read user_input2
     case $user_input2 in
       1)
       git_setup=true
       ;;
       2)
       postgresql_setup=true
       ;;
       3)
       java_setup=true
       ;;
       4)
       maven_setup=true
       ;;
       5)
       project_download=true
       ;;
       6)
       project_compile=true
       ;;
     esac
   ;;
   3)
     project_run=true
   ;;
   4)
     echo "Version 1.0.0 ALPHA"
     exit 0
   ;;
esac

if cd /home/; then
   mkdir -p logs
   cd logs
   touch -c log
   touch -c error_log
else
     error_exit "$LINENO: An error has occured. Cannot change directory! Aborting."
fi

exec 2>error_log
exec 3>log

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
      yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-6-x86_64/pgdg-redhat-repo-latest.noarch.rpm >&3
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
#      yum install -y java-1.8.0-openjdk-devel >&3
      cd /usr/local/src/ && wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" \
"http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.rpm"
      yum localinstall jdk-8u*-linux-x64.rpm
      rm -f /usr/local/src/jdk-8u*-linux-x64.rpm
      cd /usr/local/src && wget --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u121-b13/e9e7ea248e2c4826b92b3f075a80e441/jre-8u121-linux-x64.rpm
      sudo yum localinstall jre-8u121-linux-x64.rpm
      rm -f /usr/local/src/jre-8u121-linux-x64.rpm
      {
        'JAVA_HOME=/usr/java/jdk1.8.0_121/'
        'PATH=$JAVA_HOME/bin:$PATH'
        'export PATH JAVA_HOME'
        'export CLASSPATH=.'
      } > /etc/profile.d/java.sh
      chmod +x /etc/profile.d/java.sh
      source /etc/profile.d/java.sh
fi
fi

if [ "$maven_setup" = true ]
  then
    if which mvn | grep "mvn" >&3
      then
        echo "Maven already installed"
    else
      if cd /opt
        then
          wget http://www-us.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz >&3
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
fi

export MAVEN_OPTS="-Xmx512m"
#export JAVA_HOME=/usr/java/jdk1.8.0_252/
#export PATH=$PATH:$JAVA_HOME

################################################################################
# Project                                                                      #
################################################################################

if [ "$project_download" = true ]
  then
    cd /home/
    if cd /home/project/
      then
        cd /home/project/Task1
        git clone https://github.com/alexdestr/Task1
        echo "Project updated."
    else
        mkdir project
        cd /home/project/
        git init
        git clone https://github.com/alexdestr/Task1
        echo "Project downloaded."
    fi
fi

if [ "$project_compile" = true ]
  then
    su - postgres << EOF
    export PGPASSWORD=1234
    psql
    CREATE DATABASE datapolice;
    \c datapolice
    psql datapolice
    \i /home/project/Task1/db/db_create.sql
EOF
    cd /home/project/Task1
    mvn package
    mvn compile
    echo "Project builded,"
fi

if [ "$project_run" = true ]
  then
    java -jar /home/project/Task1/target/Task1.jar
fi
