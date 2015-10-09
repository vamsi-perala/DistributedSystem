#!/bin/bash


# Change this to your netid
netid=$2

#
# Root directory
PROJDIR="`pwd`"
javac Project1.java
#
# This assumes your config file is named "config.txt"
# and is located in your project directory
#
CONFIG=$PROJDIR/$1
CONF=$1
#
# Directory your java classes are in
#
#BINDIR=$PROJDIR/bin

#
# Your main project class
#

n=1
PROG=Project1
#hosts, ports, nodes, paths
cat $CONFIG | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
	numOfNodes="$(echo $i | head -c 1)"
	nodes=""
	hosts=""
	ports=""
	paths=""
	hostsArray=""
	portsArray=""
	k=0
    while [ $k -lt $numOfNodes ]
    do
		read line
		#nodes_temp=$( echo $line | awk '{ print $1 }' )
		#nodes=$nodes" "$nodes_temp
		
		hosts_temp=$( echo $line | awk '{ print $2 }' )
        	hosts=$hosts" "$hosts_temp
		hostsArray[$k]=$( echo $line | awk '{ print $2 }' )
		
		ports_temp=$( echo $line | awk '{ print $3}' )
		portsArray[$k]=$ports_temp
		k=$(( k +1 ))
        n=$(( n + 1 ))
    done

	while read line
    do
		paths_temp="$(echo -e "${line}" | tr -d '[[:space:]]')"
		paths=$paths" del "$paths_temp
    done
		paths_temp="$(echo -e "${line}" | tr -d '[[:space:]]')"
		paths=$paths" del "$paths_temp
k=0
	for port in "${portsArray[@]}" 
	do 
		l=${#port}
		#l=$(( l-1 ))
		temp=${port:0:l}
		ports=$ports" "$temp
		k=$(( k +1 ))
	done

	echo $CONF
	#exit
	k=0
	for host in "${hostsArray[@]}" 
	do 
		temp=$numOfNodes" "$k" "$CONF" "$paths
		echo $host $temp $hosts $ports 
		ssh -l "$netid" "$host"  "cd $PROJDIR; java $PROG $temp $hosts $ports" &
		#ssh $netid@$host java $BINDIR/$PROG $temp $ports $paths &
		k=$(( k +1 ))
	done
)



