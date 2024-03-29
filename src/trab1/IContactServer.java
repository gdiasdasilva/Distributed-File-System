package trab1;

/**
 * SD 13/14 - Trabalho pratico 1
 * Gon�alo Dias da Silva: 41831
 * Joao Francisco Pinto: 41887
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IContactServer extends Remote {
	
	/**
	 * Metodo para listar os servidores a que um utilizador tem acesso.
	 * @param userName
	 * @return
	 */
	public String[] listServers(String userName) throws RemoteException;
	
	/**
	 * Metodo que regista um servidor no servidor de contacto.
	 * @param serverName
	 * @param serverIP
	 * @param userName
	 */
	public boolean registerServer(String serverName, String serverIP, String userName) throws RemoteException;
	
	/**
	 * Metodo para adicionar permissao de acesso de um utilizador a um servidor.
	 * @return
	 */
	public boolean addPermission(String server, String userName, String owner) throws RemoteException; 
	
	/**
	 * Metodo para remover a permissao de acesso de um utilizador a um servidor
	 * @param server
	 * @param user
	 * @return
	 * @throws RemoteException
	 */
	public boolean remPermission( String server, String user, String owner) throws RemoteException;
	
	/**
	 * Devolve o endereco ip de um servidor dado o nome.
	 * @param server
	 * @return
	 */
	public String serverAddress(String server, String user) throws RemoteException;
	
	/**
	 * 
	 * @throws RemoteException
	 */
	public void clearUnavailableServers() throws RemoteException;

}
