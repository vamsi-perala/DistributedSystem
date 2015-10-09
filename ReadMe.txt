
Project Description

 The project is intended to facilitate communication in a distributed system of 'n' nodes, using TCP/IP communication .
Every node selects a label value (basically a random number) uniformly at random in the beginning. Every node then circulates a token through the system that visits each node in the system once and computes the sum of all the label values along the way. The path taken by the token of each node is specified in a configuration file. At the end, each node prints its label value and the sum of all the label values computed by its token.

Salient features of the project:
1) Implemented the Single Server-Multiple Client toplogy, to enable each server to handle multiple requests simultaneously.
2) The communication between different nodes is via TCP/IP sockets.
3) Applied multithreading concepts of Java, to enable parallel processing of the token requests from different nodes.
4) Handled exceptions wherever needed, to ensure smooth execution of the project.

Responsibilities of the member classes:
Project.java : This is the driver program.

Client: Initiates dynamic requests to neighbouring nodes of each path

Server: Serves the client requests, by forwarding the requests to a helper thread, called ClientWorker

ClientWorker: Called upon by the Server thread to perform computations like updating the token value and preparing the client request, reuired to contact the next neighbour in the path.

Prerequisites:
1) The project is intended to be executed on a distributed unix system. 
2) The current project is tailored to be executed on the distributed network at The University of Texas At Dallas. Minor changes need to be incorporated in the launcher script to execute the project on another distributed system.

Instructions to execute
1) Please unzip the contents of the project into your current directory

2) 
a) To execute the program, please navigate to the directory containing the project files.
and
b) Run the 'launcher.sh' script with the config file and netId as inputs. The 'launcher.sh' compiles and executes the program.

3) The output files will be created in the directory from where the script is launched.


Note: Sometimes the JVM holds on to the output files and they do not appear in the current directory. In that case, please execute the cleanup.sh script, which frees the files from the JVM and make them appear in the current directory.






