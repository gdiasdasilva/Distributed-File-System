package trab1;

/**
 * SD 13/14 - Trabalho pratico 1
 * Gonçalo Dias da Silva: 41831
 * Joao Francisco Pinto: 41887
 */

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IProxyDropbox extends Remote{
	
	/**
	 * Metodo criado para a listagem de uma directoria.
	 * @param server
	 * @param user
	 * @param dir
	 * @param currentUser
	 * @return
	 * @throws InfoNotFoundException
	 * @throws RemoteException
	 */
	public String[] dir(String dir) throws InfoNotFoundException, RemoteException;
	
	/**
	 * Metodo criado para auxiliar a criacao de uma nova directoria.
	 * @param dir
	 * @return
	 * @throws RemoteException
	 */
	public boolean mkdir(String dir) throws RemoteException;
	
	/**
	 * Metodo criado para a eliminacao de uma directoria.
	 * @param dir
	 * @return
	 * @throws RemoteException
	 */
	public boolean rmdir(String dir) throws RemoteException;
	
	/**
	 * Metodo criado para eliminacao de um ficheiro.
	 * @param dir
	 * @return
	 * @throws RemoteException
	 */
	public boolean rm(String dir) throws RemoteException;
	
	/**
	 * Metodo que retorna a informacao sobre um ficheiro ou directoria.
	 * @return - FileInfo
	 * @throws RemoteException
	 * @throws InfoNotFoundException 
	 */
	public FileInfo getAttr(String path) throws RemoteException, InfoNotFoundException; 
	
	/**
	 * 
	 * @throws RemoteException
	 */
	public void activeTest() throws RemoteException;

	public boolean copy(String string, String string2) throws RemoteException;
	

}

