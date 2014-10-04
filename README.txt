Sistemas Distribuídos 13/14
1º Trabalho prático

As classes a utilizar têm os seguintes nomes:

- FileServer (servidor de ficheiros RMI)
- FileServerWS (servidor de ficheiros WS)
- ContactServer
- FileClient

Inicia-se cada uma delas com os seguintes comandos:

File Server
java trab1.FileServer nome_do_servidor owner

File Server WS
java trab1.FileServerWS nome_do_servidor owner

Contact Server
java trab1.ContactServer

File Client
java trab1.FileClient utilizador

É possível dar-se o IP do Contact Server nos parâmetros de cada comando, sendo esse colocado a seguir ao nome_do_servidor nos File Servers e antes do utilizador no File Client.
