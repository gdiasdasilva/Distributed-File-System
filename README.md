#Distributed File System

First lab for Distributed System's course @ FCT/UNL, 2013/2014.

###Authors
* Gonçalo Dias da Silva
* João Francisco Pinto

###Main structure

The distributed file system is based on the following classes:

* FileServer (file server using in Java RMI)
* FileServerWS (file server using Web Services)
* ContactServer (class where the information (IP) about different clients and servers is stored)
* FileClient

###Running the distributed file system

#####File Server (RMI)
`java trab1.FileServer server_name owner`

#####File Server (Web Services)
`java trab1.FileServerWS server_name owner`

#####Contact Server
java trab1.ContactServer

#####File Client
java trab1.FileClient username

You can add the **Contact Server**'s IP as a parameter in the commands above. In the case of a File Server, the IP goes after `server_name`, but in instances of File Client, you should put the IP before `username`.

###Usage

* **servers** - shows the list of all online servers
* **addPermission server user2** - adds user2 to the list of users with permission to access the server *server@user* (being *user* the one that is running the program)
* **remPermission server user2** - removes user2 from the list of users with permission to access the server *server@user* (being *user* the one that is running the program)
* **ls server@user:dir** - shows the list of files/directories that exist at directory *dir*, in the server *server:user*
* **mkdir server@user:dir** - creates the directory *dir* in the server *server@user*
* **rmdir server@user:dir** - removes the directory *dir* from the server *server@user* if it is empty
* **cp path1 path2** - copies the file *path1* to *path2*, with *path1* and *path2* being writen *server@user:dir* or *dir* (the first one is the directory *dir* at *server@user*, the second one is local)
* **rm path** - removes the file at *path*
* **getattr path** - shows the information about the file/directory *path*
