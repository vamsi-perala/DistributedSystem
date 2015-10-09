#!/bin/bash


# Change this to your netid
netid=$2

#
# Root directory of your project
PROJDIR="`pwd`"

#
# This assumes your config file is named "config.txt"
# and is located in your project directory
#
CONFIG=$PROJDIR/$1

#
# Directory your java classes are in
#
BINDIR=$PROJDIR

#
# Your main project class
#
PROG=Project1
dc=dc
n=1

cat $CONFIG | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    #echo $i
    while read line 
    do
        host=$( echo $line | awk '{ print $2 }' )
	
	if test "${host#*$dc}" != "$host"
    then
           echo cleaning $host
	   	# $substring is in $string
    else
        break    # $substring is not in $string
    fi	
   #     echo $host
      ssh $netid@$host killall -u $netid &
        sleep 1

        n=$(( n + 1 ))
    done
   
)


echo "Cleanup complete"
